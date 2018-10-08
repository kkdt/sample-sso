/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core;

import java.io.Serializable;
import java.util.UUID;

/**
 * The authentication object returned by the authentication API.
 * 
 * @author thinh ho
 *
 */
public class AuthenticationInfo implements Serializable {
    private static final long serialVersionUID = 6330519800236276221L;
    
    private String session;
    private String userId;
    private String email;
    private String idToken;

    public AuthenticationInfo() {
        this.session = UUID.randomUUID().toString();
    }
    
    public AuthenticationInfo(String userId) {
        this.session = UUID.randomUUID().toString();
        this.userId = userId;
        this.email = String.format("%s@email.com", userId);
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "AuthenticationInfo [session=" + session + ", userId=" + userId + ", email=" + email + "]";
    }
}
