package com.fc8.config;

import com.fc8.resolver.CurrentMemberResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final CurrentMemberResolver currentMemberResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000", "https://aptner.site", "http://localhost:8080")
            .allowedMethods("GET", "POST", "PATCH", "DELETE", "HEAD", "OPTIONS", "PUT")
            .allowCredentials(true)
            .allowedHeaders("*")
            .exposedHeaders("Authorization")
            .maxAge(3000);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(currentMemberResolver);
    }
}
