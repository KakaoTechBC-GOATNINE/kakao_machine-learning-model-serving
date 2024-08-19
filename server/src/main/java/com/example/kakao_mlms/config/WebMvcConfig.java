package com.example.kakao_mlms.config;

import com.example.kakao_mlms.constant.Constants;
import com.example.kakao_mlms.intercepter.UserIdArgumentResolver;
import com.example.kakao_mlms.intercepter.UserIdInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserIdArgumentResolver userIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        //기존 resolver 들 등록
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);

        //userId resolver 등록
        resolvers.add(this.userIdArgumentResolver);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        //Interceptor 추가
        registry.addInterceptor(new UserIdInterceptor())
                .addPathPatterns("/**") //적용하는 경로 추가 (모든 경로)
                .excludePathPatterns(Constants.NO_NEED_AUTH_URLS); //제외하는 경로 등록
    }
}
