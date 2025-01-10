package com.dofuspulse.api.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private static final String[] WHITE_LIST_URLS = {
      "/api/v1/auth/**",
      "/error",
      "/api/v1/**"
  };

  private final UnauthorizedHandler unauthorizedHandler;

  @Bean
  public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .sessionManagement((s) -> {
          s.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::newSession);
          s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        })
        .formLogin(AbstractHttpConfigurer::disable)
        .exceptionHandling(c -> c.authenticationEntryPoint(unauthorizedHandler))
        .securityMatcher("/**")
        .logout(logout -> logout
            .logoutUrl("/api/v1/auth/logout")
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll()
        )
        .authorizeHttpRequests(registry -> registry
            .requestMatchers(WHITE_LIST_URLS).permitAll()
            .requestMatchers("/admin").hasRole("ADMIN")
            .anyRequest().authenticated());

    return http.build();
  }
}