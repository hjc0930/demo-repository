package com.hjc.blog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 配置类
 * 用于配置 Knife4j 文档的标题、描述等信息
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Blog API 接口文档")
                        .version("1.0.0")
                        .description("个人博客系统 API 接口文档")
                        .contact(new Contact()
                                .name("Huang Jiacheng")
                                .email("hjc_0827@outlook.com")));
    }
}
