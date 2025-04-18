package com.mdemydovych.intro.introspring.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http
        .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers(HttpMethod.GET, "/webjars/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
  }

  @Bean
  public JwtAuthenticationConverter customJwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(
        source -> ((ArrayList<Map<String, String>>) source.getClaim("authorities")).stream()
            .map(roleMap -> new SimpleGrantedAuthority(roleMap.get("role"))).collect(
                Collectors.toCollection(ArrayList::new)));
    return converter;
  }
}