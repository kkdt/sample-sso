/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * Configuration that exposes the RMI client stub for {@linkplain IAuthenticationService}.
 * 
 * @author thinh ho
 *
 */
@Configuration
public class AuthenticationServiceLocator {
    private static final Logger logger = Logger.getLogger(AuthenticationServiceLocator.class);
        
    public static class Builder {
        AuthenticationServiceLocator locator = new AuthenticationServiceLocator();
        
        public Builder withHost(String host) {
            locator.host = host;
            return this;
        }
        
        public Builder withPort(int port) {
            locator.port = port;
            return this;
        }
        
        public IAuthenticationService build() {
            RmiProxyFactoryBean proxy = locator.service();
            proxy.afterPropertiesSet();
            return (IAuthenticationService)proxy.getObject();
        }
    }
    
    @Value("${AuthenticationServiceLocator.host}")
    private String host;
    
    @Value("${AuthenticationServiceLocator.port}")
    private int port;
    
    public AuthenticationServiceLocator withHost(String host) {
        this.host = host;
        return this;
    }
    
    public AuthenticationServiceLocator withPort(int port) {
        this.port = port;
        return this;
    }
    
    @Bean
    public RmiProxyFactoryBean service() {
        logger.info("Creating RMI IAuthenticationService " + host + ":" + port);
        RmiProxyFactoryBean rmiProxyFactory = new RmiProxyFactoryBean();
        rmiProxyFactory.setServiceUrl(String.format("rmi://%s:%d/%s", host, port, IAuthenticationService.class.getSimpleName()));
        rmiProxyFactory.setServiceInterface(IAuthenticationService.class);
        return rmiProxyFactory;
    }
}
