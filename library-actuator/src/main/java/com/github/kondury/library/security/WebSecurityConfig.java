package com.github.kondury.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailService) throws Exception {

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )
                .addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/error",
                                "/css/**",
                                "js/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/",
                                "/books/list",
                                "/comments/list")
                        .hasAuthority("READ")
                        .requestMatchers(HttpMethod.GET, "/books/new")
                        .hasAuthority("CREATE")
                        .requestMatchers(HttpMethod.GET, "/books/*/edit")
                        .hasAuthority("UPDATE")
                        .requestMatchers(HttpMethod.GET, "/api/**")
                        .hasAuthority("READ")
                        .requestMatchers(HttpMethod.POST, "/api/**")
                        .hasAuthority("CREATE")
                        .requestMatchers(HttpMethod.PUT, "/api/**")
                        .hasAuthority("UPDATE")
                        .requestMatchers(HttpMethod.DELETE, "/api/**")
                        .hasAuthority("DELETE")
                        .anyRequest().denyAll()
                )
                .formLogin(withDefaults())
                .userDetailsService(userDetailService)
                .exceptionHandling(e -> e
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**")));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}