/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.authserver;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.rmi.RmiServiceExporter;

import kkdt.sample.sso.core.IAuthenticationService;
import kkdt.sample.sso.core.security.jwt.ClasspathResourceRSAKey;
import kkdt.sample.sso.core.security.jwt.IDBroker;

@SpringBootApplication
public class AuthenticationServerApplication {
    private static final Logger logger = Logger.getLogger(AuthenticationServerApplication.class);
    
    @Value("${AuthenticationServerApplication.validUser}")
    private String validUser;
    
    @Value("${AuthenticationServerApplication.port}")
    private int port;
    
    @Bean
    public IAuthenticationService authenticationService() 
        throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException 
    {
        return new AuthenticationService(validUser, 
            new IDBroker(new ClasspathResourceRSAKey("server.p12", "server", "changeit")));
    }
    
    @Bean
    public RmiServiceExporter rmiExporter(IAuthenticationService authenticationService) {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceInterface(IAuthenticationService.class);
        exporter.setService(authenticationService);
        exporter.setServiceName(IAuthenticationService.class.getSimpleName());
        exporter.setRegistryPort(port); 
        return exporter;
    }
    
    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthenticationServerApplication.class)
            .bannerMode(Mode.LOG)
            .logStartupInfo(false)
            .headless(true)
            .web(WebApplicationType.NONE)
            .run(args);
        logger.info("RMI interface active " + IAuthenticationService.class.getSimpleName());
    }
}
