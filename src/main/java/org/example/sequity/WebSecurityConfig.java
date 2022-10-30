package org.example.sequity;

import lombok.RequiredArgsConstructor;
import org.example.sequity.filter.JWTAuthenticationFilter;
import org.example.sequity.filter.JWTAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig{

    private final JpaUserDetailsService service;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(service)
                .passwordEncoder(passwordEncoder()).and().build();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/user").permitAll()
                .and().authorizeRequests().antMatchers(HttpMethod.POST, "/author", "/genre", "/book").hasRole("ADMIN")
                .and().authorizeRequests().antMatchers(HttpMethod.DELETE, "/author/*", "/genre/*", "/book/*").hasRole("ADMIN")
                .and().authorizeRequests().antMatchers(HttpMethod.GET, "/comment/","/user/**").hasRole("ADMIN")
                .and().authorizeRequests().anyRequest().hasAnyRole("READER", "ADMIN")
                .and().addFilter(new JWTAuthenticationFilter(authenticationManager(http), service))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(http), service))
                .formLogin(Customizer.withDefaults()).build();
    }

}
