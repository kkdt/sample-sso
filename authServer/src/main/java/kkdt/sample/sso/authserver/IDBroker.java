/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.authserver;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import kkdt.sample.sso.core.ClasspathResourceRSAKey;
import kkdt.sample.sso.core.IdentityBroker;
import kkdt.sample.sso.core.JWECrypto;
import kkdt.sample.sso.core.JWECrypto.Output;

/**
 * Broker out where to obtain the id_token.
 * 
 * @author thinh ho
 *
 */
public class IDBroker implements IdentityBroker {
    private static final Logger logger = Logger.getLogger(IDBroker.class);
    private static final String ID = String.format("kkdt://%s/", IDBroker.class.getSimpleName());
    
    private final ClasspathResourceRSAKey keyProvider;
    private final JWECrypto jweCrypto;
    private final Map<String, UUID> activeSessions = Collections.synchronizedMap(new HashMap<>());
    
    /**
     * The default identity provider will the current server.
     * 
     * @param keyProvider
     */
    public IDBroker(ClasspathResourceRSAKey keyProvider) {
        this.keyProvider = keyProvider;
        this.jweCrypto = new JWECrypto();
    }
    
    /**
     * Encrypt a JWS.
     * 
     * @param userId
     * @return
     * @throws Exception
     */
    private String jweGenerateToken(String userId) throws Exception {
        // generate the jws as the payload
        String jws = jwsGenerateToken(userId);
        return jweCrypto.encrypt(jws, keyProvider.getPublicKey());
    }
    
    /**
     * The default identity provider is the current server.
     * 
     * @param authentication
     * @return
     */
    private String jwsGenerateToken(String userId)  throws Exception {
        Date now = new Date();
        Calendar exp = Calendar.getInstance();
        exp.setTime(now);
        exp.setTimeInMillis(Instant.ofEpochMilli(now.getTime()).plus(10, ChronoUnit.HOURS).toEpochMilli());
        
        // claims for the identity
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(userId)
            .expirationTime(exp.getTime())
            .audience(ID + userId)
            .issueTime(now)
            .issuer(ID)
            .build();
        
        // the JWS to sign
        SignedJWT signedJWT = new SignedJWT(
            new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(keyProvider.getKeyID())
                .build(),
            claimsSet);
        
        // rsa-signer with the private key
        try {
            signedJWT.sign(new RSASSASigner(keyProvider.getKey()));
        } catch (JOSEException e) {
            throw new IllegalStateException("Cannot sign JWS", e);
        }
        
        return signedJWT.serialize();
    }
    
    private String defaultIdToken(String userId, Output o) throws Exception {
        String token = "";
        switch(o) {
        case Signed:
            logger.info("Generating JWS token for authentication");
            token = jwsGenerateToken(userId);
            break;
        case Encrypted:
            logger.info("Generating JWE token for authentication");
            token = jweGenerateToken(userId);
            break;
        }
        return token;
    }

    @Override
    public String idToken(String userId, String source, Output o) throws Exception {
        if(userId == null || source == null || o == null) {
            throw new IllegalStateException("Cannot generate an id_token, not enough information");
        }
        
        String token = null;
        switch(source) {
        default:
            token = defaultIdToken(userId, o);
            break;
        }
        return token;
    }
    
    @Override
    public String idToken(String userId, Output o) throws Exception {
        return idToken(userId, "", o);
    }

    @Override
    public String session(String userId) {
        UUID session = activeSessions.getOrDefault(userId, UUID.randomUUID());
        activeSessions.put(userId, session);
        return session.toString();
    }

}
