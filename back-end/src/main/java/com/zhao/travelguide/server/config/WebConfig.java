package com.zhao.travelguide.server.config;

import com.zhao.travelguide.server.interceptor.CorsInterceptor;
import com.zhao.travelguide.server.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private JwtInterceptor jwtInterceptor;
    @Autowired
    private CorsInterceptor corsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor)
                .addPathPatterns("/**");
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/hello")
                .excludePathPatterns("/auth/login")
                .excludePathPatterns("/auth/register");
//                .excludePathPatterns("/guides")
//                .excludePathPatterns("/guides/{id}")
//                .excludePathPatterns("/guides/{id}/comments");
    }
}
