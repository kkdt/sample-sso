/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.authserver;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.rmi.RmiServiceExporter;

import kkdt.sample.sso.core.AuthenticationService;

@SpringBootApplication
public class AuthenticationServerApplication {
    private static final Logger logger = Logger.getLogger(AuthenticationServerApplication.class);
    
    @Value("${AuthenticationServerApplication.validUser}")
    private String validUser;
    
    @Value("${AuthenticationServerApplication.port}")
    private int port;
    
    @Bean
    public AuthenticationService authenticationService() {
        return new AuthService(validUser);
    }
    
    @Bean
    public RmiServiceExporter rmiExporter(AuthenticationService authenticationService) {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceInterface(AuthenticationService.class);
        exporter.setService(authenticationService);
        exporter.setServiceName(AuthenticationService.class.getSimpleName());
        exporter.setRegistryPort(port); 
        return exporter;
    }
    
    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthenticationServerApplication.class)
            .bannerMode(Mode.LOG)
            .logStartupInfo(false)
            .headless(true)
            .web(false)
            .run(args);
        logger.info("RMI interface active " + AuthenticationService.class.getSimpleName());
    }
}
