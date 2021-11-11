package com.jcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author jiaxm
 * @date 2021/11/11
 */
@SpringBootApplication
public class JCloudApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(JCloudApplication.class, args);
    }
}
