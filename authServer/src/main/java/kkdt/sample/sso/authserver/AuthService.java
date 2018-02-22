/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.authserver;

import java.rmi.Remote;

import org.apache.log4j.Logger;

import kkdt.sample.sso.core.AuthInfo;
import kkdt.sample.sso.core.AuthenticationService;

/**
 * Only recognize a single user identifier - implementation is an RMI interface.
 * 
 * @author thinh ho
 *
 */
public class AuthService implements AuthenticationService, Remote {
    private static final Logger logger = Logger.getLogger(AuthService.class);
    
    private String validUser;
    
    public AuthService(String userId) {
        this.validUser = userId;
    }
    
    @Override
    public boolean validUser(String userId) {
        return validUser.equals(userId);
    }

    @Override
    public AuthInfo authenticate(String userId, char[] password) {
        AuthInfo auth = new AuthInfo(userId);
        if(!validUser(userId)) {
            logger.warn("Authenticating " + userId + " is not valid");
            auth.setSession(null);
        }
        return auth;
    }

}
