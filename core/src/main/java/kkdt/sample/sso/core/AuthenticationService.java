/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core;

public interface AuthenticationService {
    /**
     * Determine if the user is a registered user.
     * 
     * @param userId
     * @return
     */
    boolean validUser(String userId);
    
    /**
     * Authenticate the user credentials.
     * 
     * @param userId
     * @param password
     * @return
     */
    AuthInfo authenticate(String userId, char[] password);
}
