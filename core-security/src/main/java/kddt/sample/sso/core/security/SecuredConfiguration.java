/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kddt.sample.sso.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import kddt.sample.sso.core.security.jwt.JWEAuthenticationProcessingFilter;
import kddt.sample.sso.core.security.jwt.JWEAuthenticationProvider;
import kddt.sample.sso.core.security.jwt.JWSAuthenticationProcessingFilter;
import kddt.sample.sso.core.security.jwt.JWSAuthenticationProvider;
import kddt.sample.sso.core.security.jwt.JWTDetailsSource;
import kkdt.sample.sso.core.AuthenticationServiceLocator;
import kkdt.sample.sso.core.IAuthenticationService;
import kkdt.sample.sso.core.JWECrypto;

/**
 * Configuration class that can be shared for any application that wants to hook
 * up Spring Security. Defaults to built-in login form and logout functionality.
 * 
 * @author thinh ho
 *
 */
@Configuration
@Import(value=AuthenticationServiceLocator.class)
public class SecuredConfiguration extends WebSecurityConfigurerAdapter {
    
    @Value("${server.context-path}")
    private String contextPath;
    
    @Autowired
    private IAuthenticationService authenticationService;
    
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JWSAuthenticationProcessingFilter jwsFilter = 
            new JWSAuthenticationProcessingFilter("/jws", JWTDetailsSource.jwsURL, authenticationManager());
        
        JWEAuthenticationProcessingFilter jweFilter = 
            new JWEAuthenticationProcessingFilter("/jwe", JWTDetailsSource.jweURL, authenticationManager());
        
        JWEAuthenticationProcessingFilter ssoFilter = 
            new JWEAuthenticationProcessingFilter("/sso", JWTDetailsSource.jweCookie, authenticationManager());
        
        // https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#csrf
        
        http.exceptionHandling()
            .and()
                .addFilterAfter(jwsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jweFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(ssoFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
                .antMatchers("/public")
                .anonymous()
            .and().authorizeRequests()
                .antMatchers("/**")
                .hasAnyAuthority("ADMIN", "JWS", "JWE")
            // authenticate all other requests
            .anyRequest().authenticated()
            .and().formLogin()
                .defaultSuccessUrl(contextPath)
            .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .logoutSuccessUrl(contextPath)
            // additional authentication providers
            .and()
                .authenticationProvider(new AuthenticationInfoProvider(authenticationService))
                .authenticationProvider(new JWSAuthenticationProvider())
                .authenticationProvider(new JWEAuthenticationProvider(new JWECrypto()));
        
        // TODO: Spring Security and session management - Allow Spring Security to create sessions?
    }
}
