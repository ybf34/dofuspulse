package com.dofuspulse.api.security;


import lombok.RequiredArgsConstructor;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private static final String[] WHITE_LIST_URLS = {
      "/api/v1/auth/**",
      "/login/**",
      "/error"
  };

  private final UnauthorizedHandler unauthorizedHandler;
  private final CustomAccessDeniedHandler accessDeniedHandler;

  @Bean
  public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
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
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

}