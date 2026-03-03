package com.soybean.admin.mail.config;

import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;

/**
 * 邮件配置
 */
@Configuration
public class MailConfiguration {

    /**
     * FreeMarker配置
     */
    @Bean
    public freemarker.template.Configuration freemarkerConfiguration() {
        FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();
        factory.setTemplateLoaderPath("classpath:/templates/mail/");
        factory.setDefaultEncoding("UTF-8");

        freemarker.template.Configuration configuration = null;
        try {
            configuration = factory.createConfiguration();
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setLogTemplateExceptions(false);
            configuration.setWrapUncheckedExceptions(true);
            configuration.setFallbackOnNullLoopVariable(false);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create FreeMarker configuration", e);
        }

        return configuration;
    }

    /**
     * 异步配置
     */
    @Bean
    public org.springframework.scheduling.annotation.AsyncConfigurer asyncConfigurer() {
        return new org.springframework.scheduling.annotation.AsyncConfigurer() {
            @Override
            public org.springframework.core.task.TaskExecutor getAsyncExecutor() {
                return new org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor() {{
                    setCorePoolSize(5);
                    setMaxPoolSize(10);
                    setQueueCapacity(100);
                    setThreadNamePrefix("mail-sender-");
                    initialize();
                }};
            }
        };
    }
}
