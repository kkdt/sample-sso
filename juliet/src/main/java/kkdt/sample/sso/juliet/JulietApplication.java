/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.juliet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import kkdt.sample.sso.core.ClasspathResourceJWSVerifier;
import kkdt.sample.sso.core.ClasspathResourceRSAKey;
import kkdt.sample.sso.core.IdentityBrokerServiceLocator;
import kkdt.sample.sso.core.JWECrypto;

@SpringBootApplication
@Import(value=IdentityBrokerServiceLocator.class)
public class JulietApplication {
    
    @Bean
    public ClasspathResourceRSAKey rsaKey() throws Exception {
        return new ClasspathResourceRSAKey("server.p12", "server", "changeit");
    }
    
    @Bean
    public JWECrypto jweCrypto() {
        return new JWECrypto();
    }
    
    @Bean
    public ClasspathResourceJWSVerifier jwsVerifier() throws Exception {
        return new ClasspathResourceJWSVerifier("server.crt");
    }
    
    public static void main(String[] args) {
        SpringApplication.run(JulietApplication.class, args);
    }
}
