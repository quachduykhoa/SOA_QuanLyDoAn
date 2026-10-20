package com.soa.dangky.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Chuyển tiếp header Authorization từ HTTP request hiện tại (của người dùng)
 * sang các lệnh gọi Feign (sinhvien-service / detai-service).
 *
 * Vai trò: khi SV đăng ký đề tài, dangky-service gọi Feign
 * POST /api/v1/detai/{maDeTai}/increment-slot -> detai-service yêu cầu "đã đăng nhập"
 * nên Feign PHẢI mang theo đúng token của SV đó.
 */
@Configuration
public class FeignAuthRelayConfig {

    @Bean
    public RequestInterceptor authRelayInterceptor() {
        return template -> {
            var attrs = RequestContextHolder.getRequestAttributes();
            if (attrs instanceof ServletRequestAttributes servletAttrs) {
                String authHeader = servletAttrs.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && !authHeader.isBlank()) {
                    template.header(HttpHeaders.AUTHORIZATION, authHeader);
                }
            }
        };
    }
}