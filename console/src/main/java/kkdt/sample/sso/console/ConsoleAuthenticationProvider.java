/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.console;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

import kkdt.sample.sso.core.AuthenticationInfo;
import kkdt.sample.sso.core.IAuthenticationService;
import kkdt.sample.sso.core.security.AuthInfoSecured;
import kkdt.sample.sso.core.security.AuthenticationInfoProvider;

/**
 * Specific to the console Spring Security configuration that will read in either
 * a JWE or JWS authentication token.
 * 
 * @author thinh ho
 *
 */
public class ConsoleAuthenticationProvider extends AuthenticationInfoProvider {
    
    /**
     * Uses the authentication API to authenticate users.
     * 
     * @param authenticationService
     */
    public ConsoleAuthenticationProvider(IAuthenticationService authenticationService) {
        super(authenticationService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String source = "";
        if(authentication instanceof JWEToken) {
            source = "jwe";
        } else if(authentication instanceof JWSToken) {
            source = "jws";
        }
        return authenticate(authentication, source);
    }
    
    private Authentication authenticate(Authentication authentication, String source) throws AuthenticationException {
        Authentication authenticated = null;
        AuthenticationInfo info = authenticationService
            .authenticate(authentication.getPrincipal().toString(), ("" + authentication.getCredentials()).toCharArray(), source);
        if(info.getSession() != null && info.getUserId() != null && !"".equals(info.getUserId())) {
            authenticated = new AuthInfoSecured(info.getUserId(),
                info.getIdToken(), 
                Stream.of(new SimpleGrantedAuthority("ADMIN")).collect(Collectors.toList()));
        } else {
            throw new UnauthorizedUserException("User is not valid");
        }
        return authenticated;
    }
}
