/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.console;

import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The console security authentication manager. It listens for authentication 
 * events published by the underlying {@linkplain AuthenticationManager} on
 * login attempts. In addition, this object also publishes custom logout events
 * to itself so that the same logic is used to set the {@linkplain SecurityContextHolder}
 * for the entire console application.
 * 
 * <p>
 * Must be configured within an {@linkplain ApplicationContext} as a bean so that
 * authentication events can be published/processed by the appropriate listeners.
 * </p>
 * 
 * @author thinh ho
 *
 */
public class GlobalSecurityContextAuthenticationManager 
    implements ApplicationContextAware, ApplicationListener<AbstractAuthenticationEvent> 
{
    private static final Logger logger = Logger.getLogger(GlobalSecurityContextAuthenticationManager.class);
    
    private final AuthenticationManager authenticationManager;
    private ApplicationContext applicationContext;
    
    public GlobalSecurityContextAuthenticationManager(AuthenticationManager authenticationManager) {
        Objects.requireNonNull(authenticationManager, "Requires an Authentication Manager");
        this.authenticationManager = authenticationManager;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    /**
     * Authenticate the user name.
     * 
     * @param username
     * @return
     */
    public Authentication login(String username) {
        return authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(username, ""));
    }
    
    /**
     * Logout of the application and publish the event.
     */
    public void logout() {
        final Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Logging out: " + currentAuth);
        applicationContext.publishEvent(new ConsoleLogoutEvent());
    }

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        logger.info("Received authentication event: " + event);
        SecurityContextHolder.getContext().setAuthentication(event.getAuthentication());
    }

}
