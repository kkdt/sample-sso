/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.authserver;

import java.rmi.Remote;

import org.apache.log4j.Logger;

import kkdt.sample.sso.core.AuthenticationInfo;
import kkdt.sample.sso.core.IAuthenticationService;
import kkdt.sample.sso.core.IdentityBroker;

/**
 * Only recognize a single user identifier - implementation is an RMI interface.
 * 
 * @author thinh ho
 *
 */
public class AuthenticationService implements IAuthenticationService, Remote {
    private static final Logger logger = Logger.getLogger(AuthenticationService.class);
    
    private final String validUser;
    private final IdentityBroker identityBroker;
    
    public AuthenticationService(String userId, IdentityBroker identityBroker) {
        this.validUser = userId;
        this.identityBroker = identityBroker;
    }
    
    @Override
    public AuthenticationInfo authenticate(String userId, char[] password, String source) {
        AuthenticationInfo auth = new AuthenticationInfo(userId);
        if(!validUser.equals(userId)) {
            logger.warn("Authenticating " + userId + " is not valid");
            auth.setSession(null);
        } else {
            String idToken = null;
            try {
                idToken = identityBroker.generateIdToken(auth, source);
                logger.info(String.format("Generated IDToken from %s: %s", source, idToken));
            } catch (Exception e) {
                logger.error("Cannot generate id_token", e);
            } 
            auth.setIdToken(idToken);
        }
        return auth;
    }
}
