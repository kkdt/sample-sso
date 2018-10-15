/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.log4j.Logger;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.RSAKey.Builder;

/**
 * Loads a classpath keystore file and extract the PKCS12 key-pair - JOSE implementation.
 * 
 * @author thinh ho
 *
 */
public class ClasspathResourceRSAKey {
    private static final Logger logger = Logger.getLogger(ClasspathResourceRSAKey.class);
    
    private final RSAKey rsaKey;
    
    public ClasspathResourceRSAKey(final String classpathKeystore, String alias, final String password) 
        throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException 
    {
        try (InputStream is = ClasspathResourceRSAKey.class.getClassLoader().getResourceAsStream(classpathKeystore)) {
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            keystore.load(is, password.toCharArray());
            RSAPrivateKey privateKey = (RSAPrivateKey) keystore.getKey(alias, password.toCharArray());
            RSAPublicKey publicKey = (RSAPublicKey) keystore.getCertificate(alias).getPublicKey();
            
            this.rsaKey = new Builder(publicKey)
                .algorithm(JWSAlgorithm.RS256)
                .privateKey(privateKey)
                .keyID(alias)
                .build();
        }
    }
    
    public RSAPublicKey getPublicKey() {
        RSAPublicKey key = null;
        try {
            key = rsaKey.toRSAPublicKey();
        } catch (JOSEException e) {
            logger.error(e);
        }
        return key;
    }

    public RSAPrivateKey getPrivateKey() {
        RSAPrivateKey key = null;
        try {
            key = rsaKey.toRSAPrivateKey();
        } catch (JOSEException e) {
            logger.error(e);
        } 
        return key;
    }

    public String getKeyID() {
        return rsaKey.getKeyID();
    }
    
    public RSAKey getKey() {
        return rsaKey;
    }

}
