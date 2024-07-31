package com.example.kakao_mlms.security.config;

import com.example.kakao_mlms.constant.Constants;
import com.example.kakao_mlms.domain.type.ERole;
import com.example.kakao_mlms.security.CustomAuthenticationProvider;
import com.example.kakao_mlms.security.JwtAuthEntryPoint;
import com.example.kakao_mlms.security.filter.JwtExceptionFilter;
import com.example.kakao_mlms.security.filter.JwtFilter;
import com.example.kakao_mlms.security.handler.JwtAccessDeniedHandler;
import com.example.kakao_mlms.security.handler.signin.OAuth2LoginFailureHandler;
import com.example.kakao_mlms.security.handler.signin.OAuth2LoginSuccessHandler;
import com.example.kakao_mlms.security.handler.signout.CustomSignOutProcessHandler;
import com.example.kakao_mlms.security.handler.signout.CustomSignOutResultHandler;
import com.example.kakao_mlms.security.service.CustomOAuth2UserService;
import com.example.kakao_mlms.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
//    private final DefaultSignInSuccessHandler defaultSignInSuccessHandler;
//    private final DefaultSignInFailureHandler defaultSignInFailureHandler;
//    private final CustomUserDetailService customUserDetailService;
//    private final BasicAuthenticationProvider basicAuthenticationProvider;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtUtil jwtUtil;
    private final CustomSignOutProcessHandler customSignOutProcessHandler;
    private final CustomSignOutResultHandler customSignOutResultHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationProvider customAuthenticationProvider;


    @Bean
    protected SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) //보호 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 HTTP 기본 인증 비활성화
                .sessionManagement((sessionManagement) -> //상태를 유지하지 않는 세션 정책 설정
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                        .requestMatchers(Constants.NO_NEED_AUTH_URLS).permitAll()
                        .requestMatchers("/api/v1/admins/**").hasRole(ERole.ADMIN.name())
                        .anyRequest().authenticated())
                //폼 기반 로그인 설정
//                .formLogin(configurer ->
//                        configurer
//                                .loginPage("/login")
//                                .loginProcessingUrl("/sign-in") //로그인 처리 URL (POST)
//                                .usernameParameter("serialId") //사용자 아이디 파라미터 이름
//                                .passwordParameter("password") //비밀번호 파라미터 이름
//                                .successHandler(defaultSignInSuccessHandler) //로그인 성공 핸들러
//                                .failureHandler(defaultSignInFailureHandler) // 로그인 실패 핸들러
//                )//.userDetailsService(customUserDetailService) //사용자 검색할 서비스 설정
                //소셜 로그인
                .oauth2Login(configurer ->
                        configurer
                                .successHandler(oAuth2LoginSuccessHandler)
                                .failureHandler(oAuth2LoginFailureHandler)
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint.userService(customOAuth2UserService)
                                )
                )
                // 로그아웃 설정
                .logout(configurer ->
                        configurer
                                .logoutUrl("/auth/logout")
                                .addLogoutHandler(customSignOutProcessHandler)
                                .logoutSuccessHandler(customSignOutResultHandler)
                                .deleteCookies("JSESSIONID")
                )

                //예외 처리 설정
                .exceptionHandling(configurer ->
                        configurer
                                .authenticationEntryPoint(jwtAuthEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .addFilterBefore(new JwtFilter(jwtUtil, customAuthenticationProvider), LogoutFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtFilter.class)

                //SecurityFilterChain 빈을 반환
                .getOrBuild();
    }
}
