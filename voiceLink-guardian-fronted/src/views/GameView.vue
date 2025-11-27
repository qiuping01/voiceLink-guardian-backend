<template>
  <div class="game-container">
    <!-- 主内容区域 -->
    <div class="game-content">
      <!-- 关卡标题和信息 -->
      <div class="game-header">
        <div class="task-badge">5级任务</div>
        <h1 class="game-title">声联守护·终极挑战</h1>
        <p class="game-subtitle">AIoT守护者训练营 - 为空巢老人构建紧急呼叫系统</p>
      </div>

      <!-- 团队信息 -->
      <div class="team-info">
        <div class="team-member">小队名称: {{ userInfo.groupName }}</div>
        <div class="team-member">指挥官: {{ userInfo.commander }}</div>
        <button class="exit-button" @click="handleLogout">退出登录</button>
      </div>
    </div>

    <!-- 进度指示器 -->
    <div class="progress-container">
      <div class="progress-track">
        <div 
          v-for="(level, index) in levels" 
          :key="index"
          class="progress-step"
          :class="{ 
            'active': currentLevel === index, 
            'completed': currentLevel > index 
          }"
        >
          <div class="progress-dot"></div>
          <div class="progress-line" v-if="index < levels.length - 1"></div>
        </div>
      </div>
      
      <div class="level-labels">
        <div 
          v-for="(level, index) in levels" 
          :key="index"
          class="level-label"
          :class="{ 
            'active': currentLevel === index, 
            'completed': currentLevel > index 
          }"
        >
          {{ level.name }}
        </div>
      </div>
    </div>

    <!-- 关卡内容区域 -->
    <div class="level-content">
      <!-- 当前关卡内容 -->
      <div v-if="currentLevel === 0" class="level-0 mission-card">
        <h2>第一关：情境与问题——为何需要"声联守护"</h2>
        <p><strong>任务目标:</strong> 理解项目的意义与价值，明确技术选型的理由。</p>
        
        <div class="mission-objective">
          <h3>行动指南:</h3>
          <p>1. 观看情境视频，思考：视频中老人面临的最大困境是什么？</p>
          <p>2. 与小队成员交流你的看法。</p>
        </div>
        
        <div class="question-container">
          <div class="question-text">思考与讨论：视频中老人面临的最大困境是什么？</div>
          <div class="options-container">
            <div class="option" id="option-a">
              <input type="radio" id="choice-a" name="question1" value="A" @change="updateFirstDilemmaText('A', '摔倒本身')">
              <label for="choice-a">A. 摔倒本身</label>
            </div>
            <div class="option" id="option-b">
              <input type="radio" id="choice-b" name="question1" value="B" @change="updateFirstDilemmaText('B', '摔倒后求救信号无法有效传递')">
              <label for="choice-b">B. 摔倒后求救信号无法有效传递</label>
            </div>
          </div>
        </div>
        
        <div class="mission-objective">
          <h3>我们的使命:</h3>
          <p>构建一个能"听懂"老人呼救，并能立即向云端发送警报的智能系统。</p>
        </div>
        
        <!-- 过关确认 (初始隐藏) -->
        <div class="level-completion" id="level-1-completion" v-if="showLevelCompletion[0]">
          <div class="completion-icon">🎯</div>
          <div class="completion-title">第一关完成！</div>
          <div class="completion-message">您已成功理解项目意义，可以进入下一关</div>
          <div class="completion-badge-preview">
            <div>
              <div class="badge-icon">🔍</div>
              <div>洞察者</div>
            </div>
          </div>
          <p>已解锁勋章：洞察者</p>
        </div>
        
        <div class="level-actions">
          <button @click="prevLevel" class="prev-button" :disabled="currentLevel === 0">上一关</button>
          <button @click="nextLevel" class="next-button">下一关</button>
        </div>
      </div>

      <div v-else-if="currentLevel === 1" class="level-1 mission-card">
        <h2>第二关：方案设计——空巢老人智能安全系统设计方案</h2>
        <p><strong>任务目标:</strong> 为你的系统选定场景、设计功能，并规划技术路线。</p>
        
        <div class="mission-objective">
          <h3>行动指南:</h3>
          <p>请同学们选定一个生活场景，深入分析空巢老人在此可能遇到的困难与风险，并设计出能通过语音识别技术提供帮助的智能解决方案。</p>
        </div>
        
        <!-- 嵌入house.html文件 -->
        <div class="embedded-content">
          <h4>参考场景图示:</h4>
          <iframe ref="visibleIframe" class="house-iframe" title="空巢老人住宅场景参考" frameborder="0"></iframe>
          <p class="alternative-link">模型如未正常显示则是在加载中，请稍等片刻...</p>
        </div>
        
        <!-- 空巢老人安全管理系统 -->
        <div class="elderly-safety-system">
          <div class="elderly-safety-header">
            <h3>系统设计蓝图:</h3>
          </div>
          <div class="scene-item">
            <span class="scene-label">我们小组重点关注的场景是:</span>
            <select class="input-field" id="scene-select">
              <option value="" selected disabled>-- 请选择场景 --</option>
              <option value="客厅">客厅</option>
              <option value="卧室">卧室</option>
              <option value="卫生间">卫生间</option>
              <option value="厨房">厨房</option>
            </select>
          </div>
          <div class="scene-item">
            <span class="scene-label">在该场景下，老人最可能遇到的风险是:</span>
            <input type="text" class="input-field" id="risk-input" placeholder="例如: 摔倒、突发疾病">
          </div>
          
          <h4 class="mt-4">物联网三层架构设计:</h4>
          <table class="design-table">
            <thead>
              <tr>
                <th>物联网三层架构</th>
                <th>环节</th>
                <th>我们的设计</th>
                <th>注意事项</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td rowspan="2">感知层（信息采集）</td>
                <td>老人在这种紧急情况下，最可能喊出的语音指令是:</td>
                <td>
                  <input type="text" class="input-field" placeholder="指令1" id="cmd1">
                  <input type="text" class="input-field" placeholder="指令2" id="cmd2">
                </td>
                <td>指令应简短、顺口、不易误触发。</td>
              </tr>
              <tr>
                <td>行空板收到求救信息后，发出什么语音安抚老人</td>
                <td>
                  <input type="text" class="input-field" placeholder="播放语音" id="response-speech">
                </td>
                <td>本地响应可以让老人确认系统已启动，给予安全感。</td>
              </tr>
              <tr>
                <td>网络层+应用层（信息传输与使用）</td>
                <td>发送什么信息给家人或云平台</td>
                <td><input type="text" class="input-field" placeholder="发送内容" id="cloud-data"></td>
                <td>数据要包含最关键的信息，让远方监护人能快速了解情况。</td>
              </tr>
            </tbody>
          </table>
          
          <div class="check-item mt-3 highlight-completion highlight-warning">
            <input type="checkbox" id="check2-1">
            <label for="check2-1">我们的设计蓝图已通过指挥官（老师）审核！</label>
          </div>
          </div>
        <div class="level-completion" id="level-2-completion" v-if="showLevelCompletion[1]">
          <div class="completion-icon">📐</div>
          <div class="completion-title">第二关完成！</div>
          <div class="completion-message">您已成功设计空巢老人安全系统方案</div>
          <div class="completion-badge-preview">
            <div>
              <div class="badge-icon">📐</div>
              <div>设计师</div>
            </div>
          </div>
          <p>已解锁勋章：设计师</p>
        </div>
        
        <div class="level-actions">
          <button @click="prevLevel" class="prev-button">上一关</button>
          <button @click="nextLevel" class="next-button">下一关</button>
        </div>
      </div>

      <div v-else-if="currentLevel === 2" class="level-2 mission-card">
        <h2>第三关：动手搭建——探究实施，联调系统</h2>
        <p><strong>任务目标:</strong> 编写程序实现语音识别功能，并将数据上传到云端。</p>
        
        <!-- 任务导航 -->
        <div class="task-navigation">
          <button class="task-nav-btn" :class="{ active: activeTask === 'task1' }" @click="activeTask = 'task1'">任务一：让行空板'学会听'</button>
          <button class="task-nav-btn" :class="{ active: activeTask === 'task2' }" :disabled="!task1Completed" @click="activeTask = 'task2'">任务二：实现数据上云</button>
        </div>
        
        <!-- 任务一内容 -->
        <div class="task-content" v-show="activeTask === 'task1'">
          <div class="task-card">
            <div class="task-header">
              <div class="task-title">任务一：编写程序，让行空板'学会听'</div>
              <div class="task-status">
                <span class="task-status-badge" :class="{ 'status-completed': task1Completed, 'status-inprogress': !task1Completed }">
                  {{ task1Completed ? '已完成' : '进行中' }}
                </span>
              </div>
            </div>
            <p>准确识别老人"救命啊"等求助指令，输出识别结果</p>
            
            <h4>行动指南:</h4>
            <div class="task-step">
              <p><strong>1.</strong> 连接行空板，在Mind+中打开"任务一"程序</p>
              <img src="https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511212312353.png" alt="连接行空板" class="guide-image">
            </div>
            <div class="task-step">
              <p><strong>2.</strong> 参照《实验手册》，修改代码中的唤醒词和识别指令</p>
              <img src="https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511250038814.png" alt="修改唤醒词和识别指令" class="guide-image">
            </div>
            <div class="task-step">
              <p><strong>3.</strong> 上传程序并测试！</p>
              <img src="https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511212312125.png" alt="上传程序并测试" class="guide-image">
            </div>
            
            <div class="check-item mt-3 highlight-task-completion">
              <input type="checkbox" id="check3-1" v-model="task1Completed" @change="handleTask1Completion">
              <label for="check3-1">我们成功让行空板"听懂"了指令！</label>
            </div>
            
            <!-- 任务一完成提示 -->
            <div v-if="task1Completed" class="task-unlock-message">
              <i class="fas fa-check-circle"></i> 恭喜！任务一已完成，现在可以开始任务二了！
            </div>
          </div>
        </div>
        
        <!-- 任务二内容 -->
        <div class="task-content" v-show="activeTask === 'task2'">
          <div class="task-card" :class="{ 'task-locked': !task1Completed }">
            <div v-if="!task1Completed" class="task-locked-overlay">请先完成任务一</div>
            <div class="task-header">
              <div class="task-title">任务二：实现行空板数据上云</div>
              <div class="task-status">
                <span class="task-status-badge" :class="{ 'status-completed': task2Completed, 'status-locked': !task1Completed }">
                  {{ task2Completed ? '已完成' : (!task1Completed ? '锁定中' : '进行中') }}
                </span>
              </div>
            </div>
            <p>将语音识别结果与传感器数据上传至Easy IoT平台，支持远程查看</p>
            
            <h4>行动指南:</h4>
            
            <div class="step-section">
              <div class="step-title">一、编写程序</div>
              <p>1. 在任务一的基础上，在左下角扩展"网络服务"界面，选择"MQTT"和"WI-FI"两项扩展</p>
              <img src="https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511212311639.png" alt="网络服务扩展" class="guide-image">
              <p>2. 点击"返回"回到主界面，左侧积木栏中增加了"掌控"和"网络服务"两个积木。其中，"网络服务"又包括了"MQTT"和"WI-FI"两项扩展，在左侧网络服务中将图下程序块拖动到编程界面，修改WIFI名称密码。</p>
              <img src="https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511212311094.png" alt="修改WIFI配置" class="guide-image">
            </div>
            
            <div class="step-section">
              <div class="step-title">二、搭建应用层平台</div>
              <p>1. 在学生素材文件夹，打开Easy Iot平台<br>
                 网址：<a href="https://iot.dfrobot.com.cn/index.html" target="_blank">https://iot.dfrobot.com.cn/index.html</a></p>
              
              <p>2. 点击工作间→登录账号→添加新的设备→需点击眼睛图标→显示相应信息</p>
              <img src="https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511250044571.png" alt="Easy IOT平台设置" class="guide-image">
              
              <p>3. 使用Easy Iot连接物联网，需要用到(图中标记的参数需要对应填入到mind+的MQTT初始化参数：<br>
                 iot_id、iot_pwd、话题（topic）</p>
              <img src="https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511212311078.png" alt="MQTT参数" class="guide-image">
              
              <p>4. 完整程序如图所示</p>
              <img src="https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511250047434.png" alt="完整程序" class="guide-image">
            </div>
            
            <div class="step-section">
              <div class="step-title">三、刷入程序</div>
              <p>Mind+点击上传到设备，确认是否上传成功</p>
            </div>
            
            <div class="step-section">
              <div class="step-title">四、测试实验结果</div>
              <p>1. 说"你好小行（小新）"，进入唤醒状态。说对应的识别词，喇叭播放对应的回应词。</p>
              <p>2. 进入Easy IOT平台→点击查看详情→点击刷新→点击查看，查看是否已经收到对应的回应词，收到即实验成功！</p>
              <img src="https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511212311010.png" alt="查看实验结果" class="guide-image">
            </div>
            
            <div class="check-item mt-3 highlight-task-completion task2">
              <input type="checkbox" id="check3-2" v-model="task2Completed" :disabled="!task1Completed" @change="handleTask2Completion">
              <label for="check3-2">我们成功将数据上传到云端！</label>
            </div>
            
            <!-- 任务二完成提示 -->
            <div v-if="task2Completed" class="task-unlock-message">
              <i class="fas fa-check-circle"></i> 恭喜！任务二已完成，第三关所有任务已完成！
            </div>
            
            <!-- 挑战任务选择 -->
            <div v-if="task2Completed" class="step-section">
              <div class="step-title">五、挑战任务（选做）</div>
              <p>恭喜完成基础任务！现在可以选择一个进阶挑战来提升你的技能：</p>
              
              <div class="scene-item">
                <span class="scene-label">选择挑战任务:</span>
                <select class="input-field" id="challenge-select" @change="showChallengeDescription">
                  <option value="" selected disabled>-- 请选择挑战任务 --</option>
                  <option value="challenge1">进阶挑战一（温湿度传感器火灾报警）</option>
                  <option value="challenge2">进阶挑战二（语音控制开关灯）</option>
                  <option value="challenge3">进阶挑战三（检测加速度值摔倒报警）</option>
                </select>
              </div>
              
              <!-- 挑战任务描述 -->
              <div id="challenge-descriptions" style="margin-top: 15px;">
                <!-- 挑战一描述 -->
                <div id="challenge1-desc" class="mission-objective" v-show="selectedChallenge === 'challenge1'">
                  <h4>进阶挑战一：温湿度传感器火灾报警</h4>
                  <p><strong>任务描述:</strong> 使用K10自带的温湿度传感器，当检测到异常高温时自动触发火灾报警。</p>
                  <p><strong>实现步骤:</strong></p>
                  <ul>
                    <li>1. 在行空板板载传感器中选择读取温度（摄氏度）</li>
                    <li>2. 设置温度阈值（如35°C）</li>
                    <li>3. 当温度超过阈值时，自动发送火灾警报到云端</li>
                    <li>4. 行空板同时播放语音警告</li>
                  </ul>
                  <p><strong>参考程序:</strong></p>
                  <img src="https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511250122908.png" alt="温湿度传感器火灾报警程序" class="guide-image">
                </div>
                
                <!-- 挑战二描述 -->
                <div id="challenge2-desc" class="mission-objective" v-show="selectedChallenge === 'challenge2'">
                  <h4>进阶挑战二：语音控制开关灯</h4>
                  <p><strong>任务描述:</strong> 实现通过语音指令控制LED灯的开关。</p>
                  <p><strong>实现步骤:</strong></p>
                  <ul>
                    <li>1. 点击左边行空板K10，找到LED控制模块，具体程序如下：</li>
                  </ul>
                  <img src="https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511250123350.png" alt="LED控制模块程序" class="guide-image">
                  <ul>
                    <li>2. 训练语音识别模块识别"开灯"、"关灯"指令</li>
                    <li>3. 根据语音指令控制LED灯状态</li>
                  </ul>
                </div>
                
                <!-- 挑战三描述 -->
                <div id="challenge3-desc" class="mission-objective" v-show="selectedChallenge === 'challenge3'">
                  <h4>进阶挑战三：检测加速度值摔倒报警</h4>
                  <p><strong>任务描述:</strong> 利用行空板内置的加速度传感器检测老人摔倒动作。</p>
                  <p><strong>实现步骤:</strong></p>
                  <ul>
                    <li>1. 启用行空板内置的加速度传感器</li>
                    <li>2. 设置加速度阈值算法检测摔倒动作</li>
                    <li>3. 检测到摔倒时自动发送紧急求助信息</li>
                    <li>4. 结合语音识别实现双重保险</li>
                  </ul>
                  <img src="https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511250124819.png" alt="加速度传感器程序" class="guide-image">
                </div>
              </div>
              
              <div class="check-item mt-3 highlight-completion highlight-purple">
                <input type="checkbox" id="check3-3" v-model="challengeCompleted" :disabled="!task2Completed">
                <label for="check3-3">我们完成了选做的挑战任务！</label>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 过关确认 -->
        <div v-if="task2Completed" class="level-completion" id="level-3-completion">
          <div class="completion-icon">🔧</div>
          <div class="completion-title">第三关完成！</div>
          <div class="completion-message">您已成功搭建语音识别系统并实现数据上云</div>
          <div class="completion-badge-preview">
            <div>
              <div class="badge-icon">👂</div>
              <div class="text-justify">听觉专家</div>
            </div>
          </div>
          <p class="text-justify">已解锁勋章：听觉专家 & 云端专家</p>
        </div>
        
        <div v-if="task2Completed" class="badges-container">
          <div class="badge" id="badge-3">
            <div>
              <div class="badge-icon">👂</div>
              <div class="text-justify">听觉专家</div>
            </div>
          </div>
          <div class="badge" id="badge-4">
            <div>
              <div class="badge-icon">☁️</div>
              <div class="text-justify">云端专家</div>
            </div>
          </div>
        </div>
        
        <div class="level-actions">
          <button @click="prevLevel" class="prev-button">上一关</button>
          <button @click="nextLevel" :disabled="!task1Completed || !task2Completed" class="next-button">下一关</button>
        </div>
      </div>

      <div v-else-if="currentLevel === 3" class="level-3 mission-card">
        <h2>第四关：成果展示——授勋大殿</h2>
        <p><strong>任务目标:</strong> 向所有守护者展示你们的成果与智慧。</p>
        
        <div class="mission-objective">
          <h3>行动指南:</h3>
          <p>1. 成果展示：现场演示你的"声联守护"系统。</p>
          <p>2. 心得分享：分享闯关过程中最难忘的一个"Boss"（难题）以及你们是如何击败它的。</p>
        </div>
        
        <div class="checklist">
          <h3>项目总结:</h3>
          <div class="scene-item">
            <span class="scene-label">我们小组选择的是:</span>
            <select class="input-field" id="final-scene">
              <option value="" selected disabled>-- 请选择场景 --</option>
              <option value="卧室">卧室</option>
              <option value="卫生间">卫生间</option>
              <option value="厨房">厨房</option>
              <option value="客厅">客厅</option>
            </select>
          </div>
          <div class="scene-item">
            <span class="scene-label">实现功能:</span>
            <input type="text" class="input-field" id="final-function" placeholder="功能描述">
          </div>
          <div class="scene-item">
            <span class="scene-label">我们还添加了功能:</span>
            <input type="text" class="input-field" id="final-extra" placeholder="额外功能">
          </div>
          
          <!-- 新增：最难Boss和解决方法 -->
          <div class="scene-item">
            <span class="scene-label">在闯关过程中，我们遇到最难的一个"Boss"（难题）是:</span>
            <input type="text" class="input-field" id="final-boss" placeholder="描述难题">
          </div>
          <div class="scene-item">
            <span class="scene-label">我们通过:</span>
            <input type="text" class="input-field" id="final-solution" placeholder="解决方法">
            <span>的方法最终击败了它！</span>
          </div>
          
          <div class="check-item mt-3 highlight-completion highlight-info">
            <input type="checkbox" id="check4-1" v-model="level4Completed" @change="handleLevel4Completion">
            <label for="check4-1">我们成功完成了系统演示！</label>
          </div>
        </div>
        
        <!-- 过关确认 -->
        <div class="level-completion" id="level-4-completion" v-if="showLevelCompletion[3]">
          <div class="completion-icon">🏆</div>
          <div class="completion-title">第四关完成！</div>
          <div class="completion-message">您已成功展示项目成果并分享心得</div>
          <div class="completion-badge-preview">
            <div>
              <div class="badge-icon">🏆</div>
              <div>展示专家</div>
            </div>
          </div>
          <p>已解锁勋章：展示专家</p>
        </div>
        
        <div class="badges-container" v-if="showLevelCompletion[3]">
          <div class="badge" id="badge-5">
            <div>
              <div class="badge-icon">🏆</div>
              <div>展示专家</div>
            </div>
          </div>
        </div>
        
        <div class="level-actions">
          <button @click="prevLevel" class="prev-button">上一关</button>
          <button @click="nextLevel" :disabled="!level4Completed" class="next-button">下一关</button>
        </div>
      </div>

      <div v-else-if="currentLevel === 4" class="level-4 mission-card">
        <h2>第五关：总结拓展</h2>
        <div class="completion-screen">
          <div class="final-badge">
            <div>
              <div style="font-size: 40px; margin-bottom: 10px;">🎓</div>
              <div>声联守护<br>终极勋章</div>
            </div>
          </div>
          
          <h2>第五关：<span id="final-team-name">{{ userInfo.groupName }}</span>的学习总结</h2>
          <p>完成学习评估与反思，继续前往教学评价环节！</p>
          
          <div class="achievement-stats">
            <div class="stat">
              <div class="stat-value">{{ badgesEarned }}</div>
              <div class="stat-label">勋章获得</div>
            </div>
            <div class="stat">
              <div class="stat-value">{{ tasksCompleted }}</div>
              <div class="stat-label">任务完成</div>
            </div>
            <div class="stat">
              <div class="stat-value">{{ challengesBeaten }}</div>
              <div class="stat-label">挑战击败</div>
            </div>
          </div>
          <div class="mission-card mt-4">
            <h3>学习效果评估与反思</h3>
            
            <!-- 问题1 -->
            <div class="feedback-item">
              <label class="feedback-label"><strong>1. 通过本项目，我对人工智能语音识别与物联网技术的理解有了明显加深。</strong></label>
              <div class="options-container">
                <div class="option">
                  <input type="radio" id="q1-5" name="question1" value="5" v-model="evaluationAnswers.question1">
                  <label for="q1-5">非常同意</label>
                </div>
                <div class="option">
                  <input type="radio" id="q1-4" name="question1" value="4" v-model="evaluationAnswers.question1">
                  <label for="q1-4">同意</label>
                </div>
                <div class="option">
                  <input type="radio" id="q1-3" name="question1" value="3" v-model="evaluationAnswers.question1">
                  <label for="q1-3">一般</label>
                </div>
                <div class="option">
                  <input type="radio" id="q1-2" name="question1" value="2" v-model="evaluationAnswers.question1">
                  <label for="q1-2">不同意</label>
                </div>
                <div class="option">
                  <input type="radio" id="q1-1" name="question1" value="1" v-model="evaluationAnswers.question1">
                  <label for="q1-1">非常不同意</label>
                </div>
              </div>
            </div>
            
            <!-- 问题2 -->
            <div class="feedback-item">
              <label class="feedback-label"><strong>2. 我能够清晰地描述"声联守护"系统中感知层、网络层、应用层各自的作用。</strong></label>
              <div class="options-container">
                <div class="option">
                  <input type="radio" id="q2-5" name="question2" value="5" v-model="evaluationAnswers.question2">
                  <label for="q2-5">非常同意</label>
                </div>
                <div class="option">
                  <input type="radio" id="q2-4" name="question2" value="4" v-model="evaluationAnswers.question2">
                  <label for="q2-4">同意</label>
                </div>
                <div class="option">
                  <input type="radio" id="q2-3" name="question2" value="3" v-model="evaluationAnswers.question2">
                  <label for="q2-3">一般</label>
                </div>
                <div class="option">
                  <input type="radio" id="q2-2" name="question2" value="2" v-model="evaluationAnswers.question2">
                  <label for="q2-2">不同意</label>
                </div>
                <div class="option">
                  <input type="radio" id="q2-1" name="question2" value="1" v-model="evaluationAnswers.question2">
                  <label for="q2-1">非常不同意</label>
                </div>
              </div>
            </div>
            
            <!-- 问题3 -->
            <div class="feedback-item">
              <label class="feedback-label"><strong>3. 在完成本项目后，我掌握或提升了以下哪些技能？（可多选）</strong></label>
              <div class="options-container horizontal-options">
                <div class="option">
                  <input type="checkbox" id="skill1" name="skills" value="mindplus" v-model="evaluationAnswers.skills">
                  <label for="skill1">使用Mind+进行图形化编程</label>
                </div>
                <div class="option">
                  <input type="checkbox" id="skill2" name="skills" value="speech" v-model="evaluationAnswers.skills">
                  <label for="skill2">配置和使用语音识别模块</label>
                </div>
                <div class="option">
                  <input type="checkbox" id="skill3" name="skills" value="mqtt" v-model="evaluationAnswers.skills">
                  <label for="skill3">理解并配置MQTT协议参数</label>
                </div>
                <div class="option">
                  <input type="checkbox" id="skill4" name="skills" value="iot" v-model="evaluationAnswers.skills">
                  <label for="skill4">将硬件设备数据上传至云平台(Easy IoT)</label>
                </div>
                <div class="option">
                  <input type="checkbox" id="skill5" name="skills" value="debug" v-model="evaluationAnswers.skills">
                  <label for="skill5">软硬件联调与故障排查</label>
                </div>
                <div class="option">
                  <input type="checkbox" id="skill6" name="skills" value="teamwork" v-model="evaluationAnswers.skills">
                  <label for="skill6">团队协作与项目规划</label>
                </div>
              </div>
            </div>
            <!-- 问题4 -->
            <div class="feedback-item">
              <label class="feedback-label"><strong>4. 你认为课程中哪个环节挑战最大？</strong></label>
              <div class="options-container">
                <div class="option">
                  <input type="radio" id="challenge1" name="challenge" value="design" v-model="evaluationAnswers.challenge">
                  <label for="challenge1">方案设计（将想法转化为具体方案）</label>
                </div>
                <div class="option">
                  <input type="radio" id="challenge2" name="challenge" value="speech" v-model="evaluationAnswers.challenge">
                  <label for="challenge2">语音识别程序编写与调试</label>
                </div>
                <div class="option">
                  <input type="radio" id="challenge3" name="challenge" value="mqtt" v-model="evaluationAnswers.challenge">
                  <label for="challenge3">MQTT参数配置与联网</label>
                </div>
                <div class="option">
                  <input type="radio" id="challenge4" name="challenge" value="debug" v-model="evaluationAnswers.challenge">
                  <label for="challenge4">软硬件联合调试</label>
                </div>
                <div class="option">
                  <input type="radio" id="challenge5" name="challenge" value="none" v-model="evaluationAnswers.challenge">
                  <label for="challenge5">没有特别困难的环节</label>
                </div>
              </div>
            </div>
            <!-- 问题5 -->
            <div class="feedback-item">
              <label class="feedback-label"><strong>5. 如果可以为"声联守护"系统增加一项新功能，你希望是什么？（例如：自动拨打求救电话、增加环境传感器(烟雾、煤气)、能用语音聊天陪伴老人等）</strong></label>
              <textarea class="feedback-input textarea-field" rows="3" v-model="evaluationAnswers.newFeature" placeholder="请描述你希望增加的新功能..."></textarea>
            </div>
            <!-- 问题6 -->
            <div class="feedback-item">
              <label class="feedback-label"><strong>6. 通过本节课，你对"技术服务于人"的理解有哪些新的收获？</strong></label>
              <textarea class="feedback-input textarea-field" rows="3" v-model="evaluationAnswers.valueInsight" placeholder="分享你的理解..."></textarea>
            </div>
            <div class="check-item mt-3 highlight-completion highlight-warning">
              <input type="checkbox" id="check5-1" v-model="level5Completed" @change="handleLevel5Completion">
              <label for="check5-1">我已完成学习效果评估与反思！</label>
            </div>
          </div>
          <div class="mission-card mt-4">
              <h4>指挥官寄语:</h4>
              <p style="font-style: italic;">
                技术是冰冷的，但你们赋予它的设计与关怀，让它拥有了温度。
                感谢每一位守护者的努力，你们不仅是技术的掌控者，更是温暖的传递者！
              </p>
            </div>
            
            <!-- 过关确认 -->
            <div class="level-completion" id="level-5-completion" v-if="showLevelCompletion[4]">
              <div class="completion-icon">🤔</div>
              <div class="completion-title">第五关完成！</div>
              <div class="completion-message">您已完成课程总结与反思</div>
              <div class="completion-badge-preview">
                <div>
                  <div class="badge-icon">🤔</div>
                  <div>反思者</div>
                </div>
              </div>
              <p>已解锁勋章：反思者</p>
            </div>
            
            <div class="badges-container" v-if="showLevelCompletion[4]">
              <div class="badge" id="badge-6">
                <div>
                  <div class="badge-icon">🤔</div>
                  <div>反思者</div>
                </div>
              </div>
            </div>
          </div>
        
        <!-- 过关确认 -->
        <div class="level-completion" id="level-5-completion" v-if="showLevelCompletion[4]">
          <div class="completion-icon">🎯</div>
          <div class="completion-title">第五关完成！</div>
          <div class="completion-message">您已成功完成全部学习评估和反思</div>
          <div class="completion-badge-preview">
            <div>
              <div class="badge-icon">🎓</div>
              <div>终极守护者</div>
            </div>
          </div>
          <p>已解锁勋章：终极守护者</p>
        </div>
        
        <div class="badges-container" v-if="showLevelCompletion[4]">
          <div class="badge" id="badge-6">
            <div>
              <div class="badge-icon">🎓</div>
              <div>终极守护者</div>
            </div>
          </div>
        </div>
        
        <div class="level-actions">
          <button @click="prevLevel" class="prev-button">上一关</button>
          <button @click="nextLevel" class="next-button">下一关</button>
        </div>
      </div>

      <div v-else-if="currentLevel === 5" class="level-6 mission-card">
        <div class="level-content" id="level-6">
          <div class="completion-screen">
            <div class="evaluation-badge">
              <div>
                <div style="font-size: 40px; margin-bottom: 5px;">⭐</div>
                <div>教学评价</div>
              </div>
            </div>
            
            <h2>教学评价与反馈</h2>
            <p>请对本次课程的教学质量进行评价，帮助我们不断改进教学方法和内容</p>
            
            <div class="evaluation-form">
              <h3>教学评价表</h3>
              
              <div class="rating-container">
                <div class="rating-item">
                  <div class="rating-label">教学内容设计</div>
                  <div class="rating-stars" id="content-design-rating">
                    <span v-for="i in 5" :key="`content-${i}`" class="star" :class="{ active: evaluationAnswers.teachingRatings.contentDesign >= i }" @click="setRating('contentDesign', i)">★</span>
                  </div>
                </div>
                
                <div class="rating-item">
                  <div class="rating-label">教学方法与指导</div>
                  <div class="rating-stars" id="teaching-method-rating">
                    <span v-for="i in 5" :key="`method-${i}`" class="star" :class="{ active: evaluationAnswers.teachingRatings.teachingMethod >= i }" @click="setRating('teachingMethod', i)">★</span>
                  </div>
                </div>
                
                <div class="rating-item">
                  <div class="rating-label">实践环节安排</div>
                  <div class="rating-stars" id="practice-arrangement-rating">
                    <span v-for="i in 5" :key="`practice-${i}`" class="star" :class="{ active: evaluationAnswers.teachingRatings.practiceArrangement >= i }" @click="setRating('practiceArrangement', i)">★</span>
                  </div>
                </div>
              </div>
              
              <div class="feedback-section">
                <div class="feedback-title">详细反馈</div>
                
                <div class="feedback-item">
                  <label class="feedback-label">您认为本课程的教学有哪些优点？</label>
                  <textarea class="feedback-input" rows="3" v-model="evaluationAnswers.teachingAdvantages" placeholder="请分享您的看法..."></textarea>
                </div>
                
                <div class="feedback-item">
                  <label class="feedback-label">您认为本课程的教学有哪些可以改进的地方？</label>
                  <textarea class="feedback-input" rows="3" v-model="evaluationAnswers.teachingImprovements" placeholder="请提出您的建议..."></textarea>
                </div>
              </div>
              
              <div class="check-item mt-4">
                <input type="checkbox" id="check6-1" v-model="level6Completed" @change="handleLevel6Completion">
                <label for="check6-1">我已认真完成教学评价！</label>
              </div>
            </div>
            
            <div class="mission-card mt-4">
              <h4>感谢您的反馈！</h4>
              <p>您的评价将帮助我们不断改进教学内容与教学方法，为更多学生提供优质的学习体验。</p>
            </div>
            
            <div style="margin-top: 30px;">
              <button class="btn btn-complete" @click="restartGame">重新开始挑战</button>
            </div>
          </div>
          
          <!-- 过关确认 -->
          <div class="level-completion" id="level-6-completion" v-if="showLevelCompletion[5]">
            <div class="completion-icon">📝</div>
            <div class="completion-title">第六关完成！</div>
            <div class="completion-message">您已完成教学评价与反馈</div>
            <div class="completion-badge-preview">
              <div>
                <div class="badge-icon">📝</div>
                <div>评价者</div>
              </div>
            </div>
            <p>已解锁勋章：评价者</p>
          </div>
          
          <div class="badges-container" v-if="showLevelCompletion[5]">
            <div class="badge" id="badge-7">
              <div>
                <div class="badge-icon">📝</div>
                <div>评价者</div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="level-actions">
          <button @click="prevLevel" class="prev-button">上一关</button>
          <button @click="finishGame" class="finish-button" :disabled="!level6Completed">完成挑战</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { passLevel, backLevel, writeLevel1, writeLevel2, writeLevel3, writeLevel4, writeLevel5, writeLevel6 } from '../api/user'
import { getLoginUser, userLogout } from '../api/user';

import '../assets/styles/level6-style.css';

export default {
  name: 'GameView',
  data() {
    return {
      currentLevel: 0,
      // 加载状态管理
      isLoading: false,
      levels: [
        { id: 0, name: '情境与问题' },
        { id: 1, name: '方案设计' },
        { id: 2, name: '动手搭建' },
        { id: 3, name: '成果展示' },
        { id: 4, name: '总结拓展' },
        { id: 5, name: '教学评价' }
      ],
      userInfo: {
        groupName: '第一组',
        commander: '刘燚娜老师'
      },
      showLevelCompletion: {
        0: false, // 第一关完成状态
        1: false,
        2: false,
        3: false,
        4: false,
        5: false
      },
      // 第三关任务相关变量
      activeTask: 'task1', // 当前活动任务
      task1Completed: false, // 任务一完成状态
      task2Completed: false, // 任务二完成状态
      selectedChallenge: '', // 当前选择的挑战任务
      challengeCompleted: false, // 挑战任务完成状态
      thirdChallenge: '', // 第三关挑战任务答案
      level4Completed: false, // 第四关完成状态
      level5Completed: false, // 第五关完成状态
      level6Completed: false, // 第六关完成状态
      // 第四关答案变量
      fourthScene: '', // 选择的场景
      fourthAchieve: '', // 实现功能
      fourthExtra: '', // 额外添加的功能
      fourthProblem: '', // 遇到的难题
      fourthSolution: '', // 解决方法
      evaluationAnswers: { // 学习评估答案
        question1: '',
        question2: '',
        skills: [],
        challenge: '',
        newFeature: '',
        valueInsight: '',
        // 第六关教学评价数据
        teachingRatings: {
          contentDesign: 0,
          teachingMethod: 0,
          practiceArrangement: 0
        },
        teachingAdvantages: '',
        teachingImprovements: '',
        // 第六关参数
        sixthEduComment1: "2",
        sixthEduComment2: "3",
        sixthEduComment3: "4",
        sixthFeedback1: "本课程的教学有哪些优点",
        sixthFeedback2: "教学有哪些可以改进的地方"
      },
      firstDilemma: '', // 第一关选择题答案（格式：选项标识:文字内容）
      // 第二关答案变量
      secondDesign1: '', // 老人在紧急情况下最可能喊出的语音指令（指令1）
      secondDesign2: '', // 老人在紧急情况下最可能喊出的语音指令（指令2）
      secondDesign3: '', // 行空板收到求救信息后，发出的语音安抚内容
      secondDesign4: '', // 发送给家人或云平台的信息内容
      secondRisk: '', // 场景中老人最可能遇到的风险
      secondScene: '', // 小组重点关注的场景 
        badgesEarned: 0, // 勋章获得数量
        tasksCompleted: 0, // 任务完成数量
      challengesBeaten: 0 // 挑战击败数量
    }
  },
  created() {
    // 组件创建时获取用户信息
    this.fetchUserInfo();
    // 监听页面刷新
    window.addEventListener('beforeunload', this.saveUserInfoToSession);
  },
    // 同步关卡完成状态，确保内部状态一致性
    syncLevelCompletionStatus() {
      // 确保showLevelCompletion对象存在
      if (!this.showLevelCompletion) {
        this.showLevelCompletion = {};
      }
      
      // 根据具体任务完成状态同步第三关显示状态
      if (this.task2Completed) {
        this.showLevelCompletion[2] = true;
      }
      
      // 同步第四关状态
      if (this.level4Completed) {
        this.showLevelCompletion[3] = true;
      }
      
      // 同步第五关状态
      if (this.level5Completed) {
        this.showLevelCompletion[4] = true;
      }
      
  },
  
  mounted() {
    // 尝试从Session恢复用户信息
    this.restoreUserInfoFromSession();
    // 如果没有用户信息，重新获取
    if (!this.userInfo.groupName) {
      this.fetchUserInfo();
    }
    // 尝试从Session恢复游戏进度
    this.restoreProgressFromSession();
    // 同步关卡完成状态确保一致性
    this.syncLevelCompletionStatus();
    // 保存当前进度
    this.saveProgressToSession();
    
    // 组件挂载后设置iframe
    this.setIframeSrc();
  },
  
  watch: {
    // 监听当前关卡变化
    currentLevel(newLevel) {
      // 保存进度到会话存储
      this.saveProgressToSession();
      
      // 使用统一的方法设置iframe src
      this.setIframeSrc();
    },
    
    // 监听用户信息变化，确保用户切换后正确设置iframe
    'userInfo.groupName': function(newGroupName, oldGroupName) {
      if (newGroupName !== oldGroupName) {
        console.log('用户信息变化，重新设置iframe:', { newGroupName, currentLevel: this.currentLevel });
        // 延迟一下确保DOM已更新
        setTimeout(() => {
          this.setIframeSrc();
        }, 100);
      }
    }
  },

  beforeUnmount() {
    window.removeEventListener('beforeunload', this.saveUserInfoToSession);
  },
  methods: {
    // 设置iframe的src根据当前关卡
    setIframeSrc() {
      // 确保在DOM更新后执行
      this.$nextTick(() => {
        console.log('设置iframe src:', { currentLevel: this.currentLevel, hasIframe: !!this.$refs.visibleIframe });
        if (this.$refs.visibleIframe) {
          if (this.currentLevel === 1) {
            console.log('设置iframe src为: ../../house.html');
            this.$refs.visibleIframe.src = "../../house.html";
          } else if (this.currentLevel === 2) {
            console.log('设置iframe src为: ../../cg.html');
            this.$refs.visibleIframe.src = "../../cg.html";
          }
        } else {
          console.error('visibleIframe引用不存在!');
        }
      });
    },
    // 预加载第三关的图片资源
    preloadLevel3Images() {
      try {
        // 第三关使用的所有图片URL列表
        const level3Images = [
          'https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511212312353.png',
          'https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511250038814.png',
          'https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511212312125.png',
          'https://img-1372837879.cos.ap-guangzhou.myqcloud.com/img/202511250124819.png'
        ];
        
        // 遍历图片列表，创建Image对象进行预加载
        level3Images.forEach((imageUrl) => {
          const img = new Image();
          img.src = imageUrl;
          
          // 可选：添加加载事件处理
          img.onload = () => {
            console.log(`图片预加载成功: ${imageUrl}`);
          };
          
          img.onerror = () => {
            console.warn(`图片预加载失败: ${imageUrl}`);
          };
        });
        
        console.log('第三关图片预加载任务已启动');
      } catch (error) {
        console.error('预加载图片时发生错误:', error);
      }
    },
    
    // 更新第一关选择题答案，将选项标识和文字内容合并
    updateFirstDilemmaText(value, text) {
      this.firstDilemma = `${value}:${text}`;
    },
    // 同步关卡完成状态，确保内部状态一致性
    syncLevelCompletionStatus() {
      // 确保showLevelCompletion对象存在
      if (!this.showLevelCompletion) {
        this.showLevelCompletion = {};
      }
      
      // 根据具体任务完成状态同步第三关显示状态
      if (this.task2Completed) {
        this.showLevelCompletion[2] = true;
      }
      
      // 同步第四关状态
      if (this.level4Completed) {
        this.showLevelCompletion[3] = true;
      }
      
      // 同步第五关状态
      if (this.level5Completed) {
        this.showLevelCompletion[4] = true;
      }
      
      // 同步第六关状态
      if (this.level6Completed) {
        this.showLevelCompletion[5] = true;
      }
      
      // 重新计算成就以确保数据一致性
      this.calculateAchievements();
    },
    // 设置星级评分
    setRating(ratingType, value) {
      this.evaluationAnswers.teachingRatings[ratingType] = value;
    },
    
    // 处理第六关完成
    handleLevel6Completion() {
      if (this.level6Completed && !this.showLevelCompletion[5]) {
        // 显示完成界面
        this.showLevelCompletion[5] = true;
        // 计算成就统计
        this.calculateAchievements();
        // 保存进度
        this.saveProgressToSession();
      }
    },
    
    // 重新开始挑战
    restartGame() {
      // 重置所有游戏状态
      this.currentLevel = 0;
      this.task1Completed = false;
      this.task2Completed = false;
      this.activeTask = 'task1';
      this.selectedChallenge = '';
      this.challengeCompleted = false;
      this.level4Completed = false;
      this.level5Completed = false;
      this.level6Completed = false;
      this.showLevelCompletion = {
        0: false,
        1: false,
        2: false,
        3: false,
        4: false,
        5: false
      };
      // 重置评价数据
      this.evaluationAnswers = {
        question1: '',
        question2: '',
        skills: [],
        challenge: '',
        newFeature: '',
        valueInsight: '',
        teachingRatings: {
          contentDesign: 0,
          teachingMethod: 0,
          practiceArrangement: 0
        },
        teachingAdvantages: '',
        teachingImprovements: ''
      };
      // 重置成就统计
      this.badgesEarned = 0;
      this.tasksCompleted = 0;
      this.challengesBeaten = 0;
      
      // 清除所有相关的sessionStorage数据
      sessionStorage.removeItem('gameProgress');
      // 清除可能存在的旧格式数据
      sessionStorage.removeItem('task1Completed');
      sessionStorage.removeItem('task2Completed');
      
      // 保存重置后的进度（可选）
      this.saveProgressToSession();
    },
    
    // 完成游戏
    finishGame() {
      // 验证是否确实完成了第六关
      if (this.level6Completed) {
        // 确保第六关显示为已完成
        this.showLevelCompletion[5] = true;
        // 重新计算最终成就
        this.calculateAchievements();
        // 保存最终进度
        this.saveProgressToSession();
        // 显示完成提示
        alert('恭喜您完成所有挑战！');
      }
    },
    
    // 保存第二关答案
    async saveSecondLevelAnswer() {
      try {
        // 从DOM元素获取用户输入的值
        const sceneSelect = document.getElementById('scene-select');
        const riskInput = document.getElementById('risk-input');
        const cmd1 = document.getElementById('cmd1');
        const cmd2 = document.getElementById('cmd2');
        const responseSpeech = document.getElementById('response-speech');
        const cloudData = document.getElementById('cloud-data');
        
        // 更新Vue实例的数据
        this.secondScene = sceneSelect ? sceneSelect.value : '';
        this.secondRisk = riskInput ? riskInput.value : '';
        this.secondDesign1 = cmd1 ? cmd1.value : '';
        this.secondDesign2 = cmd2 ? cmd2.value : '';
        this.secondDesign3 = responseSpeech ? responseSpeech.value : '';
        this.secondDesign4 = cloudData ? cloudData.value : '';
        
        // 构建请求数据
        const secondLevelRequest = {
          secondDesign1: this.secondDesign1,
          secondDesign2: this.secondDesign2,
          secondDesign3: this.secondDesign3,
          secondDesign4: this.secondDesign4,
          secondRisk: this.secondRisk,
          secondScene: this.secondScene
        };
        
        // 只有在有网络连接且环境允许的情况下才调用后端接口
        if (navigator.onLine) {
          const response = await writeLevel2(secondLevelRequest);
          // 检查响应
          if (response && response.code === 0) {
            console.log('第二关答案保存成功');
            return true;
          } else {
            console.warn('第二关答案保存失败：', response?.message || '未知错误');
          }
        } else {
          console.log('当前处于离线状态，已保存到本地');
        }
        // 无论接口是否成功，都保存到本地
        this.saveProgressToSession();
        return true;
      } catch (error) {
        console.error('保存第二关答案时出错：', error);
        // 出错时也保存到本地
        this.saveProgressToSession();
        return true; // 即使保存失败也允许继续游戏
      }
    },
    
    // 保存第三关答案
    async saveThirdLevelAnswer() {
      try {
        // 从DOM元素获取用户选择的挑战任务
        const challengeSelect = document.getElementById('challenge-select');
        
        // 获取选择的显示文本而不是value值
        let selectedText = '';
        if (challengeSelect && challengeSelect.selectedIndex !== -1) {
          selectedText = challengeSelect.options[challengeSelect.selectedIndex].text;
        }
        
        // 更新Vue实例的数据
        this.thirdChallenge = selectedText;
        
        // 构建请求数据
        const thirdLevelRequest = {
          thirdChallenge: this.thirdChallenge
        };
        
        // 只有在有网络连接且环境允许的情况下才调用后端接口
        if (navigator.onLine) {
          const response = await writeLevel3(thirdLevelRequest);
          // 检查响应
          if (response && response.code === 0) {
            console.log('第三关答案保存成功');
            return true;
          } else {
            console.warn('第三关答案保存失败：', response?.message || '未知错误');
          }
        } else {
          console.log('当前处于离线状态，已保存到本地');
        }
        // 无论接口是否成功，都保存到本地
        this.saveProgressToSession();
        return true;
      } catch (error) {
        console.error('保存第三关答案时出错：', error);
        // 出错时也保存到本地
        this.saveProgressToSession();
        return true; // 即使保存失败也允许继续游戏
      }
    },
    
    // 保存第四关答案
    async saveFourthLevelAnswer() {
      try {
        // 从DOM元素获取用户输入的值
        const finalScene = document.getElementById('final-scene');
        const finalFunction = document.getElementById('final-function');
        const finalExtra = document.getElementById('final-extra');
        const finalBoss = document.getElementById('final-boss');
        const finalSolution = document.getElementById('final-solution');
        
        // 更新Vue实例的数据
        this.fourthScene = finalScene ? finalScene.value : '';
        this.fourthAchieve = finalFunction ? finalFunction.value : '';
        this.fourthExtra = finalExtra ? finalExtra.value : '';
        this.fourthProblem = finalBoss ? finalBoss.value : '';
        this.fourthSolution = finalSolution ? finalSolution.value : '';
        
        // 构建请求数据
        const fourthLevelRequest = {
          fourthAchieve: this.fourthAchieve,
          fourthExtra: this.fourthExtra,
          fourthProblem: this.fourthProblem,
          fourthScene: this.fourthScene,
          fourthSolution: this.fourthSolution
        };
        
        // 只有在有网络连接且环境允许的情况下才调用后端接口
        if (navigator.onLine) {
          const response = await writeLevel4(fourthLevelRequest);
          // 检查响应
          if (response && response.code === 0) {
            console.log('第四关答案保存成功');
            return true;
          } else {
            console.warn('第四关答案保存失败：', response?.message || '未知错误');
          }
        } else {
          console.log('当前处于离线状态，已保存到本地');
        }
        // 无论接口是否成功，都保存到本地
        this.saveProgressToSession();
        return true;
      } catch (error) {
        console.error('保存第四关答案时出错：', error);
        // 出错时也保存到本地
        this.saveProgressToSession();
        return true; // 即使保存失败也允许继续游戏
      }
    },
    
    // 保存第五关答案
    async saveFifthLevelAnswer() {
      try {
        // 创建选项值到文字描述的映射
        const ratingMap = {
          '5': '非常同意',
          '4': '同意', 
          '3': '一般',
          '2': '不同意',
          '1': '非常不同意'
        };
        
        const skillMap = {
          'mindplus': '使用Mind+进行图形化编程',
          'speech': '配置和使用语音识别模块',
          'mqtt': '理解并配置MQTT协议参数',
          'iot': '将硬件设备数据上传至云平台(Easy IoT)',
          'debug': '软硬件联调与故障排查',
          'teamwork': '团队协作与项目规划'
        };
        
        const challengeMap = {
          'design': '方案设计（将想法转化为具体方案）',
          'speech': '语音识别程序编写与调试',
          'mqtt': 'MQTT参数配置与联网',
          'debug': '软硬件联合调试',
          'none': '没有特别困难的环节'
        };
        
        // 转换技能选择为文字描述
        let skillsText = '';
        if (this.evaluationAnswers.skills && Array.isArray(this.evaluationAnswers.skills)) {
          skillsText = this.evaluationAnswers.skills
            .map(skill => skillMap[skill] || skill)
            .join('、');
        }
        
        // 构建请求数据，使用文字描述而非选项ID
        const fifthLevelRequest = {
          fifthComment1: ratingMap[this.evaluationAnswers.question1] || '',
          fifthComment2: ratingMap[this.evaluationAnswers.question2] || '',
          fifthComment3: skillsText,
          fifthComment4: challengeMap[this.evaluationAnswers.challenge] || '',
          fifthComment5: this.evaluationAnswers.newFeature || '',
          fifthComment6: this.evaluationAnswers.valueInsight || ''
        };
        
        // 只有在有网络连接且环境允许的情况下才调用后端接口
        if (navigator.onLine) {
          const response = await writeLevel5(fifthLevelRequest);
          // 检查响应
          if (response && response.code === 0) {
            console.log('第五关答案保存成功');
            return true;
          } else {
            console.warn('第五关答案保存失败：', response?.message || '未知错误');
          }
        } else {
          console.log('当前处于离线状态，已保存到本地');
        }
        // 无论接口是否成功，都保存到本地
        this.saveProgressToSession();
        return true;
      } catch (error) {
        console.error('保存第五关答案时出错：', error);
        // 出错时也保存到本地
        this.saveProgressToSession();
        return true; // 即使保存失败也允许继续游戏
      }
    },
    
    // 保存第六关答案
    async saveSixthLevelAnswer() {
      try {
        // 构建请求数据
        const sixthLevelRequest = {
          sixthEduComment1: this.evaluationAnswers.teachingRatings.contentDesign,
          sixthEduComment2: this.evaluationAnswers.teachingRatings.teachingMethod,
          sixthEduComment3: this.evaluationAnswers.teachingRatings.practiceArrangement,
          sixthFeedback1: this.evaluationAnswers.teachingAdvantages,
          sixthFeedback2: this.evaluationAnswers.teachingImprovements
        };
        
        // 只有在有网络连接且环境允许的情况下才调用后端接口
        if (navigator.onLine) {
          const response = await writeLevel6(sixthLevelRequest);
          // 检查响应
          if (response && response.code === 0) {
            console.log('第六关答案保存成功');
            return true;
          } else {
            console.warn('第六关答案保存失败：', response?.message || '未知错误');
          }
        } else {
          console.log('当前处于离线状态，已保存到本地');
        }
        // 无论接口是否成功，都保存到本地
        this.saveProgressToSession();
        return true;
      } catch (error) {
        console.error('保存第六关答案时出错：', error);
        // 出错时也保存到本地
        this.saveProgressToSession();
        return true; // 即使保存失败也允许继续游戏
      }
    },
    // 显示挑战任务描述
    showChallengeDescription(event) {
      this.selectedChallenge = event.target.value;
    },
    // 获取用户信息
    async fetchUserInfo() {
      try {
        // 只有在有网络连接且环境允许的情况下才调用后端接口
        if (navigator.onLine) {
          const response = await getLoginUser();
          // 检查响应格式，避免解构undefined
          if (response && response.code === 0 && response.data) {
            const data = response.data;
            this.userInfo = {
              groupName: data.groupName || (data.id ? `第${data.id}组` : '第一组'),
              commander: data.commander || '刘燚娜老师'
            };
            this.saveUserInfoToSession();
          }
        }
        // 如果离线或接口调用失败，保持默认值或从session恢复
      } catch (error) {
        console.warn('获取用户信息接口暂时不可用，使用默认用户信息');
      }
    },
    
    // 任务一完成处理
    handleTask1Completion() {
      if (this.task1Completed && !this.showLevelCompletion[2]) {
        // 任务一单独完成时的处理
        console.log('任务一已完成');
        this.saveProgressToSession();
      }
    },
    
    // 任务二完成处理
    handleTask2Completion() {
      if (this.task2Completed && !this.showLevelCompletion[2]) {
        // 当任务二完成时，标记整个第三关为完成状态
        this.showLevelCompletion[2] = true;
        console.log('任务二已完成，第三关所有任务已完成');
        // 计算成就
        this.calculateAchievements();
        this.saveProgressToSession();
      }
    },
    
    // 第四关完成处理
    handleLevel4Completion() {
      if (this.level4Completed && !this.showLevelCompletion[3]) {
        // 标记第四关为完成状态
        this.showLevelCompletion[3] = true;
        console.log('第四关已完成');
        // 计算成就
        this.calculateAchievements();
        this.saveProgressToSession();
      }
    },
    
    // 第五关完成处理
    async handleLevel5Completion() {
      if (this.level5Completed) {
        // 先保存第五关答案到后端
        await this.saveFifthLevelAnswer();
        
        if (!this.showLevelCompletion[4]) {
          // 标记第五关为完成状态
          this.showLevelCompletion[4] = true;
          // 计算成就统计
          this.calculateAchievements();
          console.log('第五关已完成');
        }
        this.saveProgressToSession();
      }
    },
    
    // 第六关完成处理
    async handleLevel6Completion() {
      if (this.level6Completed) {
        // 先保存第六关答案到后端
        await this.saveSixthLevelAnswer();
        
        // 标记第六关为完成状态
        this.showLevelCompletion[5] = true;
        // 计算成就统计
        this.calculateAchievements();
        console.log('第六关已完成');
      }
      this.saveProgressToSession();
    },
    
    // 设置教学评价分数
    setTeachingRating(type, value) {
      this.evaluationAnswers.teachingRatings[type] = value;
      this.saveProgressToSession();
    },
    
    // 计算成就统计
    calculateAchievements() {
      // 重置成就计数
      this.badgesEarned = 1; // 基础勋章
      this.tasksCompleted = 0;
      this.challengesBeaten = 0;
      
      // 计算获得的勋章数量 - 添加空值检查
      if (this.showLevelCompletion) {
        if (this.showLevelCompletion[0]) this.badgesEarned += 1;
        if (this.showLevelCompletion[1]) this.badgesEarned += 1;
        if (this.showLevelCompletion[2]) this.badgesEarned += 2; // 第三关有2个勋章
        if (this.showLevelCompletion[3]) this.badgesEarned += 1;
        if (this.showLevelCompletion[4]) this.badgesEarned += 1;
        if (this.showLevelCompletion[5]) this.badgesEarned += 1; // 第六关有1个勋章
      }
      
      // 计算完成的任务数量
      if (this.task1Completed) this.tasksCompleted += 1;
      if (this.task2Completed) this.tasksCompleted += 1;
      if (this.level4Completed) this.tasksCompleted += 1;
      if (this.level5Completed) this.tasksCompleted += 1;
      if (this.level6Completed) this.tasksCompleted += 1;
      
      // 计算击败的挑战数量
      if (this.challengeCompleted) this.challengesBeaten = 1;
    },
    
    // 保存进度到sessionStorage
    saveProgressToSession() {
      try {
        // 数据验证和过滤，创建安全的进度对象
        const safeProgress = {
          currentLevel: Number(this.currentLevel) || 0,
          showLevelCompletion: this.showLevelCompletion || {},
          task1Completed: Boolean(this.task1Completed),
          task2Completed: Boolean(this.task2Completed),
          activeTask: String(this.activeTask) || 'task1',
          selectedChallenge: String(this.selectedChallenge) || '',
          challengeCompleted: Boolean(this.challengeCompleted),
          thirdChallenge: String(this.thirdChallenge) || '',
          level4Completed: Boolean(this.level4Completed),
          level5Completed: Boolean(this.level5Completed),
          level6Completed: Boolean(this.level6Completed),
          evaluationAnswers: this.evaluationAnswers || {
            teachingRatings: { contentDesign: 0, teachingMethod: 0, practiceArrangement: 0 }
          },
          badgesEarned: Number(this.badgesEarned) || 0,
          tasksCompleted: Number(this.tasksCompleted) || 0,
          challengesBeaten: Number(this.challengesBeaten) || 0,
          saveTime: new Date().toISOString()
        };
        
        // 批量保存以减少sessionStorage操作次数
        sessionStorage.setItem('gameProgress', JSON.stringify(safeProgress));
      } catch (error) {
        console.error('保存进度失败:', error);
      }
    },
    // 从sessionStorage恢复进度
    restoreProgressFromSession() {
      try {
        // 优先尝试新的批量存储格式
        const savedProgress = sessionStorage.getItem('gameProgress');
        
        if (savedProgress) {
          const parsed = JSON.parse(savedProgress);
          
          // 验证并恢复每个字段，设置合理的默认值
          this.currentLevel = Number(parsed.currentLevel) || 0;
          
          // 安全恢复showLevelCompletion
          if (parsed.showLevelCompletion && typeof parsed.showLevelCompletion === 'object') {
            this.showLevelCompletion = { ...this.showLevelCompletion, ...parsed.showLevelCompletion };
          }
          
          this.task1Completed = Boolean(parsed.task1Completed);
          this.task2Completed = Boolean(parsed.task2Completed);
          this.activeTask = String(parsed.activeTask) || 'task1';
          this.selectedChallenge = String(parsed.selectedChallenge) || '';
          this.challengeCompleted = Boolean(parsed.challengeCompleted);
          this.thirdChallenge = String(parsed.thirdChallenge) || '';
          this.level4Completed = Boolean(parsed.level4Completed);
          this.level5Completed = Boolean(parsed.level5Completed);
          this.level6Completed = Boolean(parsed.level6Completed);
          
          // 安全恢复evaluationAnswers，确保结构完整
          if (parsed.evaluationAnswers) {
            this.evaluationAnswers = {
              ...this.evaluationAnswers,
              ...parsed.evaluationAnswers,
              teachingRatings: {
                ...this.evaluationAnswers.teachingRatings,
                ...(parsed.evaluationAnswers.teachingRatings || {})
              }
            };
          }
          
          this.badgesEarned = Number(parsed.badgesEarned) || 0;
          this.tasksCompleted = Number(parsed.tasksCompleted) || 0;
          this.challengesBeaten = Number(parsed.challengesBeaten) || 0;
        } else {
          // 兼容旧的存储格式
          this.restoreLegacyProgress();
        }
      } catch (error) {
        console.error('恢复进度失败:', error);
        // 错误处理：不覆盖当前状态，避免数据丢失
      }
    },
    
    // 恢复旧格式的进度数据（兼容性处理）
    restoreLegacyProgress() {
      try {
        const task1Completed = sessionStorage.getItem('task1Completed');
        const task2Completed = sessionStorage.getItem('task2Completed');
        // 其他旧格式数据恢复...
      } catch (legacyError) {
        console.warn('恢复旧格式进度失败');
      }
    },
    
    // 保存用户信息到Session
    saveUserInfoToSession() {
      if (this.userInfo) {
        sessionStorage.setItem('gameUserInfo', JSON.stringify(this.userInfo));
      }
    },
    
    // 从Session恢复用户信息
    restoreUserInfoFromSession() {
      const stored = sessionStorage.getItem('gameUserInfo');
      if (stored) {
        try {
          this.userInfo = JSON.parse(stored);
        } catch (error) {
          console.error('解析用户信息失败:', error);
        }
      }
    },
    
    // 退出登录
    async handleLogout() {
      try {
        // 尝试调用退出登录接口
        if (navigator.onLine) {
          await userLogout();
        }
        // 无论接口调用成功与否，都执行本地清理和跳转
        this.clearUserDataAndRedirect();
      } catch (error) {
        console.warn('退出登录接口调用失败，但仍执行本地清理');
        // 即使失败也清除本地信息并重定向
        this.clearUserDataAndRedirect();
      }
    },
    
    // 清理用户数据并重定向到登录页
    clearUserDataAndRedirect() {
      try {
        // 获取用户信息用于清除对应localStorage中的进度数据
        const userInfoStr = sessionStorage.getItem('gameUserInfo');
        if (userInfoStr) {
          try {
            const userName = JSON.parse(userInfoStr).groupName;
            // 清除localStorage中可能存在的进度数据
            localStorage.removeItem(`gameProgress_${userName}`);
          } catch (parseError) {
            console.error('解析用户信息失败:', parseError);
          }
        }
        
        // 清除所有与游戏进度相关的数据
        sessionStorage.removeItem('gameUserInfo');
        sessionStorage.removeItem('gameProgress');
        
        console.log('所有游戏进度相关的持久化数据已清空');
      } catch (error) {
        console.error('清除持久化数据时出错:', error);
      }
      // 重定向到登录页面根路径
      try {
        if (this.$router) {
          this.$router.push('/');
        } else {
          // 如果router不可用，使用window.location
          window.location.href = '/';
        }
      } catch (redirectError) {
        console.error('重定向失败:', redirectError);
        // 最终备用方案
        window.location.href = '/';
      }
    },
    
    async nextLevel() {
      // 添加边界检查
      if (this.currentLevel >= 0 && this.currentLevel < this.levels.length - 1) {
        // 如果已经在加载中，则不重复请求
        if (this.isLoading) return;
        
        try {
          // 设置加载状态
          this.isLoading = true;
          
          // 当用户从第一关切换到第二关时，提交第一关的答案
          if (this.currentLevel === 0) {
            // 确保用户已经选择了答案
            if (!this.firstDilemma) {
              alert('请选择第一关的答案');
              this.isLoading = false;
              return;
            }
            
            // 提交第一关答案（已包含选项标识和文字内容）
            const firstLevelResponse = await writeLevel1({
              firstDilemma: this.firstDilemma
            });
            
            if (!firstLevelResponse.success && !firstLevelResponse.data) {
              console.error('提交第一关答案失败:', firstLevelResponse.message || '未知错误');
              alert(firstLevelResponse.message || '提交答案失败，请稍后重试');
              this.isLoading = false;
              return;
            }
          } else if (this.currentLevel === 1) {
            // 当用户从第二关切换到第三关时，提交第二关的答案
            await this.saveSecondLevelAnswer();
          } else if (this.currentLevel === 2) {
            // 当用户完成第三关时，提交第三关的答案
            await this.saveThirdLevelAnswer();
          } else if (this.currentLevel === 3) {
            // 当用户完成第四关时，提交第四关的答案
            await this.saveFourthLevelAnswer();
          }
          
          // 请求后端通关接口
            const response = await passLevel(this.currentLevel);
          
          // 检查响应是否成功
          if (response.success || response.data) {
            // 标记当前关卡为已完成
            this.showLevelCompletion[this.currentLevel] = true;
            
            // 安全递增关卡，确保不会越界
            this.currentLevel = Math.min(this.currentLevel + 1, this.levels.length - 1);
            
            // 保存进度到会话存储
            this.saveProgressToSession();
          } else {
            console.error('通关失败:', response.message || '未知错误');
            alert(response.message || '通关失败，请稍后重试');
          }
        } catch (error) {
          console.error('通关请求失败:', error);
          // 根据错误类型显示不同的提示
          if (error.response) {
            alert(`请求失败: ${error.response.status} ${error.response.statusText}`);
          } else if (error.request) {
            alert('网络连接失败，请检查您的网络设置');
          } else {
            alert('请求过程中发生错误，请稍后重试');
          }
        } finally {
          // 无论成功失败，都要重置加载状态
          this.isLoading = false;
        }
      }
    },
    
    async prevLevel() {
      if (this.currentLevel > 0) {
        // 如果已经在加载中，则不重复请求
        if (this.isLoading) return;
        
        try {
          // 设置加载状态
          this.isLoading = true;
          
          // 请求后端上一关接口
            const response = await backLevel();
          
          // 检查响应是否成功
          if (response.success || response.data) {
            this.currentLevel--;
            // 保存进度到会话存储
            this.saveProgressToSession();
          } else {
            console.error('返回上一关失败:', response.message || '未知错误');
            alert(response.message || '返回上一关失败，请稍后重试');
          }
        } catch (error) {
          console.error('上一关请求失败:', error);
          // 根据错误类型显示不同的提示
          if (error.response) {
            alert(`请求失败: ${error.response.status} ${error.response.statusText}`);
          } else if (error.request) {
            alert('网络连接失败，请检查您的网络设置');
          } else {
            alert('请求过程中发生错误，请稍后重试');
          }
        } finally {
          // 无论成功失败，都要重置加载状态
          this.isLoading = false;
        }
      }
    },
    
    finishGame() {
      // 完成游戏的逻辑
      alert('恭喜你完成了所有关卡的挑战！');
    }
  }
}
</script>
<style scoped>
  /* 确保选项文本不加粗 */
  .option label {
    font-weight: normal !important;
  }
  /* 确保输入框文本不加粗 */
  .feedback-input {
    font-weight: normal !important;
  }
</style>

<style scoped>
  .badges-container {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 20px;
    margin: 20px 0;
  }
  
  .badge {
    text-align: center;
  }
  
  .completion-badge-preview {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .text-justify {
    text-align: center;
    white-space: nowrap;
    display: inline-block;
    min-width: 100px;
  }
  
  .level-completion p {
    text-align: center;
    white-space: nowrap;
    display: inline-block;
    min-width: 100%;
  }
  
  /* 第五关样式 */
  .completion-screen {
    text-align: center;
  }
  
  .final-badge {
    background: linear-gradient(135deg, #ffd700, #ffb700);
    border-radius: 50%;
    width: 150px;
    height: 150px;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 20px auto;
    box-shadow: 0 4px 20px rgba(255, 215, 0, 0.5);
    border: 4px solid #fff;
  }
  
  .final-badge div {
    text-align: center;
    color: #8b4513;
    font-weight: bold;
  }
  
  .achievement-stats {
    display: flex;
    justify-content: space-around;
    margin: 30px 0;
    background: #f0f7ff;
    padding: 20px;
    border-radius: 15px;
  }
  
  .stat {
    text-align: center;
    flex: 1;
  }
  
  .stat-value {
    font-size: 2.5em;
    font-weight: bold;
    color: #4a4a8c;
  }
  
  .stat-label {
    color: #666;
    margin-top: 5px;
  }
  
  .feedback-item {
    margin: 20px 0;
    text-align: left;
  }
  
  .feedback-label {
    display: block;
    margin-bottom: 10px;
    font-weight: 500;
    color: #333;
  }
  
  .options-container {
    display: flex;
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
  
  .horizontal-options {
          display: flex !important;
          flex-direction: row !important;
          flex-wrap: wrap !important;
          gap: 15px !important;
          width: 100% !important;
          box-sizing: border-box !important;
          justify-content: flex-start !important;
        }
        .horizontal-options .option {
          background-color: #ffffff !important;
          border: 1px solid #e0e0e0 !important;
          border-radius: 4px !important;
          padding: 8px 12px !important;
          display: flex !important;
          align-items: center !important;
          text-align: left !important;
          min-width: 180px !important;
          margin: 0 !important;
          flex: 0 0 auto !important;
        }
        .horizontal-options .option input[type="checkbox"] {
          margin-right: 8px !important;
          margin-left: 0 !important;
        }
        .horizontal-options .option label {
          margin: 0 !important;
          text-align: left !important;
          white-space: nowrap !important;
        }
  
  .option {
    display: flex;
    align-items: center;
    cursor: pointer;
    text-align: left;
  }
  
  .option input[type="radio"] {
    margin-right: 5px;
  }
  
  .option input[type="checkbox"] {
    margin-right: 5px;
  }
  
  .option label {
    margin-left: 5px;
  }
  
  .textarea-field {
    width: 100%;
    padding: 12px;
    border: 1px solid #ddd;
    border-radius: 8px;
    min-height: 100px;
    resize: vertical;
    font-family: inherit;
  }
  
  .textarea-field:focus {
    outline: none;
    border-color: #4a4a8c;
    box-shadow: 0 0 0 2px rgba(74, 74, 140, 0.1);
  }
  
  .encouragement-message {
    background: #fff3cd;
    border: 1px solid #ffeaa7;
    border-radius: 10px;
    padding: 20px;
    margin-top: 30px;
    text-align: center;
  }
  
  .encouragement-message h4 {
    color: #856404;
    margin-bottom: 10px;
  }
  
  .mt-4 {
    margin-top: 20px;
  }
  
  .mt-3 {
    margin-top: 15px;
  }
</style>

<style scoped>
.game-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  color: #333;
}

.game-content {
  background-color: #ffffff;
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  text-align: center;
  margin-bottom: 30px;
}

.game-header {
  margin-bottom: 30px;
}

.task-badge {
  background-color: #ff4757;
  color: white;
  display: inline-block;
  padding: 8px 20px;
  border-radius: 30px;
  font-weight: bold;
  font-size: 14px;
  margin-bottom: 15px;
}

.game-title {
  font-size: 2.5em;
  color: #4a4a8c;
  margin: 15px 0;
  font-weight: bold;
}

.game-subtitle {
  font-size: 1.2em;
  color: #666;
  margin-bottom: 30px;
}

.team-info {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 40px;
  flex-wrap: wrap;
  margin-bottom: 30px;
  background-color: #f0f7ff;
  padding: 20px;
  border-radius: 15px;
  border: 2px solid #d0e6ff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.team-member {
  background-color: #e8f4fd;
  padding: 12px 24px;
  border-radius: 15px;
  color: #4a4a8c;
  font-weight: 500;
}

.exit-button {
  background-color: #ff4757;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 15px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s ease;
}

.exit-button:hover {
  background-color: #ff3347;
}

.progress-container {
  margin: 40px auto;
  max-width: 900px;
}

.progress-track {
  display: flex;
  justify-content: space-between;
  position: relative;
  margin-bottom: 30px;
}

.progress-step {
  flex: 1;
  position: relative;
  display: flex;
  align-items: center;
}

.progress-dot {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #ddd;
  border: 3px solid #fff;
  z-index: 2;
  transition: all 0.3s ease;
}

.progress-line {
  flex: 1;
  height: 4px;
  background-color: #ddd;
  position: absolute;
  left: 24px;
  right: 0;
  z-index: 1;
  transition: all 0.3s ease;
}

.progress-step.active .progress-dot,
.progress-step.completed .progress-dot {
  background-color: #4a4a8c;
}

.progress-step.completed .progress-line {
  background-color: #4a4a8c;
}

.progress-container {
  margin: 40px auto;
  max-width: 900px;
  background: linear-gradient(135deg, #4a11cb 0%, #2575fc 100%);
  padding: 20px;
  border-radius: 20px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.progress-track {
  display: flex;
  justify-content: space-between;
  position: relative;
  margin-bottom: 20px;
}

.progress-step {
  flex: 1;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.progress-dot {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #ddd;
  border: 3px solid #fff;
  z-index: 2;
  transition: all 0.3s ease;
  margin-bottom: 10px;
}

.progress-line {
  width: 100%;
  height: 4px;
  background-color: #ddd;
  position: absolute;
  top: 12px;
  left: 50%;
  z-index: 1;
  transition: all 0.3s ease;
}

.progress-step:last-child .progress-line {
  display: none;
}

.level-labels {
  display: flex;
  justify-content: space-between;
  font-size: 0.9em;
  width: 100%;
}

.level-label {
  color: #fff;
  text-align: center;
  transition: all 0.3s ease;
  font-weight: bold;
  padding: 8px 12px;
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 0.1);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  width: 100px;
  margin: 0 auto;
}

.level-label.active {
  color: #4a4a8c;
  background-color: rgba(255, 255, 255, 0.9);
  font-weight: bold;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  text-shadow: none;
}

.level-label.completed {
  color: white;
  background-color: rgba(46, 213, 115, 0.9);
  font-weight: bold;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.level-content {
      max-width: 1400px;
      margin: 0 auto;
      padding: 0 20px;
    }
    
    /* 第三关特定样式 */
    .task-navigation {
      display: flex;
      gap: 10px;
      margin: 20px 0;
    }
    
    /* 第三关文字在页面宽度内正常显示 */
    .level-2 .task-step p,
    .level-2 .step-section p,
    .level-2 .task-card p,
    .level-2 h4 {
      word-break: break-word;
      hyphens: auto;
      display: block;
      white-space: normal;
      width: 100%;
      max-width: 100%;
      overflow: hidden;
    }
    
    /* 标题保持正常显示 */
    .level-2 h2 {
      word-break: break-word;
      hyphens: auto;
      display: block;
      white-space: normal;
      width: 100%;
      max-width: 100%;
    }
    
    /* 防止WiFi等特定词汇被错误拆分 */
    .level-2 p {
      word-break: break-word;
      hyphens: auto;
      display: block;
      white-space: normal;
      width: 100%;
      max-width: 100%;
      overflow: hidden;
    }
    
    /* 确保内容容器在页面宽度内 */
    .level-2 .task-card,
    .level-2 .step-section,
    .level-2 .task-step {
      width: 100%;
      max-width: 100%;
      overflow-x: hidden;
    }
    
    .task-nav-btn {
      padding: 10px 20px;
      border: 2px solid #2196F3;
      background: white;
      color: #2196F3;
      border-radius: 20px;
      cursor: pointer;
      transition: all 0.3s ease;
      font-size: 14px;
    }
    
    .task-nav-btn.active {
      background: #2196F3;
      color: white;
    }
    
    .task-nav-btn:hover:not(.task-locked) {
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(33, 150, 243, 0.3);
    }
    
    /* 移除默认隐藏，让v-show指令正常工作 */
    .task-content {
      /* display: none; 已移除，改为由v-show指令控制 */
    }
    
    /* 保留active类作为额外样式增强 */
    .task-content.active {
      display: block;
    }
    
    .task-card {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 20px;
      margin-bottom: 20px;
      border-left: 4px solid #2196F3;
    }
    
    .task-card.task-locked {
      position: relative;
      opacity: 0.6;
    }
    
    .task-locked-overlay {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(255, 255, 255, 0.8);
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: bold;
      color: #666;
    }
    
    .task-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;
    }
    
    .task-title {
      font-size: 18px;
      font-weight: bold;
      color: #333;
    }
    
    .task-status-badge {
      padding: 5px 12px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 500;
    }
    
    .task-status-badge.status-inprogress {
      background: #FF9800;
      color: white;
    }
    
    .task-status-badge.status-completed {
      background: #4CAF50;
      color: white;
    }
    
    .task-status-badge.status-locked {
      background: #9E9E9E;
      color: white;
    }
    
    .task-step {
      margin-bottom: 20px;
    }
    
    .task-step p {
      margin-bottom: 10px;
    }
    
    .guide-image {
      max-width: 100%;
      height: auto;
      border: 1px solid #ddd;
      border-radius: 6px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }
    
    .step-section {
      margin-bottom: 25px;
    }
    
    .step-title {
      font-size: 16px;
      font-weight: bold;
      color: #2196F3;
      margin-bottom: 10px;
      padding-bottom: 5px;
      border-bottom: 2px solid #e3f2fd;
    }
    
    .check-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 15px;
      background: white;
      border-radius: 6px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
      margin-bottom: 15px;
    }
    
    .check-item input[type="checkbox"] {
      width: 20px;
      height: 20px;
      cursor: pointer;
    }
    
    .check-item label {
      cursor: pointer;
      font-weight: 500;
    }
    
    .highlight-task-completion {
      transition: all 0.3s ease;
    }
    
    .highlight-task-completion input[type="checkbox"]:checked + label {
      color: #4CAF50;
      text-decoration: line-through;
    }
    
    .highlight-task-completion input[type="checkbox"]:checked {
      accent-color: #4CAF50;
    }
    
    .task-unlock-message {
      background: #e8f5e9;
      color: #4CAF50;
      padding: 12px;
      border-radius: 6px;
      margin-top: 15px;
      display: flex;
      align-items: center;
      gap: 10px;
      font-weight: 500;
    }
    
    .mt-3 {
      margin-top: 15px;
    }
    
    /* 链接样式 */
    a {
      color: #2196F3;
      text-decoration: none;
    }
    
    a:hover {
      text-decoration: underline;
    }

.mission-card {
  background-color: #ffffff;
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  min-height: 400px;
  display: flex;
  flex-direction: column;
  border-left: 5px solid #4a4a8c;
}

.mission-objective {
  background-color: #e8f4fd;
  padding: 20px;
  border-radius: 15px;
  margin: 20px 0;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.mission-objective h3 {
  color: #4a4a8c;
  margin-top: 0;
  margin-bottom: 10px;
  font-weight: bold;
  text-align: left;
}

.mission-objective p {
  text-align: left;
  color: #666;
  font-size: 1.1em;
  margin-bottom: 10px;
}

.question-container {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 15px;
  margin: 20px 0;
  border-left: 5px solid #4a4a8c;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.question-text {
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
  text-align: left;
}

.options-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.option {
  background-color: #ffffff;
  padding: 15px;
  border-radius: 10px;
  border: 2px solid #ddd;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  position: relative;
}

.option input[type="radio"] {
  margin-right: 10px;
  margin-top: 0;
  vertical-align: middle;
  width: 18px;
  height: 18px;
}

.option label {
  flex: 1;
  cursor: pointer;
  display: flex;
  align-items: center;
  vertical-align: middle;
  font-size: 1.1em;
}

.option:hover {
  border-color: #4a4a8c;
  background-color: #f5f8ff;
  box-shadow: 0 3px 10px rgba(74, 74, 140, 0.1);
}

.option input[type="radio"] {
  margin-right: 10px;
}

.level-completion {
  background-color: #2ed573;
  color: white;
  padding: 30px;
  border-radius: 20px;
  margin: 20px 0;
  text-align: center;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
  margin-top: 40px;
}

.completion-icon {
  font-size: 48px;
  margin-bottom: 15px;
}

.completion-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.completion-message {
  margin-bottom: 20px;
}

.completion-badge-preview {
  background-color: rgba(255, 255, 255, 0.2);
  padding: 15px;
  border-radius: 10px;
  display: inline-block;
  margin-bottom: 15px;
}

.badge-icon {
  font-size: 24px;
  margin-bottom: 5px;
}

.level-actions {
  display: flex;
  gap: 20px;
  justify-content: center;
  margin-top: auto;
  padding-top: 30px;
  margin-top: 30px;
}

.prev-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.level-content h2 {
  color: #4a4a8c;
  margin-bottom: 20px;
  font-size: 2em;
  text-align: left;
}

.level-content p {
  color: #666;
  font-size: 1.1em;
  margin-bottom: 20px;
  text-align: left;
  max-width: 600px;
}

.level-actions {
  display: flex;
  gap: 20px;
  justify-content: center;
  margin-top: 30px;
}

.next-button,
.prev-button,
.finish-button {
  padding: 12px 30px;
  border: none;
  border-radius: 15px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1em;
}

.next-button {
  background-color: #4a4a8c;
  color: white;
}

.next-button:hover {
  background-color: #3a3a7c;
}

.prev-button {
  background-color: #ddd;
  color: #666;
}

.prev-button:hover {
  background-color: #ccc;
}

.finish-button {
  background-color: #ff6b6b;
  color: white;
}

.finish-button:hover {
  background-color: #ff5252;
}

.elderly-safety-system {
  margin-top: 30px;
}

.elderly-safety-header {
  margin-bottom: 20px;
}

.scene-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.scene-label {
  width: 250px;
  font-weight: bold;
  color: #333;
  text-align: left;
}

.input-field {
  flex: 1;
  padding: 12px 15px;
  border: 2px solid #ddd;
  border-radius: 8px;
  font-size: 1em;
  transition: border-color 0.3s ease;
  max-width: 400px;
}

.input-field:focus {
  outline: none;
  border-color: #4a4a8c;
  box-shadow: 0 0 0 3px rgba(74, 74, 140, 0.1);
}

.mt-4 {
  margin-top: 30px;
  margin-bottom: 20px;
  color: #4a4a8c;
  text-align: left;
}

.design-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 30px;
  background-color: #ffffff;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.design-table th {
  background-color: #4a4a8c;
  color: white;
  padding: 15px;
  text-align: left;
  font-weight: bold;
}

.design-table td {
  padding: 15px;
  border-bottom: 1px solid #eee;
  vertical-align: top;
}

.design-table tr:last-child td {
  border-bottom: none;
}

.design-table td:first-child {
  font-weight: bold;
  color: #4a4a8c;
}

.design-table td:nth-child(2) {
  font-weight: 500;
  color: #333;
}

.design-table td:nth-child(3) {
  min-width: 200px;
}

.design-table td:nth-child(4) {
  color: #666;
  font-style: italic;
}

.design-table td input.input-field {
  max-width: 100%;
  margin-bottom: 10px;
}

.design-table td input.input-field:last-child {
  margin-bottom: 0;
}

.check-item {
  display: flex;
  align-items: center;
  margin-top: 20px;
  padding: 20px;
  background-color: #fff3cd;
  border: 2px solid #ffeaa7;
  border-radius: 10px;
}

.check-item input[type="checkbox"] {
  width: 20px;
  height: 20px;
  margin-right: 15px;
  accent-color: #ff6b6b;
}

.check-item label {
  font-size: 1.1em;
  font-weight: bold;
  color: #856404;
  cursor: pointer;
}

.highlight-completion {
  transition: all 0.3s ease;
}

.highlight-warning {
  background-color: #fff3cd;
  border-color: #ffeaa7;
}

.check-item input[type="checkbox"]:checked + label {
  color: #27ae60;
}

.check-item input[type="checkbox"]:checked {
  accent-color: #27ae60;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .game-container {
    padding: 10px;
  }
  
  .game-content {
    padding: 20px;
  }
  
  .game-title {
    font-size: 1.8em;
  }
  
  .team-info {
    flex-direction: column;
    gap: 15px;
  }
  
  .level-labels {
    font-size: 0.8em;
  }
  
  .level-content {
    padding: 20px;
  }
  
  .level-content h2 {
    font-size: 1.5em;
  }
  
  .scene-item {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .scene-label {
    width: 100%;
    margin-bottom: 10px;
  }
  
  .design-table {
    font-size: 0.9em;
  }
  
  .design-table th,
  .design-table td {
    padding: 10px;
  }
}

/* 为指令输入框添加特殊样式 */
#cmd1,
#cmd2 {
  margin-right: 10px;
}

/* 为场景选择下拉框添加特殊样式 */
#scene-select {
  background-color: white;
  cursor: pointer;
}

/* 评价表样式 - 使星星与文字一起居中对齐 */
.evaluation-form {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
}

.rating-container {
  width: 100%;
  margin-bottom: 30px;
}

.rating-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  text-align: center;
  border: 2px solid #e0e0e0;
  border-radius: 10px;
  padding: 15px;
  background-color: #f9f9f9;
}

.rating-label {
  font-size: 1.1em;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
  text-align: center;
  width: 100%;
}

.rating-stars {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
}

.star {
  font-size: 2em;
  color: #ddd;
  cursor: pointer;
  transition: color 0.3s ease;
}

/* 嵌入内容样式 */
.embedded-content {
  margin: 20px 0;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
}

.embedded-content h4 {
  margin-top: 0;
  color: #333;
  font-size: 16px;
  margin-bottom: 10px;
}

.house-iframe {
  width: 100%;
  height: 800px;
  border-radius: 4px;
  margin-bottom: 10px;
}

.alternative-link {
  font-size: 14px;
  color: #666;
  text-align: center;
  margin-bottom: 0;
}

.alternative-link a {
  color: #1890ff;
  text-decoration: none;
}

.alternative-link a:hover {
  text-decoration: underline;
}

.star:hover,
.star.active {
  color: #ffc107;
}
</style>