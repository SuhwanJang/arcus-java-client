package com.jam2in.arcus.admin.tool.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jam2in.arcus.admin.tool.bean.JwtAccessDeniedHandler;
import com.jam2in.arcus.admin.tool.bean.JwtAuthenticationEntryPoint;
import com.jam2in.arcus.admin.tool.bean.JwtTokenProvider;
import com.jam2in.arcus.admin.tool.bean.UserDetailsService;
import com.jam2in.arcus.admin.tool.bean.UserPasswordEncoder;
import com.jam2in.arcus.admin.tool.domain.user.repository.UserRepository;
import com.jam2in.arcus.admin.tool.filter.JwtAuthenticationFilter;
import com.jam2in.arcus.admin.tool.filter.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final ObjectMapper objectMapper;

  private final UserRepository userRepository;

  public SecurityConfiguration(ObjectMapper objectMapper,
                               UserRepository userRepository) {
    this.objectMapper = objectMapper;
    this.userRepository = userRepository;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable();

    http.httpBasic()
        .disable();

    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.headers()
        .frameOptions()
        .sameOrigin();

    http.authorizeRequests()
        .antMatchers(HttpMethod.POST,
            "/api/*/users", "/login").permitAll()
        //.antMatchers("/api/**").authenticated()
        .anyRequest().permitAll();

    http.exceptionHandling()
        .accessDeniedHandler(jwtAccessDeniedHandler())
        .authenticationEntryPoint(jwtAuthenticationEntryPoint());

    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
        authenticationManager(), jwtTokenProvider(), objectMapper);
    jwtAuthenticationFilter.setFilterProcessesUrl("/login");

    JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(
        authenticationManager(), jwtTokenProvider());

    http.addFilter(jwtAuthenticationFilter)
        .addFilter(jwtAuthorizationFilter);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService())
        .passwordEncoder(passwordEncoder());
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsService(userRepository);
  }

  @Bean
  public JwtAccessDeniedHandler jwtAccessDeniedHandler() {
    return new JwtAccessDeniedHandler(objectMapper);
  }

  @Bean
  public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
    return new JwtAuthenticationEntryPoint(objectMapper);
  }

  @Bean
  public JwtTokenProvider jwtTokenProvider() {
    return new JwtTokenProvider();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new UserPasswordEncoder();
  }

}
