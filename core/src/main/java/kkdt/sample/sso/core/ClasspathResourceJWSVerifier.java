/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;

/**
 * Read in a certificate file from the classpath to verify signature - JOSE implemention.
 * 
 * @author thinh ho
 *
 */
public class ClasspathResourceJWSVerifier {
    
    private final RSASSAVerifier verifier;
    private final X509Certificate certificate;
    
    public ClasspathResourceJWSVerifier(String classpathCertificate) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, CertificateException {
        try (InputStream is = ClasspathResourceRSAKey.class.getClassLoader().getResourceAsStream(classpathCertificate)) {
            CertificateFactory f = CertificateFactory.getInstance("X.509");
            this.certificate = (X509Certificate)f.generateCertificate(is);
            PublicKey pk = certificate.getPublicKey();
            this.verifier = new RSASSAVerifier((RSAPublicKey)pk);
        }
    }
    
    public boolean verifyJWS(String jws) throws Exception {
        SignedJWT signed = SignedJWT.parse(jws);
        return signed.verify(verifier);
    }
}
