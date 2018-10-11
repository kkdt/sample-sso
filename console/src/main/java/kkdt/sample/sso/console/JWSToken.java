/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.console;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Console-only authentication token to represent a JWS.
 * 
 * @author thinh ho
 *
 */
public class JWSToken extends UsernamePasswordAuthenticationToken {
    private static final long serialVersionUID = 2048223705904666491L;

    /**
     * 
     * @param principal the user name.
     * @param credentials the password.
     */
    public JWSToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
