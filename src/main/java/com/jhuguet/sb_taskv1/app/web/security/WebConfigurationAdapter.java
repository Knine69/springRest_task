package com.jhuguet.sb_taskv1.app.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebConfigurationAdapter {

    private final SuccessHandler successHandler;

    @Autowired
    public WebConfigurationAdapter(SuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .antMatchers("/login")
                        .permitAll()
                        .antMatchers("/users/**", "/certificates/**", "/tags/**")
                        .hasRole("ADMIN")
                        .antMatchers("/certificates/**", "/tags/**")
                        .hasRole("USER")
                        .anyRequest()
                        .authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/signin")
                        .defaultSuccessUrl("/home")
                        .permitAll())
                .logout(logout -> logout.permitAll());
        return http.build();
    }

}
