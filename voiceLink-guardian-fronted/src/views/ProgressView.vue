<template>
  <div class="dashboard-container">
    <!-- 顶部导航栏 -->
    <div class="dashboard-header">
      <!-- 标题区域 -->
      <div class="title-section">
        <div class="title-decoration left"></div>
        <h1 class="dashboard-title">声联守护·进度仪表盘</h1>
        <div class="title-decoration right"></div>
      </div>
      
      <!-- 操作区域 -->
      <div class="header-actions">
        <div class="refresh-indicator">
          <div class="refresh-dot"></div>
          <span>实时更新中</span>
        </div>
        <button @click="resetAllProgress" class="reset-btn" :disabled="resetting">
          <span v-if="resetting">重置中...</span>
          <span v-else>🔄 重置进度</span>
        </button>
        <button @click="logout" class="logout-btn">退出登录</button>
      </div>
    </div>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>正在加载进度数据...</p>
    </div>

    <div v-else-if="error" class="error-container">
      <p>{{ error }}</p>
      <button @click="fetchProgress" class="retry-btn">重新加载</button>
    </div>

    <div v-else>
      <!-- 顶部概览区域 -->
      <div class="overview-section">
        <!-- 进度分布图容器 -->
        <div class="chart-container">
          <h3>小组进度分布</h3>
          <canvas id="progressChart" width="400" height="300"></canvas>
        </div>
      </div>

      <!-- 小组网格布局 -->
      <div class="groups-section">
        <div class="groups-grid">
          <div 
            v-for="item in progressData" 
            :key="item.userId"
            class="group-card"
            @click="openGroupDetails(item)"
          >
            <div class="group-header">
              <h3 class="group-name">{{ item.groupName }}</h3>
            </div>
            <div class="group-progress-bar">
              <div class="progress-fill" :style="{ width: getProgressPercentage(item) + '%' }"></div>
            </div>
            <div class="group-info">
              <div class="level-info">关卡 {{ item.currentLevel }}</div>
              <div class="progress-percentage">{{ getProgressPercentage(item) }}%</div>
            </div>
            <div class="group-hint">
              🔍 点击查看答案详情
            </div>
          </div>
        </div>
      </div>
      
      <!-- 分隔线 -->
      <div class="section-divider"></div>

      <!-- 所有小组答案信息卡片 -->
      <div class="all-groups-answers-section">
        <div class="all-groups-answers-card">
          <div class="card-header">
            <h3>所有小组关卡答案概览</h3>
            <p class="card-subtitle">展示10个小组的所有关卡答案信息</p>
          </div>
          <div class="answers-overview-content">
            <div class="answers-table-container">
              <table class="answers-table">
                <thead>
                  <tr>
                    <th>答案项</th>
                    <th v-for="item in progressData" :key="item.userId">{{ item.groupName }}</th>
                  </tr>
                </thead>
                <tbody>
                  <!-- 完成进度（第一行） -->
                  <tr style="background-color: #f0f4ff; font-weight: bold;">
                    <td class="answer-type">完成进度</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="mini-progress">
                        <div class="mini-progress-bar">
                          <div class="mini-progress-fill" :style="{ width: getProgressPercentage(item) + '%' }"></div>
                        </div>
                        <div class="mini-progress-text">{{ getProgressPercentage(item) }}%</div>
                      </div>
                    </td>
                  </tr>
                  <!-- 关卡1 -->
                  <tr class="level-1">
                    <td class="answer-type">关卡1：老人面临的困境</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'firstDilemma'))"
                           @click="toggleCellExpand(item.groupName, 'firstDilemma')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'firstDilemma') }">
                        {{ isCellExpanded(item.groupName, 'firstDilemma') ? 
                           getGroupAnswer(item.groupName, 'firstDilemma') : 
                           truncateText(getGroupAnswer(item.groupName, 'firstDilemma')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'firstDilemma') }}</div>
                    </td>
                  </tr>
                  <!-- 关卡2 -->
                  <tr class="level-2">
                    <td class="answer-type">关卡2：小组重点关注场景</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'secondScene'))"
                           @click="toggleCellExpand(item.groupName, 'secondScene')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'secondScene') }">
                        {{ isCellExpanded(item.groupName, 'secondScene') ? 
                           getGroupAnswer(item.groupName, 'secondScene') : 
                           truncateText(getGroupAnswer(item.groupName, 'secondScene')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'secondScene') }}</div>
                    </td>
                  </tr>
                  <tr class="level-2">
                    <td class="answer-type">关卡2：老人可能有的风险</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'secondRisk'))"
                           @click="toggleCellExpand(item.groupName, 'secondRisk')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'secondRisk') }">
                        {{ isCellExpanded(item.groupName, 'secondRisk') ? 
                           getGroupAnswer(item.groupName, 'secondRisk') : 
                           truncateText(getGroupAnswer(item.groupName, 'secondRisk')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'secondRisk') }}</div>
                    </td>
                  </tr>
                  <tr class="level-2">
                    <td class="answer-type">关卡2：老人喊出的指令1</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'secondDesign1'))"
                           @click="toggleCellExpand(item.groupName, 'secondDesign1')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'secondDesign1') }">
                        {{ isCellExpanded(item.groupName, 'secondDesign1') ? 
                           getGroupAnswer(item.groupName, 'secondDesign1') : 
                           truncateText(getGroupAnswer(item.groupName, 'secondDesign1')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'secondDesign1') }}</div>
                    </td>
                  </tr>
                  <tr class="level-2">
                    <td class="answer-type">关卡2：老人喊出的指令2</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'secondDesign2'))"
                           @click="toggleCellExpand(item.groupName, 'secondDesign2')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'secondDesign2') }">
                        {{ isCellExpanded(item.groupName, 'secondDesign2') ? 
                           getGroupAnswer(item.groupName, 'secondDesign2') : 
                           truncateText(getGroupAnswer(item.groupName, 'secondDesign2')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'secondDesign2') }}</div>
                    </td>
                  </tr>
                  <tr class="level-2">
                    <td class="answer-type">关卡2：安抚老人的语音</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'secondDesign3'))"
                           @click="toggleCellExpand(item.groupName, 'secondDesign3')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'secondDesign3') }">
                        {{ isCellExpanded(item.groupName, 'secondDesign3') ? 
                           getGroupAnswer(item.groupName, 'secondDesign3') : 
                           truncateText(getGroupAnswer(item.groupName, 'secondDesign3')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'secondDesign3') }}</div>
                    </td>
                  </tr>
                  <tr class="level-2">
                    <td class="answer-type">关卡2：通知给家人的信息</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'secondDesign4'))"
                           @click="toggleCellExpand(item.groupName, 'secondDesign4')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'secondDesign4') }">
                        {{ isCellExpanded(item.groupName, 'secondDesign4') ? 
                           getGroupAnswer(item.groupName, 'secondDesign4') : 
                           truncateText(getGroupAnswer(item.groupName, 'secondDesign4')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'secondDesign4') }}</div>
                    </td>
                  </tr>
                  <!-- 关卡3 -->
                  <tr class="level-3">
                    <td class="answer-type">关卡3：挑战</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'thirdChallenge'))"
                           @click="toggleCellExpand(item.groupName, 'thirdChallenge')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'thirdChallenge') }">
                        {{ isCellExpanded(item.groupName, 'thirdChallenge') ? 
                           getGroupAnswer(item.groupName, 'thirdChallenge') : 
                           truncateText(getGroupAnswer(item.groupName, 'thirdChallenge')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'thirdChallenge') }}</div>
                    </td>
                  </tr>
                  <!-- 关卡4 -->
                  <tr class="level-4">
                    <td class="answer-type">关卡4：小组选择的场景</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'fourthScene'))"
                           @click="toggleCellExpand(item.groupName, 'fourthScene')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'fourthScene') }">
                        {{ isCellExpanded(item.groupName, 'fourthScene') ? 
                           getGroupAnswer(item.groupName, 'fourthScene') : 
                           truncateText(getGroupAnswer(item.groupName, 'fourthScene')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'fourthScene') }}</div>
                    </td>
                  </tr>
                  <tr class="level-4">
                    <td class="answer-type">关卡4：小组实现的功能</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'fourthAchieve'))"
                           @click="toggleCellExpand(item.groupName, 'fourthAchieve')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'fourthAchieve') }">
                        {{ isCellExpanded(item.groupName, 'fourthAchieve') ? 
                           getGroupAnswer(item.groupName, 'fourthAchieve') : 
                           truncateText(getGroupAnswer(item.groupName, 'fourthAchieve')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'fourthAchieve') }}</div>
                    </td>
                  </tr>
                  <tr class="level-4">
                    <td class="answer-type">关卡4：额外添加的功能</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'fourthExtra'))"
                           @click="toggleCellExpand(item.groupName, 'fourthExtra')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'fourthExtra') }">
                        {{ isCellExpanded(item.groupName, 'fourthExtra') ? 
                           getGroupAnswer(item.groupName, 'fourthExtra') : 
                           truncateText(getGroupAnswer(item.groupName, 'fourthExtra')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'fourthExtra') }}</div>
                    </td>
                  </tr>
                  <tr class="level-4">
                    <td class="answer-type">关卡4：遇到的最大难题</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'fourthProblem'))"
                           @click="toggleCellExpand(item.groupName, 'fourthProblem')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'fourthProblem') }">
                        {{ isCellExpanded(item.groupName, 'fourthProblem') ? 
                           getGroupAnswer(item.groupName, 'fourthProblem') : 
                           truncateText(getGroupAnswer(item.groupName, 'fourthProblem')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'fourthProblem') }}</div>
                    </td>
                  </tr>
                  <tr class="level-4">
                    <td class="answer-type">关卡4：难题的解决方案</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'fourthSolution'))"
                           @click="toggleCellExpand(item.groupName, 'fourthSolution')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'fourthSolution') }">
                        {{ isCellExpanded(item.groupName, 'fourthSolution') ? 
                           getGroupAnswer(item.groupName, 'fourthSolution') : 
                           truncateText(getGroupAnswer(item.groupName, 'fourthSolution')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'fourthSolution') }}</div>
                    </td>
                  </tr>
                  <!-- 关卡5 -->
                  <tr class="level-5">
                    <td class="answer-type">关卡5：提升了技术理解</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'fifthComment1'))"
                           @click="toggleCellExpand(item.groupName, 'fifthComment1')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'fifthComment1') }">
                        {{ isCellExpanded(item.groupName, 'fifthComment1') ? 
                           getGroupAnswer(item.groupName, 'fifthComment1') : 
                           truncateText(getGroupAnswer(item.groupName, 'fifthComment1')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'fifthComment1') }}</div>
                    </td>
                  </tr>
                  <tr class="level-5">
                    <td class="answer-type">关卡5：清晰了系统理解</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'fifthComment2'))"
                           @click="toggleCellExpand(item.groupName, 'fifthComment2')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'fifthComment2') }">
                        {{ isCellExpanded(item.groupName, 'fifthComment2') ? 
                           getGroupAnswer(item.groupName, 'fifthComment2') : 
                           truncateText(getGroupAnswer(item.groupName, 'fifthComment2')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'fifthComment2') }}</div>
                    </td>
                  </tr>
                  <tr class="level-5">
                    <td class="answer-type">关卡5：掌握了哪些技能</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'fifthComment3'))"
                           @click="toggleCellExpand(item.groupName, 'fifthComment3')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'fifthComment3') }">
                        {{ isCellExpanded(item.groupName, 'fifthComment3') ? 
                           getGroupAnswer(item.groupName, 'fifthComment3') : 
                           truncateText(getGroupAnswer(item.groupName, 'fifthComment3')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'fifthComment3') }}</div>
                    </td>
                  </tr>
                  <tr class="level-5">
                    <td class="answer-type">关卡5：挑战最大的环节</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'fifthComment4'))"
                           @click="toggleCellExpand(item.groupName, 'fifthComment4')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'fifthComment4') }">
                        {{ isCellExpanded(item.groupName, 'fifthComment4') ? 
                           getGroupAnswer(item.groupName, 'fifthComment4') : 
                           truncateText(getGroupAnswer(item.groupName, 'fifthComment4')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'fifthComment4') }}</div>
                    </td>
                  </tr>
                  <tr class="level-5">
                    <td class="answer-type">关卡5：新功能建议</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'fifthComment5'))"
                           @click="toggleCellExpand(item.groupName, 'fifthComment5')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'fifthComment5') }">
                        {{ isCellExpanded(item.groupName, 'fifthComment5') ? 
                           getGroupAnswer(item.groupName, 'fifthComment5') : 
                           truncateText(getGroupAnswer(item.groupName, 'fifthComment5')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'fifthComment5') }}</div>
                    </td>
                  </tr>
                  <tr class="level-5">
                    <td class="answer-type">关卡5：技术认知的收获</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'fifthComment6'))"
                           @click="toggleCellExpand(item.groupName, 'fifthComment6')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'fifthComment6') }">
                        {{ isCellExpanded(item.groupName, 'fifthComment6') ? 
                           getGroupAnswer(item.groupName, 'fifthComment6') : 
                           truncateText(getGroupAnswer(item.groupName, 'fifthComment6')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'fifthComment6') }}</div>
                    </td>
                  </tr>
                  <tr class="level-6">
                    <td class="answer-type">关卡6：教学内容设计(5分)</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'sixthEduComment1'))"
                           @click="toggleCellExpand(item.groupName, 'sixthEduComment1')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'sixthEduComment1') }">
                        {{ isCellExpanded(item.groupName, 'sixthEduComment1') ? 
                           getGroupAnswer(item.groupName, 'sixthEduComment1') : 
                           truncateText(getGroupAnswer(item.groupName, 'sixthEduComment1')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'sixthEduComment1') }}</div>
                    </td>
                  </tr>
                  <tr class="level-6">
                    <td class="answer-type">关卡6：实践环节安排(5分)</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'sixthEduComment3'))"
                           @click="toggleCellExpand(item.groupName, 'sixthEduComment3')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'sixthEduComment3') }">
                        {{ isCellExpanded(item.groupName, 'sixthEduComment3') ? 
                           getGroupAnswer(item.groupName, 'sixthEduComment3') : 
                           truncateText(getGroupAnswer(item.groupName, 'sixthEduComment3')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'sixthEduComment3') }}</div>
                    </td>
                  </tr>

                  <tr class="level-6">
                    <td class="answer-type">关卡6：教学方法指导(5分)</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'sixthEduComment2'))"
                           @click="toggleCellExpand(item.groupName, 'sixthEduComment2')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'sixthEduComment2') }">
                        {{ isCellExpanded(item.groupName, 'sixthEduComment2') ? 
                           getGroupAnswer(item.groupName, 'sixthEduComment2') : 
                           truncateText(getGroupAnswer(item.groupName, 'sixthEduComment2')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'sixthEduComment2') }}</div>
                    </td>
                  </tr>

                  <tr class="level-6">
                    <td class="answer-type">关卡6：教学优点反馈</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'sixthFeedback1'))"
                           @click="toggleCellExpand(item.groupName, 'sixthFeedback1')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'sixthFeedback1') }">
                        {{ isCellExpanded(item.groupName, 'sixthFeedback1') ? 
                           getGroupAnswer(item.groupName, 'sixthFeedback1') : 
                           truncateText(getGroupAnswer(item.groupName, 'sixthFeedback1')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'sixthFeedback1') }}</div>
                    </td>
                  </tr>
                  <tr class="level-6">
                    <td class="answer-type">关卡6：改进建议</td>
                    <td v-for="item in progressData" :key="item.userId">
                      <div class="answer-cell" 
                           v-if="shouldTruncate(getGroupAnswer(item.groupName, 'sixthFeedback2'))"
                           @click="toggleCellExpand(item.groupName, 'sixthFeedback2')"
                           :class="{ 'expanded': isCellExpanded(item.groupName, 'sixthFeedback2') }">
                        {{ isCellExpanded(item.groupName, 'sixthFeedback2') ? 
                           getGroupAnswer(item.groupName, 'sixthFeedback2') : 
                           truncateText(getGroupAnswer(item.groupName, 'sixthFeedback2')) }}
                      </div>
                      <div v-else>{{ getGroupAnswer(item.groupName, 'sixthFeedback2') }}</div>
                    </td>
                  </tr>

                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 侧边栏抽屉 -->
    <div class="drawer" :class="{ 'drawer-open': showDrawer }">
      <div class="drawer-header">
        <h3>{{ selectedGroup?.groupName || '小组详情' }}</h3>
        <button class="close-drawer" @click="closeDrawer">×</button>
      </div>
      <div class="drawer-content">
        <div v-if="!selectedGroup" class="no-selection">
          <p>请选择一个小组查看详细信息</p>
        </div>
        <div v-else class="group-details">
          <!-- 标签页导航 -->
          <div class="tabs-nav">
            <button 
              v-for="tab in tabs" 
              :key="tab.id"
              class="tab-button"
              :class="{ active: activeTab === tab.id }"
              @click="activeTab = tab.id"
            >
              {{ tab.name }}
            </button>
          </div>
          <!-- 标签页内容 -->
          <div class="tab-content">
            <div v-show="activeTab === 'overview'" class="tab-overview">
              <h4>小组基本信息</h4>
              <div class="info-item">
                <span class="info-label">小组名称：</span>
                <span class="info-value">{{ selectedGroup.groupName }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">当前关卡：</span>
                <span class="info-value">关卡 {{ selectedGroup.currentLevel }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">完成进度：</span>
                <span class="info-value">{{ getProgressPercentage(selectedGroup) }}%</span>
              </div>
              <div class="info-item">
                <span class="info-label">状态：</span>
                <span class="info-value" :class="getStatusClass(selectedGroup)">{{ getStatusText(selectedGroup) }}</span>
              </div>
              <div class="info-item">
                
              </div>
            </div>
            

            
            <div v-show="activeTab === 'answers'" class="tab-answers">
              <h4>详细答案展示</h4>
              <div v-if="selectedGroupAnswers" class="answers-content">
                <div class="answer-section">
                  <h5>第一关：识别困境</h5>
                  <div class="answer-item">
                    <strong>问题：</strong>思考与讨论：视频中老人面临的最大困境是什么？
                  </div>
                  <div class="answer-item">
                    <strong>答案：</strong>{{ selectedGroupAnswers.firstDilemma || '未填写' }}
                  </div>
                </div>
                
                <div class="answer-section">
                  <h5>第二关：场景设计</h5>
                  <div class="answer-item">
                    <strong>重点场景：</strong>{{ selectedGroupAnswers.secondScene || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>主要风险：</strong>{{ selectedGroupAnswers.secondRisk || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>语音指令1：</strong>{{ selectedGroupAnswers.secondDesign1 || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>语音指令2：</strong>{{ selectedGroupAnswers.secondDesign2 || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>安抚语音：</strong>{{ selectedGroupAnswers.secondDesign3 || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>通知信息：</strong>{{ selectedGroupAnswers.secondDesign4 || '未填写' }}
                  </div>
                </div>
                
                <div class="answer-section">
                  <h5>第三关：进阶挑战</h5>
                  <div class="answer-item">
                    <strong>选择的挑战：</strong>{{ selectedGroupAnswers.thirdChallenge || '未填写' }}
                  </div>
                </div>
                
                <div class="answer-section">
                  <h5>第四关：功能实现</h5>
                  <div class="answer-item">
                    <strong>选择场景：</strong>{{ selectedGroupAnswers.fourthScene || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>实现功能：</strong>{{ selectedGroupAnswers.fourthAchieve || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>额外功能：</strong>{{ selectedGroupAnswers.fourthExtra || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>遇到难题：</strong>{{ selectedGroupAnswers.fourthProblem || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>解决方法：</strong>{{ selectedGroupAnswers.fourthSolution || '未填写' }}
                  </div>
                </div>
                
                <div class="answer-section" v-if="selectedGroupAnswers.fifthComment1">
                  <h5>第五关：总结反思</h5>
                  <div class="answer-item">
                    <strong>技能提升：</strong>{{ selectedGroupAnswers.fifthComment1 || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>系统理解：</strong>{{ selectedGroupAnswers.fifthComment2 || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>掌握技能：</strong>{{ selectedGroupAnswers.fifthComment3 || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>最大挑战：</strong>{{ selectedGroupAnswers.fifthComment4 || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>新功能建议：</strong>{{ selectedGroupAnswers.fifthComment5 || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>技术认知：</strong>{{ selectedGroupAnswers.fifthComment6 || '未填写' }}
                  </div>
                </div>
                
                <div class="answer-section" v-if="selectedGroupAnswers.sixthEduComment1">
                  <h5>第六关：教学评价</h5>
                  <div class="answer-item">
                    <strong>教学内容设计（星级）：</strong>{{ selectedGroupAnswers.sixthEduComment1 || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>教学方法与指导（星级）：</strong>{{ selectedGroupAnswers.sixthEduComment2 || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>实践环节安排（星级）：</strong>{{ selectedGroupAnswers.sixthEduComment3 || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>教学优点：</strong>{{ selectedGroupAnswers.sixthFeedback1 || '未填写' }}
                  </div>
                  <div class="answer-item">
                    <strong>改进建议：</strong>{{ selectedGroupAnswers.sixthFeedback2 || '未填写' }}
                  </div>
                </div>
              </div>
              <div v-else class="no-answers">
                <p>暂无详细答案数据</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 遮罩层 -->
    <div class="overlay" :class="{ 'overlay-active': showDrawer }" @click="closeDrawer"></div>
  </div>
</template>

<script>
import { getAllUserProgress, getAllUserDesign, deleteAllUserProgress, getAllUserAnswer } from '../api/user'
import { logout } from '../utils/auth'
import { ensureUserInfo, isTeacher, clearUserInfo } from '../utils/auth'
import Chart from 'chart.js/auto'

export default {
  name: 'ProgressView',
  data() {
    return {
      progressData: [],
      designData: [], // 设计蓝图数据
      allAnswersData: [], // 存储所有用户答案数据
      loading: false,
      error: null,
      refreshInterval: null, // 定时器ID
      resetting: false, // 重置状态
      chartInstance: null, // 图表实例
      expandedCells: [], // 存储展开的单元格信息
      
      // 侧边栏抽屉相关状态
      showDrawer: false,
      selectedGroup: null,
      selectedGroupAnswers: null,
      
      // 标签页相关状态
      activeTab: 'overview',
      tabs: [
        { id: 'overview', name: '概览' },
        { id: 'answers', name: '答案详情' }
      ]
    }
  },
  async mounted() {
    await this.checkAuth()
    this.fetchProgress()
    this.startAutoRefresh()
  },
    beforeUnmount() {
      this.stopAutoRefresh()
      // 销毁图表实例
      if (this.chartInstance) {
        this.chartInstance.destroy();
        this.chartInstance = null;
      }
    },
  methods: {
    // 切换单元格展开状态
    toggleCellExpand(groupName, fieldName) {
      const cellKey = `${groupName}-${fieldName}`;
      const index = this.expandedCells.indexOf(cellKey);
      if (index > -1) {
        this.expandedCells.splice(index, 1);
      } else {
        this.expandedCells.push(cellKey);
      }
      // Vue的响应式系统会自动更新DOM，不需要手动操作class
    },
    
    // 检查单元格是否已展开
    isCellExpanded(groupName, fieldName) {
      const cellKey = `${groupName}-${fieldName}`;
      return this.expandedCells.includes(cellKey);
    },
    
    // 判断文本是否需要截断（现在所有非空文本都视为需要截断，确保都可以点击展开）
    shouldTruncate(text, maxLength = 20) {
      return !!text; // 只要文本不为空就返回true
    },
    
    // 截断文本
    truncateText(text, maxLength = 20) {
      if (!text) return '';
      return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
    },
    
    startAutoRefresh() {
      // 每秒刷新一次数据
      this.refreshInterval = setInterval(() => {
        this.fetchProgress()
      }, 1000)
    },
    
    stopAutoRefresh() {
      // 清除定时器
      if (this.refreshInterval) {
        clearInterval(this.refreshInterval)
        this.refreshInterval = null
      }
    },
    
    getChineseGroupName(number) {
      const chineseNumbers = ['', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十']
      return `第${chineseNumbers[number]}组`
    },
    
    async checkAuth() {
      try {
        // 确保用户信息存在，如果本地没有则从服务器获取
        const userInfo = await ensureUserInfo()
        
        if (!userInfo) {
          // 如果没有用户信息，跳转到登录页面
          this.$router.push('/')
          return
        }
        
        // 检查是否是教师，如果不是教师则跳转到闯关页面
        if (!isTeacher()) {
          this.$router.push('/game')
          return
        }
      } catch (error) {
        console.error('权限验证失败:', error)
        this.$router.push('/')
      }
    },

    async fetchProgress() {
      // 如果不是第一次加载，不显示loading状态，避免闪烁
      if (this.progressData.length === 0) {
        this.loading = true
      }
      
      this.error = null
      
      try {
        // 同时获取进度数据、设计蓝图数据和答案数据
        const [progressResponse, designResponse, answerResponse] = await Promise.all([
          getAllUserProgress(),
          this.fetchDesignData(),
          getAllUserAnswer()
        ])
        
        // 处理进度数据
        let progressData = []
        if (progressResponse.data && progressResponse.data.code === 0) {
          progressData = progressResponse.data.data || []
        } else if (Array.isArray(progressResponse.data)) {
          progressData = progressResponse.data
        } else if (Array.isArray(progressResponse)) {
          progressData = progressResponse
        } else {
          throw new Error('未知的响应格式')
        }
        
        // 过滤掉教师数据
        progressData = progressData.filter(item => item.groupName !== 'teacher')
        
        // 创建6个组的完整数据
        const finalGroups = []
        const existingGroupsMap = new Map()
        
        // 将现有数据按中文组名提取组号存入Map
        progressData.forEach(item => {
          const chineseNumberMap = {
            '一': 1, '二': 2, '三': 3, '四': 4, '五': 5, '六': 6,
            '七': 7, '八': 8, '九': 9, '十': 10
          }
          
          const match = item.groupName.match(/第([一二三四五六七八九十]+)组/)
          if (match) {
            const chineseNumber = match[1]
            const groupNum = chineseNumberMap[chineseNumber]
            if (groupNum && groupNum >= 1 && groupNum <= 10) {
              existingGroupsMap.set(groupNum, item)
            }
          }
        })
        
        // 生成完整的10个组数据
        for (let i = 1; i <= 10; i++) {
          if (existingGroupsMap.has(i)) {
            // 使用现有数据
            const existingItem = existingGroupsMap.get(i)
            finalGroups.push({
              ...existingItem,
              groupName: this.getChineseGroupName(i) // 确保使用标准中文组名
            })
          } else {
            // 创建空数据，默认总关卡数为6（totalLevels-1=5）
            finalGroups.push({
              groupName: this.getChineseGroupName(i),
              userId: i.toString(),
              currentLevel: 0,
              totalLevels: 6  // 默认6关，实际使用totalLevels-1=5作为总关卡数
            })
          }
        }
        
        this.progressData = finalGroups
        
        // 处理答案数据
        if (answerResponse.data && answerResponse.data.code === 0) {
          this.allAnswersData = answerResponse.data.data || []
        } else if (Array.isArray(answerResponse.data)) {
          this.allAnswersData = answerResponse.data
        } else if (Array.isArray(answerResponse)) {
          this.allAnswersData = answerResponse
        }
        
        console.log('更新后的答案数据:', this.allAnswersData)
      } catch (error) {
        console.error('获取进度数据失败:', error)
        this.error = `获取进度数据失败: ${error.response?.data?.message || error.message || '未知错误'}`
      } finally {
        this.loading = false
      }
    },

    async fetchDesignData() {
      try {
        const response = await getAllUserDesign()
        
        // 处理设计蓝图数据
        let designData = []
        if (response.data && response.data.code === 0) {
          designData = response.data.data || []
        } else if (Array.isArray(response.data)) {
          designData = response.data
        } else if (Array.isArray(response)) {
          designData = response
        }
        
        // 过滤掉教师数据
        this.designData = designData.filter(item => item.groupName !== 'teacher')
        
        return designData
      } catch (error) {
        console.error('获取设计蓝图数据失败:', error)
        this.designData = []
        return []
      }
    },

    getGroupDesignData(groupName) {
      // 根据组名查找对应的设计蓝图数据
      const designItem = this.designData.find(item => item.groupName === groupName)
      
      if (designItem) {
        return {
          coreScene: designItem.coreScene || '暂未填写',
          presetVoiceCommand: designItem.presetVoiceCommand || '暂未填写',
          systemResponseLogic: designItem.systemResponseLogic || '暂未填写'
        }
      }
      
      // 如果没有找到对应的数据，返回默认值
      return {
        coreScene: '暂未填写',
        presetVoiceCommand: '暂未填写',
        systemResponseLogic: '暂未填写'
      }
    },

    getProgressPercentage(item) {
      // 使用totalLevels-1作为总关卡数
      const totalLevels = Math.max(1, item.totalLevels - 1)
      
      if (totalLevels === 0) return 0
      
      // 特殊处理：当currentLevel=totalLevels-1时显示99%（最后一关）
      if (item.currentLevel === totalLevels) {
        return 99
      }
      
      // 当currentLevel=totalLevels时显示100%（已通关）
      if (item.currentLevel >= item.totalLevels) {
        return 100
      }
      
      // 正常计算进度百分比
      return Math.round((item.currentLevel / totalLevels) * 100)
    },
    
    getStatusClass(item) {
      // 使用totalLevels-1作为总关卡数
      const totalLevels = Math.max(1, item.totalLevels - 1)
      
      // 当currentLevel=0时显示未登录样式
      if (item.currentLevel === 0) {
        return 'status-not-logged'
      }
      
      // 当currentLevel=totalLevels时显示已通关样式
      if (item.currentLevel >= item.totalLevels) {
        return 'status-completed'
      }
      
      // 当currentLevel=totalLevels-1时显示最后一关样式
      if (item.currentLevel === totalLevels) {
        return 'status-final-level'
      }
      
      // 当currentLevel>0且currentLevel<totalLevels-1时显示进行中样式
      if (item.currentLevel > 0 && item.currentLevel < totalLevels) {
        return 'status-halfway'
      }
      
      // 其他情况默认显示未登录样式
      return 'status-not-logged'
    },
    
    getStatusText(item) {
      // 使用totalLevels-1作为总关卡数
      const totalLevels = Math.max(1, item.totalLevels - 1)
      
      // 当currentLevel=0时显示"未登录"
      if (item.currentLevel === 0) {
        return '未登录'
      }
      
      // 当currentLevel=totalLevels-1时显示"最后一关"
      if (item.currentLevel === totalLevels) {
        return '最后一关'
      }
      
      // 当currentLevel=totalLevels时显示"已通关"
      if (item.currentLevel >= item.totalLevels) {
        return '已通关'
      }
      
      // 当currentLevel>0且currentLevel<totalLevels-1时显示"进行中"
      if (item.currentLevel > 0 && item.currentLevel < totalLevels) {
        return '进行中'
      }
      
      // 其他情况默认显示"未登录"
      return '未登录'
    },
    
    async logout() {
      try {
        console.log('开始退出登录...')
        
        // 调用auth.js中的logout函数，确保调用后端退出登录接口并清除本地信息
        await logout()
        console.log('退出登录成功')
      } catch (error) {
        console.error('调用退出登录接口失败:', error)
        // auth.js中的logout函数已经处理了失败情况并清除了本地信息
      } finally {
        // 无论成功失败，都跳转到登录页面
        this.$router.push('/')
      }
    },

    getSystemLogic(item) {
      // 根据当前关卡生成系统响应逻辑
      const level = item.currentLevel
      const logicMap = {
        0: "系统未检测到用户登录，请先完成身份验证",
        1: "语音识别引擎启动 → 关卡环境初始化 → 等待用户指令输入",
        2: "自然语言处理模块激活 → 语义分析 → 匹配预设指令库",
        3: "多模态感知系统运行 → 语音+视觉融合 → 智能决策生成",
        4: "机器学习模型推理 → 实时反馈计算 → 动态难度调整",
        5: "综合评估系统启动 → 成绩统计分析 → 学习路径优化",
        6: "系统完成所有关卡测试 → 生成个性化学习报告 → 数据归档"
      }
      return logicMap[level] || "系统响应逻辑初始化中..."
    },

    getCoreScenario(item) {
      // 根据当前关卡生成核心场景
      const level = item.currentLevel
      const scenarioMap = {
        0: "用户未登录系统，等待身份验证",
        1: "基础语音交互场景：用户通过语音指令控制虚拟助手",
        2: "智能对话场景：系统理解自然语言并进行智能回复",
        3: "多模态交互场景：结合语音、视觉的综合交互体验",
        4: "个性化学习场景：AI根据用户表现调整学习内容",
        5: "综合评估场景：全方位评估用户的学习成果",
        6: "项目完成场景：用户成功完成所有学习任务"
      }
      return scenarioMap[level] || "场景加载中..."
    },

    getVoiceCommands(item) {
      // 根据当前关卡生成预设语音指令
      const level = item.currentLevel
      const commandsMap = {
        0: "请先登录系统开始学习",
        1: "'开始学习'、'帮助'、'重复一遍'、'下一页'",
        2: "'你好'、'今天天气怎么样'、'介绍一下这个功能'、'返回主页'",
        3: "'打开摄像头'、'识别这个物体'、'播放音乐'、'调节音量'",
        4: "'我的学习进度'、'推荐练习'、'调整难度'、'查看报告'",
        5: "'开始测试'、'查看成绩'、'分析错误'、'学习建议'",
        6: "'完成项目'、'查看证书'、'分享成果'、'开始新项目'"
      }
      return commandsMap[level] || "指令加载中..."
    },

    // 打开小组详情侧边栏
    async openGroupDetails(group) {
      this.selectedGroup = group;
      this.showDrawer = true;
      this.activeTab = 'overview';
      
      try {
        // 调用获取所有用户答案的API
        const response = await getAllUserAnswer();
        if (response.code === 0 && response.data) {
          // 从所有答案中筛选出当前小组的答案
          this.selectedGroupAnswers = response.data.find(
            answer => answer.groupName === group.groupName
          ) || this.getMockGroupAnswers(group.groupName);
        } else {
          // 如果API调用失败或没有数据，使用模拟数据
          console.warn('获取答案数据失败，使用模拟数据:', response.message);
          this.selectedGroupAnswers = this.getMockGroupAnswers(group.groupName);
        }
      } catch (error) {
        console.error('调用获取用户答案API失败:', error);
        // 发生错误时使用模拟数据
        this.selectedGroupAnswers = this.getMockGroupAnswers(group.groupName);
      }
    },
    
    // 关闭侧边栏
    closeDrawer() {
      this.showDrawer = false;
      this.selectedGroup = null;
      this.selectedGroupAnswers = null;
      this.activeTab = 'overview';
      document.body.style.overflow = '';
    },

    // 根据组名和字段名获取小组答案信息
    getGroupAnswer(groupName, fieldName) {
      try {
        // 输入验证
        if (!groupName || !fieldName) {
          console.warn('获取答案时缺少必要参数');
          return '暂未填写';
        }
        
        // 只从allAnswersData中获取数据，不使用模拟数据
        if (Array.isArray(this.allAnswersData) && this.allAnswersData.length > 0) {
          const groupAnswer = this.allAnswersData.find(answer => 
            answer && answer.groupName === groupName
          );
          if (groupAnswer && typeof groupAnswer === 'object' && fieldName in groupAnswer && groupAnswer[fieldName]) {
            return groupAnswer[fieldName];
          }
        }
        
        // 数据不存在或未填写时返回'暂未填写'
        return '暂未填写';
      } catch (error) {
        console.error(`获取小组${groupName}的${fieldName}答案失败:`, error);
        return '暂未填写';
      }
    },
    
    // 模拟小组答案数据（实际应该从API获取）
    getMockGroupAnswers(groupName) {
      const answersMap = {
        '第一组': {
          userId: '21',
          groupName: '第一组',
          firstDilemma: 'A:摔倒本身',
          secondScene: '卫生间',
          secondRisk: '摔倒',
          secondDesign1: '救命啊',
          secondDesign2: '我摔倒了',
          secondDesign3: '请保持冷静，正在联系家人',
          secondDesign4: '发送位置信息给家人',
          thirdChallenge: '进阶挑战一（温湿度传感器火灾报警）',
          fourthScene: '厨房',
          fourthAchieve: '实现温湿度监测和报警功能',
          fourthExtra: '添加了短信通知功能',
          fourthProblem: 'MQTT连接不稳定',
          fourthSolution: '增加了重连机制',
          fifthComment1: '良好',
          fifthComment2: '优秀',
          fifthComment3: '使用Mind+进行图形化编程、理解并配置MQTT协议参数',
          fifthComment4: '语音识别程序编写与调试',
          fifthComment5: '增加视频监控功能',
          fifthComment6: '技术应该以人为本，关注老年人实际需求'
        },
        '第二组': {
          userId: '22',
          groupName: '第二组',
          firstDilemma: 'B:无法求助',
          secondScene: '卧室',
          secondRisk: '突发疾病',
          secondDesign1: '我不舒服',
          secondDesign2: '救我',
          secondDesign3: '已启动紧急救助',
          secondDesign4: '联系急救中心',
          thirdChallenge: '进阶挑战二（语音控制开关灯）',
          fourthScene: '卧室',
          fourthAchieve: '实现语音控制灯光',
          fourthExtra: '添加了定时功能',
          fourthProblem: '语音识别准确率低',
          fourthSolution: '增加了关键词训练',
          fifthComment1: '优秀',
          fifthComment2: '良好',
          fifthComment3: '语音识别技术应用',
          fifthComment4: '硬件连接与调试',
          fifthComment5: '增加健康监测功能',
          fifthComment6: '技术应简单易用，适合老年人使用'
        },
        '第三组': {
          userId: '23',
          groupName: '第三组',
          firstDilemma: 'C:无人发现',
          secondScene: '客厅',
          secondRisk: '煤气泄漏',
          secondDesign1: '煤气泄漏',
          secondDesign2: '危险',
          secondDesign3: '正在关闭煤气阀',
          secondDesign4: '打开窗户通风',
          thirdChallenge: '进阶挑战三（智能门锁控制）',
          fourthScene: '门口',
          fourthAchieve: '实现智能门锁控制',
          fourthExtra: '添加了人脸识别功能',
          fourthProblem: '识别速度慢',
          fourthSolution: '优化算法',
          fifthComment1: '良好',
          fifthComment2: '良好',
          fifthComment3: '智能识别技术应用',
          fifthComment4: '系统集成能力',
          fifthComment5: '增加远程监控功能',
          fifthComment6: '技术应提高老年人生活安全性'
        },
        '第四组': {
          userId: '24',
          groupName: '第四组',
          firstDilemma: 'A:摔倒本身',
          secondScene: '浴室',
          secondRisk: '滑倒',
          secondDesign1: '我滑倒了',
          secondDesign2: '拉我起来',
          secondDesign3: '已通知家人',
          secondDesign4: '医护人员正在赶来',
          thirdChallenge: '进阶挑战四（智能药箱提醒）',
          fourthScene: '餐厅',
          fourthAchieve: '实现定时服药提醒',
          fourthExtra: '添加了药物识别功能',
          fourthProblem: '提醒声音太小',
          fourthSolution: '增加震动提醒',
          fifthComment1: '优秀',
          fifthComment2: '良好',
          fifthComment3: '智能提醒系统设计',
          fifthComment4: '用户体验优化',
          fifthComment5: '增加药物管理功能',
          fifthComment6: '技术应关注老年人健康管理'
        },
        '第五组': {
          userId: '25',
          groupName: '第五组',
          firstDilemma: 'B:无法求助',
          secondScene: '书房',
          secondRisk: '突发疾病',
          secondDesign1: '我头晕',
          secondDesign2: '我难受',
          secondDesign3: '正在联系医生',
          secondDesign4: '请稍等救援',
          thirdChallenge: '进阶挑战一（温湿度传感器火灾报警）',
          fourthScene: '厨房',
          fourthAchieve: '实现智能报警系统',
          fourthExtra: '添加了烟雾检测功能',
          fourthProblem: '误报率高',
          fourthSolution: '优化传感器灵敏度',
          fifthComment1: '良好',
          fifthComment2: '优秀',
          fifthComment3: '环境监测技术应用',
          fourthComment4: '报警系统设计',
          fifthComment5: '增加多传感器融合',
          fifthComment6: '技术应提供可靠的安全保障'
        },
        '第六组': {
          userId: '26',
          groupName: '第六组',
          firstDilemma: 'C:无人发现',
          secondScene: '阳台',
          secondRisk: '跌倒',
          secondDesign1: '我摔倒了',
          secondDesign2: '快来人',
          secondDesign3: '已启动定位系统',
          secondDesign4: '正在通知紧急联系人',
          thirdChallenge: '进阶挑战二（语音控制开关灯）',
          fourthScene: '卧室',
          fourthAchieve: '实现全屋灯光控制',
          fourthExtra: '添加了场景模式',
          fourthProblem: '语音识别距离有限',
          fourthSolution: '增加麦克风阵列',
          fifthComment1: '优秀',
          fifthComment2: '优秀',
          fifthComment3: '语音交互系统设计',
          fourthComment4: '智能家居集成',
          fifthComment5: '增加手势控制功能',
          fifthComment6: '技术应提高老年人生活便利性'
        },
        '第七组': {
          userId: '27',
          groupName: '第七组',
          firstDilemma: 'A:摔倒本身',
          secondScene: '楼梯',
          secondRisk: '摔伤',
          secondDesign1: '救命',
          secondDesign2: '有人吗',
          secondDesign3: '已开启求助模式',
          secondDesign4: '正在发送位置信息',
          thirdChallenge: '进阶挑战三（智能门锁控制）',
          fourthScene: '门口',
          fourthAchieve: '实现远程开门功能',
          fourthExtra: '添加了异常行为检测',
          fourthProblem: '连接不稳定',
          fourthSolution: '增加备用连接',
          fifthComment1: '良好',
          fifthComment2: '良好',
          fifthComment3: '安全系统设计',
          fourthComment4: '远程控制技术',
          fifthComment5: '增加身份验证功能',
          fifthComment6: '技术应保障老年人居家安全'
        },
        '第八组': {
          userId: '28',
          groupName: '第八组',
          firstDilemma: 'B:无法求助',
          secondScene: '车库',
          secondRisk: '突发疾病',
          secondDesign1: '我需要帮助',
          secondDesign2: '快来救我',
          secondDesign3: '正在呼叫救护车',
          fourthScene: '请等待救援',
          thirdChallenge: '进阶挑战四（智能药箱提醒）',
          fourthScene: '卧室',
          fourthAchieve: '实现药物管理系统',
          fourthExtra: '添加了用药记录功能',
          fourthProblem: '界面操作复杂',
          fourthSolution: '简化用户界面',
          fifthComment1: '优秀',
          fifthComment2: '良好',
          fifthComment3: '健康管理系统设计',
          fourthComment4: '用户界面优化',
          fifthComment5: '增加智能推荐功能',
          fifthComment6: '技术应简单易用且实用'
        },
        '第九组': {
          userId: '29',
          groupName: '第九组',
          firstDilemma: 'C:无人发现',
          secondScene: '花园',
          secondRisk: '中暑',
          secondDesign1: '我不舒服',
          secondDesign2: '太热了',
          secondDesign3: '正在调整环境',
          fourthScene: '请转移到阴凉处',
          thirdChallenge: '进阶挑战一（温湿度传感器火灾报警）',
          fourthScene: '客厅',
          fourthAchieve: '实现环境监测系统',
          fourthExtra: '添加了天气预报功能',
          fourthProblem: '数据刷新慢',
          fourthSolution: '优化数据更新频率',
          fifthComment1: '良好',
          fifthComment2: '优秀',
          fifthComment3: '环境监测技术集成',
          fourthComment4: '数据可视化设计',
          fifthComment5: '增加预警功能',
          fifthComment6: '技术应关注老年人生活环境'
        },
        '第十组': {
          userId: '30',
          groupName: '第十组',
          firstDilemma: 'A:摔倒本身',
          secondScene: '健身房',
          secondRisk: '运动损伤',
          secondDesign1: '我受伤了',
          secondDesign2: '需要医疗救助',
          secondDesign3: '已启动紧急救助',
          fourthScene: '正在联系医护人员',
          thirdChallenge: '进阶挑战二（语音控制开关灯）',
          fourthScene: '全屋',
          fourthAchieve: '实现智能情景控制',
          fourthExtra: '添加了语音助手功能',
          fourthProblem: '响应不够及时',
          fourthSolution: '优化处理算法',
          fifthComment1: '优秀',
          fifthComment2: '优秀',
          fifthComment3: '智能家居生态设计',
          fourthComment4: '语音交互优化',
          fifthComment5: '增加健康监测功能',
          fifthComment6: '技术应成为老年人的得力助手'
        }
      };
      
      return answersMap[groupName] || null;
    },

    // 初始化图表
    initChart() {
      try {
        // 获取canvas元素
        const ctx = document.getElementById('progressChart');
        if (!ctx) return;
        
        // 销毁已存在的图表实例
        if (this.chartInstance) {
          this.chartInstance.destroy();
        }
        
        // 准备图表数据
        const labels = this.progressData.map(item => item.groupName);
        const progressData = this.progressData.map(item => this.getProgressPercentage(item));
        
        // 生成颜色数组
        const colors = this.generateChartColors(progressData.length);
        
        // 创建图表实例
        this.chartInstance = new Chart(ctx, {
          type: 'bar', // 使用条形图展示进度对比
          data: {
            labels: labels,
            datasets: [{
              label: '完成率 (%)',
              data: progressData,
              backgroundColor: colors,
              borderColor: colors.map(color => this.adjustColorBrightness(color, -20)),
              borderWidth: 1
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              legend: {
                display: false
              },
              tooltip: {
                callbacks: {
                  label: function(context) {
                    return `完成率: ${context.raw}%`;
                  }
                }
              }
            },
            scales: {
              y: {
                beginAtZero: true,
                max: 100,
                ticks: {
                  callback: function(value) {
                    return value + '%';
                  }
                }
              },
              x: {
                ticks: {
                  maxRotation: 45,
                  minRotation: 45
                }
              }
            }
          }
        });
      } catch (error) {
        console.error('图表初始化失败:', error);
      }
    },
    
    // 生成图表颜色数组
    generateChartColors(count) {
      const baseColors = [
        'rgba(54, 162, 235, 0.7)',
        'rgba(75, 192, 192, 0.7)',
        'rgba(255, 206, 86, 0.7)',
        'rgba(255, 99, 132, 0.7)',
        'rgba(153, 102, 255, 0.7)',
        'rgba(255, 159, 64, 0.7)',
        'rgba(199, 199, 199, 0.7)',
        'rgba(83, 102, 255, 0.7)',
        'rgba(255, 102, 102, 0.7)',
        'rgba(102, 255, 102, 0.7)'
      ];
      
      const colors = [];
      for (let i = 0; i < count; i++) {
        colors.push(baseColors[i % baseColors.length]);
      }
      return colors;
    },
    
    // 调整颜色亮度
    adjustColorBrightness(color, amount) {
      const rgbaMatch = color.match(/rgba\((\d+),\s*(\d+),\s*(\d+),\s*(\d+(?:\.\d+)?)\)/);
      if (rgbaMatch) {
        const r = Math.max(0, Math.min(255, parseInt(rgbaMatch[1]) + amount));
        const g = Math.max(0, Math.min(255, parseInt(rgbaMatch[2]) + amount));
        const b = Math.max(0, Math.min(255, parseInt(rgbaMatch[3]) + amount));
        const a = rgbaMatch[4];
        return `rgba(${r}, ${g}, ${b}, ${a})`;
      }
      return color;
    },

    async resetAllProgress() {
      // 确认对话框
      const confirmed = confirm('⚠️ 确定要重置所有小组的进度吗？\n\n此操作将清除所有学生的关卡进度，且不可恢复！')
      if (!confirmed) {
        return
      }

      this.resetting = true
      
      try {
        console.log('开始重置所有小组进度...')
        
        // 调用后端重置进度接口
        const response = await deleteAllUserProgress()
        console.log('重置进度响应:', response)
        
        if (response.code === 0 && response.data) {
          console.log('重置进度成功')
          // 显示成功提示
          alert('✅ 所有小组进度已成功重置！')
          
          // 重新获取进度数据
          await this.fetchProgress()
          // 重置侧边栏状态
          if (this.showDrawer) {
            this.closeDrawer();
          }
        } else {
          console.warn('重置进度接口调用失败:', response.message)
          alert(`❌ 重置失败: ${response.message || '未知错误'}`)
        }
      } catch (error) {
        console.error('调用重置进度接口失败:', error)
        const errorMessage = error.response?.data?.message || error.message || '网络错误'
        alert(`❌ 重置失败: ${errorMessage}`)
      } finally {
        this.resetting = false
      }
    }
  },
  
  // 计算属性
  computed: {
  },
  
  // 监听属性
  watch: {
    // 监听侧边栏状态变化，更新body样式
    showDrawer(newValue) {
      if (newValue) {
        document.body.style.overflow = 'hidden';
      } else {
        document.body.style.overflow = '';
      }
    },
    
    // 监听数据变化，更新图表
    progressData: {
      handler() {
        if (this.progressData.length > 0) {
          this.initChart();
        }
      },
      deep: true
    }
  }
}
</script>

<style scoped>
/* 全局样式重置，确保页面可以撑满整个视口 */
:deep(html),
:deep(body) {
  height: 100%;
  margin: 0;
  padding: 0;
}

:deep(#app) {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
/* 所有小组答案信息卡片样式 */
.all-groups-answers-section {
  margin-bottom: 0;
  /* 删除底部边距，让表格撑满底部 */
  min-height: 600px; /* 确保至少撑满剩余空间 */
  flex: 1; /* 使用flex布局撑满剩余空间 */
  display: flex;
  flex-direction: column;
}

.all-groups-answers-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  height: 100%; /* 让卡片撑满容器 */
  flex: 1; /* 在弹性容器中自动伸展 */
  display: flex;
  flex-direction: column;
}

.card-header {
  margin-bottom: 20px;
}

.card-header h3 {
  font-size: 1.4rem;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
}

.card-subtitle {
  font-size: 0.9rem;
  color: #666;
  margin: 0;
}

/* 分隔线样式 */
.section-divider {
  height: 2px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  margin: 30px 0;
  border-radius: 1px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.answers-overview-content {
  margin-top: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* 固定表格表头 - 增强版 */
.answers-table-container {
  /* 确保容器是sticky定位的参考元素 */
  position: relative;
  overflow: auto;
  height: 600px;
  /* 固定高度可以确保滚动行为一致 */
  max-height: 80vh;
  border-radius: 8px;
  border: 1px solid #e4eaf2;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  /* 确保容器内的z-index堆叠上下文正确 */
  z-index: 1;
}

.answers-table-container::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.answers-table-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.answers-table-container::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

.answers-table-container::-webkit-scrollbar-thumb:hover {
  background: #555;
}

.answers-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  background: white;
  min-width: 1200px;
}

/* 直接对th元素应用sticky，而不是thead */
.answers-table th {
  position: sticky;
  top: 0;
  z-index: 2000;
  padding: 12px 8px;
  text-align: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-weight: 600;
  font-size: 0.85rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  min-width: 100px;
  box-sizing: border-box;
  border-bottom: none;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  /* 确保表头不会被其他元素覆盖 */
  isolation: isolate;
  /* 确保sticky定位在所有浏览器中都能正常工作 */
  display: table-cell;
  /* 添加边框以增强视觉效果 */
  border-right: 1px solid rgba(255, 255, 255, 0.2);
}

/* 修复表格可能的边框问题 */
.answers-table {
  border-collapse: separate;
  border-spacing: 0;
  /* 确保表格不会因为边框合并影响sticky定位 */
}

/* 确保tbody不会影响表头的sticky效果 */
.answers-table tbody {
  position: relative;
  z-index: 1;
}

/* 答案项表头列固定 - 确保与其他表头样式协调 */
.answers-table th:first-child {
  min-width: 150px; /* 答案项列宽度 */
  position: sticky;
  top: 0;
  left: 0;
  /* 提高z-index确保首列表头始终可见且在所有表头之上 */
  z-index: 1100;
  text-align: left;
  /* 添加右侧边框以区分相邻列 */
  border-right: 2px solid #5a4a94;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

/* 优化单元格样式 */
.answers-table td {
  padding: 8px 6px;
  border-bottom: 1px solid #f0f0f0;
  font-size: 0.8rem;
  color: #333;
  vertical-align: middle;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100px;
  transition: background-color 0.2s ease;
}

/* 答案项列固定 */
.answers-table td:first-child {
  position: sticky;
  left: 0;
  background: white;
  /* 提高z-index确保首列数据始终可见但不遮挡表头 */
  z-index: 50;
  font-weight: 600;
  color: #764ba2;
  text-align: left;
  padding-left: 12px;
  min-width: 150px;
  /* 添加右侧边框以区分相邻列 */
  border-right: 1px solid #e0e0e0;
}

.answers-table tr:nth-child(even) td:first-child {
  background: #fafbfc;
}

.answers-table tr:hover td:first-child {
  background: #f5f7fa;
  z-index: 6;
}

/* 答案类型单元格样式 */
.answer-type {
  font-weight: 600;
  color: #764ba2;
  background: #f8f9ff !important;
}

/* 进度列特殊样式 */
.answers-table td:last-child {
  min-width: 80px;
}

/* 空单元格样式优化 */
.answers-table td:empty::after,
.answers-table td:contains('暂未填写') {
  color: #999;
  font-style: italic;
}

/* 移除表格行悬停效果 */

/* 禁用奇偶行交替背景色，使用关卡特定颜色 */

/* 关卡特定的背景颜色 */
.answers-table tr.level-1 {
  background-color: #f8f0ff; /* 浅紫色 */
}

.answers-table tr.level-2 {
  background-color: #e6f7ff; /* 浅蓝色 */
}

.answers-table tr.level-3 {
  background-color: #e6ffe6; /* 浅绿色 */
}

.answers-table tr.level-4 {
  background-color: #fff7e6; /* 浅黄色 */
}

.answers-table tr.level-5 {
  background-color: #ffe6e6; /* 浅粉色 */
}

.answers-table tr.level-6 {
  background-color: #f0f0f0; /* 浅灰色 */
}

/* 移除所有悬停效果，保持原有关卡颜色不变 */
.answers-table tr:hover {
  background-color: inherit;
}

/* 展开/收起功能样式 */
.answer-cell {
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
  display: inline-block;
  max-width: 100%;
  padding: 4px 8px;
  border-radius: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.answer-cell:hover {
  color: #764ba2;
  background-color: rgba(118, 75, 162, 0.1);
  padding-bottom: 4px;
}

/* 确保展开后文本可以正确显示多行 */
.answer-cell.expanded {
  white-space: normal;
  max-width: none;
  word-wrap: break-word;
  padding-bottom: 4px;
  border: 1px solid rgba(118, 75, 162, 0.2);
  background-color: rgba(118, 75, 162, 0.05);
}

.answer-cell.expanded:hover {
  background-color: rgba(118, 75, 162, 0.15);
}

/* 调整单元格样式以适应展开功能 */
.answers-table td {
  vertical-align: top;
  padding: 8px 6px;
  min-height: 40px;
}

/* 确保第一列（答案类型）在展开时保持固定宽度 */
.answers-table td:first-child {
  vertical-align: middle;
}

.group-cell {
  font-weight: 600;
  color: #764ba2;
}

/* 迷你进度条样式优化 */
.mini-progress {
  display: flex;
  flex-direction: column;
  gap: 4px;
  align-items: center;
}

.mini-progress-bar {
  height: 8px;
  width: 100%;
  max-width: 80px;
  background-color: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.mini-progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  transition: width 0.5s ease;
}

.mini-progress-text {
  font-size: 0.75rem;
  font-weight: 600;
  color: #764ba2;
  text-align: center;
}

/* 响应式设计 - 表格部分 */
@media (max-width: 1200px) {
  .all-groups-answers-card {
    padding: 15px;
  }
  
  .card-header h3 {
    font-size: 1.2rem;
  }
  
  .answers-table th {
    font-size: 0.8rem;
    padding: 10px 6px;
  }
  
  .answers-table td {
    font-size: 0.75rem;
    padding: 6px 4px;
    max-width: 100px;
  }
}

@media (max-width: 768px) {
  .all-groups-answers-card {
    padding: 15px;
  }
  
  .answers-table th,
  .answers-table td {
    padding: 8px;
    font-size: 0.8rem;
  }
  
  .card-header h3 {
    font-size: 1.3rem;
  }
}

.progress-container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.progress-header {
  text-align: center;
  margin-bottom: 3rem;
  color: white;
  position: relative;
}

.refresh-indicator {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.1);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  backdrop-filter: blur(10px);
}

.refresh-dot {
  width: 8px;
  height: 8px;
  background: #52c41a;
  border-radius: 50%;
  animation: pulse 1.5s ease-in-out infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.7;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

.header-actions {
  position: absolute;
  top: -10px;
  right: 0;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.reset-btn {
  background: rgba(255, 87, 34, 0.9);
  color: white;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.9rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  box-shadow: 0 2px 8px rgba(255, 87, 34, 0.3);
}

.reset-btn:hover:not(:disabled) {
  background: rgba(255, 87, 34, 1);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 87, 34, 0.4);
}

.reset-btn:disabled {
  background: rgba(150, 150, 150, 0.6);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.nav-links {
  margin-bottom: 1rem;
  text-align: left;
  display: flex;
  gap: 1rem;
  align-items: center;
}

.nav-link {
  color: white;
  text-decoration: none;
  padding: 0.5rem 1rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
  display: inline-block;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: white;
}

.logout-btn {
  background: rgba(220, 53, 69, 0.2);
  color: white;
  border: 1px solid rgba(220, 53, 69, 0.5);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.9rem;
}

.logout-btn:hover {
  background: rgba(220, 53, 69, 0.3);
  border-color: #dc3545;
  transform: translateY(-1px);
}

.progress-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.progress-subtitle {
  font-size: 1.1rem;
  opacity: 0.9;
  font-weight: 300;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  color: white;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 4px solid rgba(255, 255, 255, 0.3);
  border-top: 4px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 仪表盘容器样式 */
.dashboard-container {
  background: linear-gradient(135deg, #f5f7fa 0%, #e0e6ec 50%, #d8e0e6 100%);
  min-height: 100vh;
  color: #333;
  padding: 2rem 1rem;
}

/* 顶部导航栏样式 */
.dashboard-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
  padding: 1.5rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 20px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  margin-bottom: 2rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
}

/* 标题区域样式 */
.title-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  width: 100%;
}

/* 标题装饰元素 */
.title-decoration {
  flex: 1;
  height: 3px;
  background: linear-gradient(90deg, transparent, #4dabf7, transparent);
  border-radius: 3px;
}

.title-decoration.left {
  max-width: 150px;
}

.title-decoration.right {
  max-width: 150px;
}

/* 标题样式 */
.dashboard-title {
  font-size: 2.5rem;
  font-weight: 700;
  text-align: center;
  margin: 0;
  background: linear-gradient(135deg, #4dabf7, #74b9ff, #a2d2ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  letter-spacing: 0.5px;
}

/* 操作区域样式 */
.header-actions {
  display: flex;
  align-items: center;
  gap: 1rem;
}

/* 图表容器美化 */
.chart-container {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 15px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.chart-container h3 {
  text-align: center;
  margin-bottom: 1.5rem;
  font-size: 1.5rem;
  color: #4dabf7;
  font-weight: 600;
}

/* 小组区域美化 */
.groups-section {
  margin-top: 2rem;
}

.groups-section::before {
  content: "";
  display: block;
  height: 2px;
  background: linear-gradient(90deg, transparent, rgba(0, 0, 0, 0.1), transparent);
  margin-bottom: 2rem;
  border-radius: 1px;
}

/* 小组卡片提示样式 */
.group-hint {
  text-align: center;
  font-size: 0.85rem;
  color: #6c757d;
  margin-top: 0.75rem;
  padding: 0.5rem;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.group-card:hover .group-hint {
  color: #007bff;
  background: rgba(0, 123, 255, 0.05);
  transform: translateY(-2px);
}


.error-message {
  text-align: center;
  color: white;
  padding: 2rem;
}

.retry-btn {
  background: rgba(255, 255, 255, 0.2);
  border: 2px solid white;
  color: white;
  padding: 0.75rem 1.5rem;
  border-radius: 25px;
  cursor: pointer;
  font-size: 1rem;
  margin-top: 1rem;
  transition: all 0.3s ease;
}

.retry-btn:hover {
  background: white;
  color: #667eea;
}

.progress-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
  margin-bottom: 2rem;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
}

/* 小组区域样式 */
.groups-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

/* 小组网格布局 */
.groups-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 1.5rem;
  justify-content: center;
}

/* 小组卡片样式 */
.group-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  position: relative;
  min-height: 160px;
  cursor: pointer;
}

.group-card:hover {
  transform: translateY(-4px) scale(1.01);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.group-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #667eea, #764ba2, #f093fb);
  background-size: 200% 100%;
  animation: gradient-shift 3s ease-in-out infinite;
}

.group-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  position: relative;
}

.group-name {
  margin: 0;
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
}

.group-progress-bar {
  width: calc(100% - 2rem);
  margin: 0 auto 1rem;
  height: 8px;
  background: #ecf0f1;
  border-radius: 4px;
  overflow: hidden;
}

.group-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 1rem 1rem;
}

.level-info {
  font-size: 0.9rem;
  font-weight: 500;
  color: #667eea;
}

.progress-percentage {
  font-size: 1.1rem;
  font-weight: 700;
  color: #27ae60;
}

.progress-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  position: relative;
  min-height: 160px;
}

.progress-card:hover {
  transform: translateY(-4px) scale(1.01);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.progress-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #667eea, #764ba2, #f093fb);
  background-size: 200% 100%;
  animation: gradient-shift 3s ease-in-out infinite;
}

@keyframes gradient-shift {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 1rem 1rem 0.75rem;
  position: relative;
}

.header-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 0.5rem;
}

.compact-progress {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.8);
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  border: 1px solid rgba(102, 126, 234, 0.2);
}

.compact-level {
  font-size: 0.75rem;
  font-weight: 600;
  color: #667eea;
  min-width: 30px;
}

.compact-bar {
  width: 60px;
  height: 4px;
  background: #ecf0f1;
  border-radius: 2px;
  overflow: hidden;
}

.compact-fill {
  height: 100%;
  background: linear-gradient(90deg, #667eea, #764ba2);
  border-radius: 2px;
  transition: width 0.3s ease;
}

.compact-percent {
  font-size: 0.75rem;
  font-weight: 600;
  color: #27ae60;
  min-width: 35px;
  text-align: right;
}

.group-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.group-name {
  font-size: 1.1rem;
  font-weight: 700;
  color: #2c3e50;
  margin: 0;
  line-height: 1.2;
}

.group-number {
  font-size: 0.8rem;
  color: #7f8c8d;
  font-weight: 500;
  background: rgba(102, 126, 234, 0.1);
  padding: 0.2rem 0.6rem;
  border-radius: 8px;
  display: inline-block;
}

.card-content {
  padding: 0 1rem 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.blueprint-section {
  display: flex;
  flex-direction: column;
  gap: 0.625rem;
}

.blueprint-item {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.8), rgba(255, 255, 255, 0.6));
  border-radius: 8px;
  border: 1px solid rgba(102, 126, 234, 0.15);
  overflow: hidden;
  transition: all 0.3s ease;
}

.blueprint-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
  border-color: rgba(102, 126, 234, 0.3);
}

.blueprint-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0.75rem;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1), rgba(118, 75, 162, 0.05));
  border-bottom: 1px solid rgba(102, 126, 234, 0.1);
}

.blueprint-icon {
  font-size: 0.9rem;
  width: 18px;
  text-align: center;
}

.blueprint-title {
  font-size: 0.8rem;
  font-weight: 600;
  color: #667eea;
  line-height: 1.2;
}

.blueprint-content {
  padding: 0.5rem 0.75rem;
  font-size: 0.75rem;
  color: #2c3e50;
  line-height: 1.4;
  background: rgba(255, 255, 255, 0.5);
  min-height: 2.5rem;
  display: flex;
  align-items: center;
}

.progress-bar-container {
  margin-bottom: 0.75rem;
}

.progress-bar {
  width: 100%;
  height: 6px;
  background: #ecf0f1;
  border-radius: 3px;
  overflow: hidden;
  margin-bottom: 0.25rem;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #667eea, #764ba2);
  border-radius: 3px;
  transition: width 0.3s ease;
}

.progress-text {
  text-align: center;
  font-size: 0.8rem;
  color: #7f8c8d;
  font-weight: 500;
}

.progress-summary {
  margin-bottom: 0.75rem;
}

.level-display {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.25rem;
  font-size: 0.9rem;
  font-weight: 600;
  color: #2c3e50;
}

.current-level {
  color: #667eea;
}

.total-levels {
  color: #7f8c8d;
}

.progress-percentage {
  color: #27ae60;
  font-weight: 700;
}

.card-footer {
  display: flex;
  justify-content: center;
}

.status-badge {
  padding: 0.35rem 0.75rem;
  border-radius: 15px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.status-starting {
  background: #fff3cd;
  color: #856404;
}

.status-not-logged {
  background: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}

.status-halfway {
  background: #cce5ff;
  color: #004085;
}

.status-completed {
  background: #d4edda;
  color: #155724;
}

.status-final-level {
  background: #fff3cd;
  color: #856404;
  border: 2px solid #ffc107;
  animation: glow 2s ease-in-out infinite alternate;
}

@keyframes glow {
  from {
    box-shadow: 0 0 5px rgba(255, 193, 7, 0.5);
  }
  to {
    box-shadow: 0 0 15px rgba(255, 193, 7, 0.8);
  }
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .progress-grid {
    grid-template-columns: repeat(3, 1fr);
    gap: 0.875rem;
    max-width: 1000px;
  }
}

@media (max-width: 768px) {
  .progress-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 0.75rem;
    padding: 0 0.5rem;
    max-width: 100%;
  }
  
  .progress-card {
    min-height: 140px;
  }
  
  .card-header {
    padding: 0.875rem 0.875rem 0.625rem;
  }
  
  .card-content {
    padding: 0 0.875rem 0.875rem;
    gap: 0.625rem;
  }
  
  .current-level {
    font-size: 1.2rem;
  }
  
  .progress-percentage {
    font-size: 1.1rem;
  }
}

@media (max-width: 480px) {
  .progress-container {
    padding: 1rem 0.25rem;
  }
  
  .progress-grid {
    grid-template-columns: 1fr;
    gap: 0.625rem;
    max-width: 100%;
  }
  
  .progress-card {
    min-height: 120px;
    border-radius: 8px;
  }
  
  .card-header {
    padding: 0.75rem 0.75rem 0.5rem;
  }
  
  .card-content {
    padding: 0 0.75rem 0.75rem;
    gap: 0.5rem;
  }
  
  .group-name {
    font-size: 1rem;
  }
  
  .current-level {
    font-size: 1.1rem;
  }
  
  .progress-percentage {
    font-size: 1rem;
  }
  
  .progress-summary {
    padding: 0.625rem;
  }
  
  .stat-item {
    padding: 0.375rem;
  }
}

/* 侧边栏抽屉样式 */
.drawer {
  position: fixed;
  top: 0;
  right: 0;
  width: 80%;
  max-width: 600px;
  height: 100%;
  background-color: #fcfdfe;
  box-shadow: -8px 0 24px rgba(0, 0, 0, 0.15);
  z-index: 1001;
  transform: translateX(100%);
  transition: transform 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  border-left: 1px solid #e9ecef;
}

.drawer.drawer-open {
  transform: translateX(0);
}

.drawer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.25rem 1.5rem;
  border-bottom: 1px solid #e1e8ed;
  background: linear-gradient(135deg, #5a67d8 0%, #7c3aed 100%);
  color: white;
  position: sticky;
  top: 0;
  z-index: 10;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.drawer-header h3 {
  margin: 0;
  font-size: 1.35rem;
  font-weight: 600;
}

.close-drawer {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: white;
  padding: 0.5rem;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.close-drawer:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: scale(1.1);
}

.drawer-content {
  padding: 0;
  flex: 1;
  overflow-y: auto;
}

.no-selection {
  text-align: center;
  padding: 3rem 1.5rem;
  color: #666;
  font-size: 1.1rem;
}

.group-details {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.tabs-nav {
  display: flex;
  border-bottom: 2px solid #f0f2f5;
  background: #ffffff;
  position: sticky;
  top: 68px;
  z-index: 5;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.02);
}

.tab-button {
  flex: 1;
  padding: 1rem 0.75rem;
  border: none;
  background: none;
  cursor: pointer;
  border-bottom: 3px solid transparent;
  transition: all 0.3s ease;
  white-space: nowrap;
  color: #4a5568;
  font-weight: 600;
  font-size: 1rem;
  position: relative;
}

.tab-button:hover {
  background-color: rgba(90, 103, 216, 0.05);
  color: #5a67d8;
}

.tab-button.active {
  color: #5a67d8;
  font-weight: 700;
  background: linear-gradient(180deg, rgba(90, 103, 216, 0.1) 0%, transparent 100%);
}

.tab-button.active::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 100%;
  height: 3px;
  background: linear-gradient(90deg, #5a67d8, #7c3aed);
  border-radius: 3px 3px 0 0;
}

.tab-content {
  flex: 1;
  padding: 1.5rem;
  animation: fadeIn 0.3s ease;
  background-color: #fafbfc;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.tab-overview h4,
.tab-levels h4,
.tab-answers h4 {
  margin-top: 0;
  margin-bottom: 1.5rem;
  color: #2c3e50;
  font-size: 1.2rem;
  font-weight: 600;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #f0f2f5;
}

.info-item {
  margin-bottom: 1.25rem;
  padding-bottom: 1.25rem;
  border-bottom: 1px solid #f0f2f5;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.info-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.info-label {
  font-weight: 600;
  color: #666;
  font-size: 0.9rem;
  margin-right: 0.5rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-value {
  color: #333;
  font-size: 1rem;
  line-height: 1.4;
  padding-left: 0.25rem;
}

.status-text {
  display: inline-block;
  padding: 0.35rem 0.75rem;
  border-radius: 15px;
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.status-text.starting {
  background: #fff3cd;
  color: #856404;
}

.status-text.halfway {
  background: #cce5ff;
  color: #004085;
}

.status-text.completed {
  background: #d4edda;
  color: #155724;
}

.status-text.final-level {
  background: #fff3cd;
  color: #856404;
  border: 2px solid #ffc107;
  animation: glow 2s ease-in-out infinite alternate;
}

/* 关卡详情样式 */
.levels-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.level-card {
  background: #f8f9fa;
  border-radius: 12px;
  padding: 1.25rem;
  border: 1px solid #e9ecef;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.level-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border-color: #667eea;
}

.level-card.completed {
  background: linear-gradient(135deg, #f8f9fa 0%, #e8f5e8 100%);
  border-color: #28a745;
}

.level-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.75rem;
}

.level-card-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.level-card-title::before {
  content: '';
  display: inline-block;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #667eea;
}

.level-card.completed .level-card-title::before {
  background: #28a745;
}

.level-status-badge {
  font-size: 0.8rem;
  font-weight: 600;
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  text-transform: uppercase;
}

.level-status-badge.completed {
  background: #d4edda;
  color: #155724;
}

.level-status-badge.in-progress {
  background: #cce5ff;
  color: #004085;
}

.level-status-badge.not-started {
  background: #f8d7da;
  color: #721c24;
}

/* 答案详情样式 */
.answers-container {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.level-section {
  background: #fff;
  border-radius: 16px;
  padding: 1.5rem;
  border: 1px solid #e9ecef;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.level-section:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
}

.level-section h5 {
  margin-top: 0;
  margin-bottom: 1.25rem;
  color: #2d3748;
  font-size: 1.15rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding-bottom: 0.75rem;
  border-bottom: 2px solid #f0f2f5;
}

.level-section h5::before {
  content: '';
  display: inline-block;
  width: 4px;
  height: 24px;
  background: linear-gradient(180deg, #5a67d8, #7c3aed);
  border-radius: 2px;
}

.answers-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1.25rem;
}

.answer-field {
  background: #f8fafc;
  border-radius: 10px;
  padding: 1.25rem;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.answer-field:hover {
  border-color: #5a67d8;
  background: #f1f5f9;
  box-shadow: 0 2px 8px rgba(90, 103, 216, 0.05);
}

.answer-field::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 3px;
  height: 100%;
  background: linear-gradient(180deg, #5a67d8, #7c3aed);
}

.answer-field-label {
  font-weight: 600;
  color: #4a5568;
  font-size: 0.9rem;
  margin-bottom: 0.75rem;
  letter-spacing: 0.5px;
  display: block;
  padding-left: 0.5rem;
}

.answer-field-value {
  color: #1a202c;
  font-size: 1rem;
  line-height: 1.6;
  word-break: break-word;
  padding-left: 0.5rem;
  font-family: 'SF Pro Text', 'Roboto', sans-serif;
}

.no-answers {
  text-align: center;
  padding: 2rem 1rem;
  color: #666;
  font-size: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
  border: 2px dashed #dee2e6;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
  opacity: 0;
  visibility: hidden;
  transition: opacity 0.3s ease, visibility 0.3s ease;
}

.overlay.overlay-active {
  opacity: 1;
  visibility: visible;
}

/* 概览区域样式 */
.overview-section {
  display: flex;
  justify-content: center;
  margin-bottom: 1.5rem;
}

/* 图表容器样式增强 */
.chart-container {
  background-color: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  height: 350px;
  position: relative;
  width: 100%;
  max-width: 800px;
}

.chart-container h3 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #2c3e50;
  font-size: 18px;
  font-weight: 600;
}

/* 滚动条样式 */
.drawer::-webkit-scrollbar,
.tabs-nav::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.drawer::-webkit-scrollbar-track,
.tabs-nav::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.drawer::-webkit-scrollbar-thumb,
.tabs-nav::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.drawer::-webkit-scrollbar-thumb:hover,
.tabs-nav::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 响应式设计调整 */
@media (max-width: 768px) {
  .drawer {
    width: 95%;
    max-width: none;
  }
  
  .drawer-header {
    padding: 1rem;
  }
  
  .drawer-header h3 {
    font-size: 1.2rem;
  }
  
  .tab-content {
    padding: 1rem;
  }
  
  .level-card,
  .level-section {
    padding: 1rem;
  }
  
  .tabs-nav {
    top: 58px;
  }
  
  .tab-button {
    padding: 0.75rem 0.5rem;
    font-size: 0.85rem;
  }
}

@media (max-width: 480px) {
  .drawer {
    width: 100%;
  }
  
  .drawer-header h3 {
    font-size: 1.1rem;
  }
  
  .tab-content {
    padding: 0.75rem;
  }
  
  .info-label {
    font-size: 0.8rem;
  }
  
  .info-value {
    font-size: 0.9rem;
  }
  
  .answer-field {
    padding: 0.75rem;
  }
}
</style>