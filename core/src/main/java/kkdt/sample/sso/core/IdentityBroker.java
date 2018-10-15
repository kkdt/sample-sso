/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core;

import kkdt.sample.sso.core.JWECrypto.Output;

/**
 * API for obtaining id_token from a designated identity provider. 
 * 
 * @author thinh ho
 *
 */
public interface IdentityBroker {
    /**
     * Broker out id_token to another <code>source</code>.
     * 
     * @param userId
     * @param source
     * @param o
     * @return
     * @throws Exception
     */
    String idToken(String userId, String source, Output o) throws Exception ;
    
    /**
     * Implementation can be an OpenID provider and generate their own tokens.
     * 
     * @param userId
     * @param o
     * @return
     * @throws Exception
     */
    String idToken(String userId, Output o) throws Exception;
    
    /**
     * Obtain or activate a session for the specified user - i.e. the user is
     * logged fully authenticated and holds an active session.
     * 
     * @param userId
     * @return
     */
    String session(String userId);
}
