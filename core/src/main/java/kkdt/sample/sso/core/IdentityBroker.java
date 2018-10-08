/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core;

/**
 * API for obtaining id_token from a designated identity provider. 
 * 
 * @author thinh ho
 *
 */
public interface IdentityBroker {
    /**
     * Generate an id_token from an authentication.
     * 
     * @param authentication
     * @param source the identity provider identifier.
     * @return
     */
    String generateIdToken(AuthenticationInfo authentication, String source);
}
