package com.epam.esm.config;

import com.epam.esm.security.jwt.JwtConfigurer;
import com.epam.esm.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_ENDPOINT = "/login/**";
    private static final String SIGN_UP_ENDPOINT = "/registration/**";
    private static final String REGISTRATION_ENDPOINT = "/users";
    private static final String ADD_ORDER_ENDPOINT = "/users/{id}/orders";
    private static final String ORDER_ENDPOINT = "/orders/**";
    private static final String USER_ENDPOINT = "/users/**";
    private static final String CERTIFICATE_ENDPOINT = "/certificates/**";
    private static final String TAG_ENDPOINT = "/tags/**";
    private static final String ROLE_ADMIN = "ADMIN";
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ENDPOINT, SIGN_UP_ENDPOINT).permitAll()
                .mvcMatchers(HttpMethod.GET, CERTIFICATE_ENDPOINT).permitAll()
                .mvcMatchers(HttpMethod.POST, REGISTRATION_ENDPOINT).permitAll()
                .mvcMatchers(HttpMethod.GET, USER_ENDPOINT).authenticated()
                .mvcMatchers(HttpMethod.PATCH, USER_ENDPOINT).authenticated()
                .mvcMatchers(HttpMethod.DELETE, USER_ENDPOINT).authenticated()
                .mvcMatchers(HttpMethod.GET, TAG_ENDPOINT).authenticated()
                .mvcMatchers(HttpMethod.GET, ORDER_ENDPOINT).authenticated()
                .mvcMatchers(HttpMethod.PATCH, ADD_ORDER_ENDPOINT).authenticated()
                .antMatchers(USER_ENDPOINT).hasRole(ROLE_ADMIN)
                .antMatchers(TAG_ENDPOINT).hasRole(ROLE_ADMIN)
                .antMatchers(CERTIFICATE_ENDPOINT).hasRole(ROLE_ADMIN)
                .antMatchers(ORDER_ENDPOINT).hasRole(ROLE_ADMIN)
                .mvcMatchers(HttpMethod.GET, CERTIFICATE_ENDPOINT).permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
