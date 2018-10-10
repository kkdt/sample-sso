/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core.security.jwt;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Authentication request that wraps an id_token - JWE (encryption).
 * 
 * @author thinh ho
 *
 */
public class JWEAuthenticationRequest extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 4696506003551444240L;
    
    private final String idToken;
    private String username;
    
    public JWEAuthenticationRequest(String idToken) 
    {
        super(Stream.of(new SimpleGrantedAuthority("JWE")).collect(Collectors.toList()));
        this.idToken = idToken;
    }
    
    @Override
    public Object getCredentials() {
        return idToken;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

}
