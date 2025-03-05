package com.dofuspulse.api.security;

import com.dofuspulse.api.auth.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class OAuth2Config {

  private final CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public SecurityFilterChain OAuth2SecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/oauth2/**", "/login/oauth2/code/**")
        .oauth2Login(oauth2Login ->
            oauth2Login
                .userInfoEndpoint(userInfoEndpoint ->
                    userInfoEndpoint
                        .userService(customOAuth2UserService)
                )
        );
    return http.build();
  }
}
