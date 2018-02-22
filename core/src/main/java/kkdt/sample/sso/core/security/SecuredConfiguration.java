/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import kkdt.sample.sso.core.AuthenticationService;
import kkdt.sample.sso.core.AuthenticationServiceLocator;

/**
 * Configuration class that can be shared for any application that wants to hook
 * up Spring Security.
 * 
 * @author thinh ho
 *
 */
@Configuration
@Import(value=AuthenticationServiceLocator.class)
public class SecuredConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthInfoProvider(authenticationService);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest().authenticated()
        .and()
            .formLogin();
    }
}
