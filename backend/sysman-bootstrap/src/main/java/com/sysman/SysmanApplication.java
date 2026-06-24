package com.sysman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sysman")
public class SysmanApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysmanApplication.class, args);
    }
}
