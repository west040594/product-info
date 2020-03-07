package com.tstu.productinfo.config;

import com.tstu.commons.mapping.DateMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class ApplicationConfig {
    @Bean
    public DateMapper dateMapper() {
        return new DateMapper();
    }
}
