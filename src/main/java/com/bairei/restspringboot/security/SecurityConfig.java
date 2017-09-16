package com.bairei.restspringboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig  extends WebSecurityConfigurerAdapter {


    private BCryptPasswordEncoder passwordEncoder;
    private UserDetailsService service;
    private AuthEntryPoint entryPoint;
    private AuthFailureHandler failureHandler;
    private AuthSuccessHandler successHandler;

    @Autowired
    public SecurityConfig (@Lazy BCryptPasswordEncoder passwordEncoder, UserDetailsService service,
                           AuthEntryPoint entryPoint, AuthFailureHandler failureHandler,
                           AuthSuccessHandler successHandler){
        this.passwordEncoder = passwordEncoder;
        this.service = service;
        this.entryPoint = entryPoint;
        this.failureHandler = failureHandler;
        this.successHandler = successHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().and()
                .authorizeRequests()
                .antMatchers("/patient*/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/doctor").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/doctor").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/visit").authenticated()
                .antMatchers(HttpMethod.PATCH,"/visit").authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and()
                .formLogin().successHandler(successHandler).failureHandler(failureHandler)
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure (AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(passwordEncoder);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
