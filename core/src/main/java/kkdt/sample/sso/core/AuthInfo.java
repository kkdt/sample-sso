/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core;

import java.io.Serializable;
import java.util.UUID;

public class AuthInfo implements Serializable {
    private static final long serialVersionUID = 6330519800236276221L;
    
    private String session;
    private String userId;

    public AuthInfo() {
        this.session = UUID.randomUUID().toString();
    }
    
    public AuthInfo(String userId) {
        this.session = UUID.randomUUID().toString();
        this.userId = userId;
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
        return "AuthInfo [session=" + session + ", userId=" + userId + "]";
    }
}
