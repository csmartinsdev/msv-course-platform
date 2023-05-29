package br.com.smartinsoft.coursesplatform.config.security;

import br.com.smartinsoft.coursesplatform.config.filter.AuthenticationUserRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

  private AuthenticationUserRequestFilter authenticationUserRequestFilter;

  @Autowired
  public SecurityConfiguration(AuthenticationUserRequestFilter authenticationUserRequestFilter) {
    this.authenticationUserRequestFilter = authenticationUserRequestFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/**.html",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/actuator/**",
            "/**/auth/**",
            "/**/user/owner/**").permitAll()
        .anyRequest().authenticated()
        .and().httpBasic()
        .and().csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().cors()
        .and().addFilterBefore(authenticationUserRequestFilter, BasicAuthenticationFilter.class)
        .exceptionHandling()
        .authenticationEntryPoint((request, response, exception) ->
            response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value(), exception.getMessage()));

    return http.build();
  }


  @Bean
  public WebMvcConfigurer corsConfigurer() {
     return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedHeaders("*")
            .allowedMethods("GET","PUT", "POST", "PATCH", "DELETE", "OPTIONS");
      }
    };
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
    return authConfiguration.getAuthenticationManager();
  }

  @Bean
  public UserDetailsManager authenticateUsers() {
    return new InMemoryUserDetailsManager();
  }

  @Override
  public MethodSecurityExpressionHandler createExpressionHandler() {
    return new CustomMethodSecurityExpressionHandler();
  }
}
