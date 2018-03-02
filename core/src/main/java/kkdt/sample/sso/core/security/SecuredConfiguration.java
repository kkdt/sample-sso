/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import kkdt.sample.sso.core.AuthenticationService;
import kkdt.sample.sso.core.AuthenticationServiceLocator;

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
    private AuthenticationService authenticationService;
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthInfoProvider(authenticationService);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#csrf
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and().formLogin()
            .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .logoutSuccessUrl(contextPath);
    }
}
