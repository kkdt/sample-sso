/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.console;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;

import kkdt.sample.sso.core.security.jwt.ClasspathResourceJWSVerifier;

/**
 * Application controller that can be attached to the following actions:
 * <ol>
 * <li>Login</li>
 * <li>Logout</li>
 * <li>Exit</li>
 * </ol>
 * 
 * <p>
 * In addition, it also listens for authentication events so that the UI can 
 * react accordingly.
 * </p>
 * 
 * @author thinh ho
 *
 */
public class ConsoleController 
    implements ActionListener, ApplicationListener<AbstractAuthenticationEvent> 
{
    private static final Logger logger = Logger.getLogger(ConsoleController.class);
    
    private final GlobalSecurityContextAuthenticationManager authenticationManager;
    private Supplier<String> userName;
    private Consumer<String> feedback;
    private final Collection<Consumer<AbstractAuthenticationEvent>> authenticationListeners = new Vector<>();
    
    public ConsoleController(GlobalSecurityContextAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    /**
     * Input for the user name field.
     * 
     * @param userName
     * @return
     */
    public ConsoleController userName(Supplier<String> userName) {
        this.userName = userName;
        return this;
    }
    
    /**
     * Allow this controller to provide feedback to the UI.
     * 
     * @param feedback
     * @return
     */
    public ConsoleController feedback(Consumer<String> feedback) {
        this.feedback = feedback;
        return this;
    }
    
    /**
     * Authentication events can be deleted to UI components.
     * 
     * @param l
     * @return
     */
    public ConsoleController authenticationListener(Consumer<AbstractAuthenticationEvent> l) {
        authenticationListeners.add(l);
        return this;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
        case "Login":
            doLogin();
            break;
        case "Logout":
            doLogout();
            break;
        case "Exit":
            System.exit(0);
            break;
        }
    }
    
    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        if(event.getAuthentication() != null && event.getAuthentication().isAuthenticated()) {
            doFeedback("Successful login: " + event.getAuthentication());
            doFeedback("id_token: " + event.getAuthentication().getCredentials());
            
            // do verification
            try {
                boolean valid = new ClasspathResourceJWSVerifier("server.crt")
                    .verifyJWS((String)event.getAuthentication().getCredentials());
                doFeedback(String.format("JWS %s valid", valid ? "is" : "is NOT"));
            } catch (Exception e) {
                logger.error(e);
            }
        }
        logger.info("Publishing authentication event: " + event);
        authenticationListeners.forEach(c -> c.accept(event));
    }
    
    private void doFeedback(String message) {
        if(feedback != null) {
            this.feedback.accept(message);
        }
        logger.info(message);
    }
    
    private void doLogin() {
        if(userName == null || authenticationManager == null) {
            doFeedback("Not configured for login");
        } else {
            doFeedback("Logging into application");
            try {
                authenticationManager.login(userName.get());
            } catch (Exception e) {
                doFeedback(e.getMessage());
                logger.error(e);
            }
        }
    }
    
    private void doLogout() {
        doFeedback("Logging out of application");
        try {
            authenticationManager.logout();
        } catch (Exception e) {
            doFeedback(e.getMessage());
            logger.error(e);
        }
    }
    
}
