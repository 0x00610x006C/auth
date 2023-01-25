package com.scltools.auth.config;

import com.scltools.auth.inteceptor.AutorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
    private final AutorizationInterceptor autorizationInterceptor;

    public WebMvcConfig(AutorizationInterceptor autorizationInterceptor)
    {
        this.autorizationInterceptor = autorizationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(autorizationInterceptor)
                .addPathPatterns("/api/user");
    }
}
