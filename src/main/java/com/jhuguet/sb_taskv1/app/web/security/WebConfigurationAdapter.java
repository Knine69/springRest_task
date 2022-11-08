package com.jhuguet.sb_taskv1.app.web.security;

import com.jhuguet.sb_taskv1.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebConfigurationAdapter {

    private final UserRepository userRepository;

    @Autowired
    public WebConfigurationAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .antMatchers("/home/**")
                        .authenticated()
                        .anyRequest())
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .successForwardUrl("/home"))
                .logout(logout -> logout.permitAll());
        return http.build();
    }

}
