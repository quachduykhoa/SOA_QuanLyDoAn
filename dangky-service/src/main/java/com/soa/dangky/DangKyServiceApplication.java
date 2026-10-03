package com.soa.dangky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DangKyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DangKyServiceApplication.class, args);
    }
}
