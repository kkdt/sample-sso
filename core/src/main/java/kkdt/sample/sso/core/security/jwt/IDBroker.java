/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core.security.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import kkdt.sample.sso.core.AuthenticationInfo;
import kkdt.sample.sso.core.IdentityBroker;

/**
 * Broker out where to obtain the id_token.
 * 
 * @author thinh ho
 *
 */
public class IDBroker implements IdentityBroker {
    private static final String ID = String.format("kkdt://%s/", IDBroker.class.getSimpleName());
    
    private final ClasspathResourceRSAKey keyProvider;
    
    /**
     * The default identity provider will the current server.
     * 
     * @param keyProvider
     */
    public IDBroker(ClasspathResourceRSAKey keyProvider) {
        this.keyProvider = keyProvider;
    }
    
    @Override
    public String generateIdToken(AuthenticationInfo authentication, String source) {
        if(authentication == null || source == null) {
            throw new IllegalStateException("Cannot generate an id_token, authentication is not valid");
        }
        
        String token = null;
        switch(source) {
        default:
            token = generateIdTokenDefaultProvider(authentication);
            break;
        }
        return token;
    }
    
    /**
     * The default identity provider is the current server.
     * 
     * @param authentication
     * @return
     */
    private String generateIdTokenDefaultProvider(AuthenticationInfo authentication) {
        Date now = new Date();
        Calendar exp = Calendar.getInstance();
        exp.setTime(now);
        exp.setTimeInMillis(Instant.ofEpochMilli(now.getTime()).plus(10, ChronoUnit.HOURS).toEpochMilli());
        
        // claims for the identity
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(authentication.getUserId())
            .expirationTime(exp.getTime())
            .audience(ID + authentication.getUserId())
            .issueTime(now)
            .issuer(ID)
            .build();
        
        // the JWS to sign
        SignedJWT signedJWT = new SignedJWT(
            new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(keyProvider.getKeyID()).build(),
            claimsSet);
        
        // rsa-signer with the private key
        try {
            signedJWT.sign(new RSASSASigner(keyProvider.getKey()));
        } catch (JOSEException e) {
            throw new IllegalStateException("Cannot sign JWS", e);
        }
        
        return signedJWT.serialize();
    }

}
