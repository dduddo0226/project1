package com.ss.batch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // 수정, 생성 기록을 자동으로 실행할 수 있도록 기능을 켜는 역할
@Configuration
public class JpaConfig {

}
