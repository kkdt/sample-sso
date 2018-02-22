/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core.security;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import kkdt.sample.sso.core.AuthInfo;
import kkdt.sample.sso.core.AuthenticationService;

/**
 * Spring Security authentication provider that uses the {@linkplain AuthenticationService}
 * for authenticating users.
 * 
 * @author thinh ho
 *
 */
public class AuthInfoProvider implements AuthenticationProvider {
    
    private final AuthenticationService authenticationService;
    
    public AuthInfoProvider(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authenticated = null;
        AuthInfo info = authenticationService.authenticate(authentication.getPrincipal().toString(), ("" + authentication.getCredentials()).toCharArray());
        if(info.getSession() != null && info.getUserId() != null && !"".equals(info.getUserId())) {
            authenticated = new AuthInfoSecured(info.getUserId(),
                info.getSession(), 
                Stream.of(new SimpleGrantedAuthority("ADMIN")).collect(Collectors.toList()));
        } else {
            throw new BadCredentialsException("Unauthorized: " + info.getUserId());
        }
        return authenticated;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
