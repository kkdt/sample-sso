/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.console;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JWSToken extends UsernamePasswordAuthenticationToken {
    private static final long serialVersionUID = 2048223705904666491L;

    public JWSToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
