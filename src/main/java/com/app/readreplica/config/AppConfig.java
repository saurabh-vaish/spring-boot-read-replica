package com.app.readreplica.config;

import com.app.readreplica.config.database.DataSourceContextPropagatingDecorator;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Executor;

/**
 * @Author saurabh vaish
 * @Date 23-07-2025
 */

@Log4j2
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Bean(name = "appTaskExecutor")
    public Executor taskExecutor(DataSourceContextPropagatingDecorator decorator) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int cores = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(cores);
        executor.setMaxPoolSize(cores * 2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsyncExec-");
        executor.setTaskDecorator(decorator);
        executor.initialize();
        return executor;
    }
}
