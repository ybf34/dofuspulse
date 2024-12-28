package com.dofuspulse.api.security;

import com.dofuspulse.api.auth.CustomUserDetailsService;
import com.dofuspulse.api.auth.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private static final String[] WHITE_LIST_URLS = {
            "/api/v1/auth/**",
            "/error",
            "/"
    };

    private final UnauthorizedHandler unauthorizedHandler;
    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception{


                http.csrf(AbstractHttpConfigurer::disable)
                    .cors(AbstractHttpConfigurer::disable)
                    .sessionManagement( (session) -> {
//                        session.maximumSessions(1).maxSessionsPreventsLogin(true);
                        session.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::newSession);
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
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