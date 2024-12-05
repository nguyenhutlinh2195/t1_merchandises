package com.cbjs.util.config.security;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.cbjs.entity.RoleEnum;
import com.cbjs.service.CustomUserDetailsService;

import java.util.Arrays;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    private final CustomUserDetailsService userDetailsService;

    @Value("${ORIGIN:http://localhost}")
    private String appOrigin;

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
            .cors(httpSecurityCorsConfigurer -> corsConfigurationSource())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(r -> r
                    .requestMatchers("/error", "/v1/auth/**", "/v1/images/**", "/swagger-ui/**", "/api-docs/**").permitAll()
                    .requestMatchers("/v1/admin/**").hasAuthority(RoleEnum.ADMIN.name())
                    .requestMatchers(HttpMethod.POST,"/v1/merchs/**", "/v1/categories/**", "/v1/users/**", "/v1/images/**").hasAuthority(RoleEnum.ADMIN.name())
                    .requestMatchers(HttpMethod.PUT, "/v1/merchs/**", "/v1/categories/**", "/v1/users/**").hasAuthority(RoleEnum.ADMIN.name())
                    .requestMatchers(HttpMethod.DELETE, "/v1/merchs/**", "/v1/categories/**", "/v1/users/**").hasAuthority(RoleEnum.ADMIN.name())
                    .anyRequest().authenticated())
            .userDetailsService(userDetailsService)
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable());

    return httpSecurity.build();
}
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }}
