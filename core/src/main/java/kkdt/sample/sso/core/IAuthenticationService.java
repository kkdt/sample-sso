/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core;

/**
 * Authentication service API for username/password.
 * 
 * @author thinh ho
 *
 */
public interface IAuthenticationService {
    /**
     * Authenticate the user credentials.
     * 
     * @param userId
     * @param password
     * @param output jws or jwe.
     * @return
     */
    AuthenticationInfo authenticate(String userId, char[] password, String output);
    
    String idToken(String request);
    
    String accessToken(String request);
}
