package com.dofuspulse.api.security;


import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer.SessionFixationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private static final String[] WHITE_LIST_URLS = {
      "/api/v1/auth/**",
      "/login/**",
      "/actuator/**",
      "/error"
  };

  private final UnauthorizedHandler unauthorizedHandler;
  private final CustomAccessDeniedHandler accessDeniedHandler;

  @Value("${app.FRONTEND_HOST}")
  private String frontendHost;

  @Bean
  public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .sessionManagement((s) -> {
          s.sessionFixation(SessionFixationConfigurer::newSession);
          s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        })
        .formLogin(AbstractHttpConfigurer::disable)
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(unauthorizedHandler)
            .accessDeniedHandler(accessDeniedHandler))
        .securityMatcher("/**")
        .logout(logout -> logout
            .logoutUrl("/api/v1/auth/logout")
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .invalidateHttpSession(true)
            .addLogoutHandler(new CookieClearingLogoutHandler("JSESSIONID"))
            .permitAll()
        )
        .authorizeHttpRequests(registry -> registry
            .requestMatchers(WHITE_LIST_URLS).permitAll()
            .requestMatchers("/api/v1/admin").hasRole("ADMIN")
            .anyRequest().authenticated());

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowCredentials(true);
    config.setAllowedOrigins(Collections.singletonList(frontendHost));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    config.setAllowedHeaders(Collections.singletonList("*"));
    config.setMaxAge(3600L);

    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

}