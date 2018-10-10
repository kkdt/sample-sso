/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core.security.jwt;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;

/**
 * https://connect2id.com/products/nimbus-jose-jwt/examples/jwe-with-preset-cek
 * https://connect2id.com/products/nimbus-jose-jwt/examples/signed-and-encrypted-jwt
 * 
 * @author thinh ho
 *
 */
public class JWECrypto {
    private final ClasspathResourceRSAKey keyProvider;
    
    public JWECrypto(ClasspathResourceRSAKey keyProvider) {
        this.keyProvider = keyProvider;
    }
    
    public String encrypt(String payload) throws Exception {
        // Create JWE object with signed JWT as payload
        JWEObject jweObject = new JWEObject(
            // https://stackoverflow.com/questions/40104464/generating-signed-and-encrypted-jwt
//            new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
            new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM)
                .contentType("JWT") // required to indicate nested JWT
                .build(),
            new Payload(payload));

        // Encrypt with the recipient's public key
        jweObject.encrypt(new RSAEncrypter(keyProvider.getPublicKey()));

        // Serialise to JWE compact form
        return jweObject.serialize();
    }
    
    public String decrypt(String payload) throws Exception {
        // Parse the JWE string
        JWEObject jweObject = JWEObject.parse(payload);

        // Decrypt with private key
        jweObject.decrypt(new RSADecrypter(keyProvider.getPrivateKey()));

        // Extract payload
        return jweObject.getPayload().toString();
    }
}
