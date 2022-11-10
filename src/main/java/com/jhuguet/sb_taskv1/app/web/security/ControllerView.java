package com.jhuguet.sb_taskv1.app.web.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ControllerView implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry
                .addViewController("/home")
                .setViewName("home");
        registry
                .addViewController("/")
                .setViewName("home");
        registry
                .addViewController("/login")
                .setViewName("login");
        registry
                .addViewController("/signup")
                .setViewName("signup");
        registry
                .addViewController("/orders")
                .setViewName("orders");
        registry
                .addViewController("/neworder")
                .setViewName("neworder");
    }

}
