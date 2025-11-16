package com.ping.voicelinkguardianbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ping.voicelinkguardianbackend.mapper")
public class VoiceLinkGuardianBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoiceLinkGuardianBackendApplication.class, args);
    }

}
