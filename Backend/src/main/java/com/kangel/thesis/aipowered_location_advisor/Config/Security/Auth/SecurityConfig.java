package com.kangel.thesis.aipowered_location_advisor.Config.Security.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kangel.thesis.aipowered_location_advisor.Config.Security.CorsConfig;
import com.kangel.thesis.aipowered_location_advisor.Services.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final CorsConfig corsConfig;
    private final UserService userDetailService;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/auth/authenticated").authenticated()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/verify/**").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }

    // Used for custom secure login
    @Bean
    public AuthenticationProvider provider() {
        DaoAuthenticationProvider dprovider = new DaoAuthenticationProvider();
        dprovider.setPasswordEncoder(new HashingConfig().PasswordEncoder());
        dprovider.setUserDetailsService(userDetailService);
        return dprovider;
    }

    @Bean
    public AuthenticationManager manager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
