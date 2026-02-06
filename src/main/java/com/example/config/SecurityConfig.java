package com.example.config;

import com.example.config.shopping.CustomAuthenticationEntryPoint;
import com.example.service.ShoppingMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    @Autowired
    ShoppingMemberService shoppingMemberService;

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
                // H2 콘솔은 <frame> 태그를 사용하므로 이 설정이 없으면 화면이 깨집니다.
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                .authorizeHttpRequests(auth -> auth

                        // /admin으로 시작하는 경로는 해당 계정이 ADMIN role일 경우에만 접근 가능하도록 설정합니다.
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 1. 보안 검사 제외 항목들
                        .requestMatchers(
                                "/", "/main", "/login", "/error",           // 기본 페이지
                                "/shoppingMembers/**", "/shoppingItem/**",
                                "/.well-known/**", "/favicon.ico",
                                "/swagger-ui/**", "/v3/api-docs/**", // // Swagger 관련 주소는 누구나 접근 가능하게 허용
                                "/h2-console/**",                 // H2 DB 관리 화면
                                "/css/**", "/js/**", "/images/**"  // 정적 리소스 (Thymeleaf용)
                        ).permitAll()

                        // 2. 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // 3. 폼 로그인 설정
                .formLogin(form -> form
                        .loginPage("/shoppingMembers/login")
                        // 이 즈음에서 ShoppingMemberService.loadUserByUsername()메소드를 통해 정보 ID와 PW 비교
                        .defaultSuccessUrl("/main")
                        .usernameParameter("email")
                        .failureUrl("/shoppingMembers/login/error")
                        .permitAll()
                )

                // 4. OAuth2 로그인 사용 시 추가 (구글, 네이버 등)
//                .oauth2Login(oauth2 -> oauth2
//                        .defaultSuccessUrl("/")
//                )

                // 5. 로그아웃 설정
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/shoppingMembers/logout"))
                        .logoutSuccessUrl("/main")
                        .deleteCookies("JSESSIONID") // 로그아웃 시 쿠키 삭제 강제
                        .invalidateHttpSession(true)
                )

                // 유효하지 않은 쿠키를 들고 왔을 때 즉시 세션을 새로 고치도록 유도할 수 있습니다.
                .sessionManagement(session -> session
                        .invalidSessionUrl("/shoppingMembers/login") // 세션이 유효하지 않을 때 보낼 주소
                        .maximumSessions(1) // 중복 로그인 방지
                )

                // 인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러를 등록합니다.
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        return http.build();
    }

    /**
     * 비밀번호를 데이터베이스에 그대로 저장했을 경우, 데이터베이스가 해킹당하면 고객의 회원 정보가 그대로 노출됩니다.
     * 이를 해결하기 위해 BCryptPasswordEncoder의 해시 함수를 이용하여 비밀번호를 암호화하여 저장합니다.
     * BCryptPasswordEncoder를 빈으로 등록하여 사용하겠습니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(shoppingMemberService)
//                .passwordEncoder(passwordEncoder());
//    }
}
