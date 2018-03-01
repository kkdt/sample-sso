/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.console;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * A logout has occurred.
 * 
 * @author thinh ho
 *
 */
public class ConsoleLogoutEvent extends AbstractAuthenticationEvent {
    private static final long serialVersionUID = 473154715945937738L;
    
    public ConsoleLogoutEvent() {
        super(new Authentication() {
            private static final long serialVersionUID = 4611382770049072831L;

            @Override
            public String getName() {
                return "";
            }
            
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            }
            
            @Override
            public boolean isAuthenticated() {
                return false;
            }
            
            @Override
            public Object getPrincipal() {
                return "";
            }
            
            @Override
            public Object getDetails() {
                return "";
            }
            
            @Override
            public Object getCredentials() {
                return "";
            }
            
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.emptyList();
            }
        });
    }
}
