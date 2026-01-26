package com.example.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * 나중에 로그인을 실제로 구현하실 계획이라면, 다른 곳은 막아두더라도 Swagger 관련 주소만 보안 검사에서 제외하도록 설정해야 합니다.
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 테스트 편의를 위해 CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        // Swagger 관련 주소는 누구나 접근 가능하게 허용
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // 그 외 모든 요청은 로그인 필요
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.defaultSuccessUrl("/swagger-ui/index.html")); // 로그인 성공 시 Swagger로 이동

        return http.build();
    }
}
