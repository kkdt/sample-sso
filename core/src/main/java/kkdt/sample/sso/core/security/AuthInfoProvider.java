/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import kkdt.sample.sso.core.AuthInfo;
import kkdt.sample.sso.core.AuthenticationService;

public class AuthInfoProvider implements AuthenticationProvider {
    
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthInfo info = authenticationService.authenticate(authentication.getPrincipal().toString(), authentication.getCredentials().toString().toCharArray());
        authentication.setAuthenticated(info.getSession() != null && info.getUserId() != null && !"".equals(info.getUserId()));
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
