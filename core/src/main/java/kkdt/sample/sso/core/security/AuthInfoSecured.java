/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AuthInfoSecured extends UsernamePasswordAuthenticationToken {
    private static final long serialVersionUID = 4485996414524468337L;
    
    public AuthInfoSecured(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
    
    public AuthInfoSecured(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
