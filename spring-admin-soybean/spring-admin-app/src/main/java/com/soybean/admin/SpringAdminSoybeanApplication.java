package com.soybean.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Admin Soybean 主应用程序
 */
@SpringBootApplication(scanBasePackages = "com.soybean.admin")
public class SpringAdminSoybeanApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAdminSoybeanApplication.class, args);
    }
}
