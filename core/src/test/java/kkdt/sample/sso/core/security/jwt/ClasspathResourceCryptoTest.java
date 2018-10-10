/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core.security.jwt;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class ClasspathResourceCryptoTest {
    private static ClasspathResourceRSAKey rsaKey;
    private static ClasspathResourceJWSVerifier verifier;
    private static JWECrypto jweCrypto;
    
    @BeforeClass
    public static void before() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, InvalidKeySpecException {
        rsaKey = new ClasspathResourceRSAKey("server.p12", "server", "changeit");
        assertNotNull(rsaKey);
        assertNotNull(rsaKey.getPublicKey());
        assertNotNull(rsaKey.getPrivateKey());
        assertNotNull(rsaKey.getKeyID());
        assertNotNull(rsaKey.getKey());
        
        verifier = new ClasspathResourceJWSVerifier("server.crt");
        assertNotNull(verifier);
        
        jweCrypto = new JWECrypto(rsaKey);
        assertNotNull(jweCrypto);
    }
    
    @Test
    public void testExampleFromConnect2id() throws Exception {
        // Create RSA-signer with the private key
        JWSSigner signer = new RSASSASigner(rsaKey.getPrivateKey());

        // Prepare JWT with claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject("alice")
            .issuer("https://c2id.com")
            .expirationTime(new Date(new Date().getTime() + 60 * 1000))
            .build();

        SignedJWT signedJWT = new SignedJWT(
            new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .keyID(rsaKey.getKeyID())
                .build(),
            claimsSet);
        
        // Compute the RSA signature
        signedJWT.sign(signer);
        String s = signedJWT.serialize();
        
        assertNotNull(s);
        assertTrue(verifier.verifyJWS(s));
        
        // test JWE
        String jwe = jweCrypto.encrypt(s);
        assertNotNull(jwe);
        String jws = jweCrypto.decrypt(jwe);
        assertNotNull(jws);
        assertTrue(verifier.verifyJWS(jws));
    }
}
