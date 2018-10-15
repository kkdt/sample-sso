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
 * Configuration that exposes the RMI client stub for {@linkplain IdentityBroker}.
 * 
 * @author thinh ho
 *
 */
@Configuration
public class IdentityBrokerServiceLocator {
    private static final Logger logger = Logger.getLogger(IdentityBrokerServiceLocator.class);
    
    @Value("${IdentityBrokerServiceLocator.host}")
    private String host;
    
    @Value("${IdentityBrokerServiceLocator.port}")
    private int port;
    
    @Bean(name="identityBroker")
    public RmiProxyFactoryBean identityBroker() {
        logger.info("Creating RMI IdentityBroker " + host + ":" + port);
        RmiProxyFactoryBean rmiProxyFactory = new RmiProxyFactoryBean();
        rmiProxyFactory.setServiceUrl(String.format("rmi://%s:%d/%s", host, port, IdentityBroker.class.getSimpleName()));
        rmiProxyFactory.setServiceInterface(IdentityBroker.class);
        return rmiProxyFactory;
    }
}
