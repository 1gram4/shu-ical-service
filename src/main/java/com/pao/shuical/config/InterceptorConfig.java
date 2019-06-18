package com.pao.shuical.config;

import com.pao.shuical.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/api/v1/login")
                .excludePathPatterns("/messagePage")
                .excludePathPatterns("/static/**") //排除访问static静态文件夹下的静态资源文件
                .excludePathPatterns("/loginPage")
        ;
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}
