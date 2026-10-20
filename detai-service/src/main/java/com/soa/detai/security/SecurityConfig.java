package com.soa.detai.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Secret Base64 dùng chung với auth-service (trong Docker lấy từ env JWT_SECRET)
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())                       // REST stateless -> tắt CSRF
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()

                        // GET danh sách + chi tiết đề tài: CÔNG KHAI
                        .requestMatchers(HttpMethod.GET, "/api/v1/detai", "/api/v1/detai/**").permitAll()

                        // 2 endpoint slot (Feign gọi khi đăng ký/hủy): chỉ cần ĐÃ ĐĂNG NHẬP
                        // ⚠️ PHẢI ĐỨNG TRƯỚC luật POST/PUT/DELETE ADMIN phía dưới
                        .requestMatchers(HttpMethod.POST, "/api/v1/detai/*/increment-slot", "/api/v1/detai/*/update-slot").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/detai/*/increment-slot", "/api/v1/detai/*/update-slot").authenticated()

                        // Tạo / Sửa / Xóa đề tài: chỉ ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/v1/detai", "/api/v1/detai/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/detai", "/api/v1/detai/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/detai", "/api/v1/detai/**").hasRole("ADMIN")

                        .anyRequest().authenticated())
                .oauth2ResourceServer(o -> o.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    // Dựng JwtDecoder từ secret của chính service này (HS256)
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    // Đọc claim "role" trong token -> quyền Spring "ROLE_..."
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String role = jwt.getClaimAsString("role");
            if (role == null || role.isBlank()) {
                return List.of();
            }
            return List.of(new SimpleGrantedAuthority("ROLE_" + role));
        });
        return converter;
    }
}