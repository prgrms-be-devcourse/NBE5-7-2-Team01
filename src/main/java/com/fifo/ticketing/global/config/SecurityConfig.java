package com.fifo.ticketing.global.config;

import com.fifo.ticketing.domain.user.service.FormLoginSuccessHandler;
import com.fifo.ticketing.domain.user.service.OAuth2LoginFailureHandler;
import com.fifo.ticketing.domain.user.service.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final FormLoginSuccessHandler formLoginSuccessHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(form -> {
                form.loginPage("/users/signin")
                    .usernameParameter("email")
                    .passwordParameter("loginPwd")
                    .successHandler(formLoginSuccessHandler)
                    .failureUrl("/users/signin?error=true")
                    .permitAll();
            })
            .logout(logout -> {
                logout.logoutUrl("/signout")
                    .logoutSuccessUrl("/users/signin")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .permitAll();
            })
            .oauth2Login(oauth -> {
                oauth.loginPage("/oauth/login")
                    .successHandler(oAuth2LoginSuccessHandler)
                    .failureHandler(oAuth2LoginFailureHandler);
            })
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/index", "/", "/api/**")
                    .permitAll()
                    .requestMatchers("/users/signin", "/users/login", "/users/signup",
                        "/oauth/login")
                    .anonymous()
                    .requestMatchers("/user/**")
                    .hasAnyAuthority("USER", "ADMIN")
                    .requestMatchers("/admin/**")
                    .hasAnyAuthority("ADMIN")
                    .anyRequest()
                    .permitAll();
            })
            .build();
    }
}

