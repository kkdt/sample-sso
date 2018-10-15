/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

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
    
    /**
     * Indicate to output a JWS or JWE.
     * 
     * @author thinh ho
     *
     */
    public static enum Output {
        Signed,
        Encrypted
    }
    
    /**
     * Encrypt the payload.
     * 
     * @param payload
     * @param publicKey the recipient public key.
     * @return
     * @throws Exception
     */
    public String encrypt(String payload, RSAPublicKey publicKey) throws Exception {
        return encrypt(payload, publicKey, "JWT");
    }
    
    public String encrypt(String payload, RSAPublicKey publicKey, String contentType) throws Exception {
        JWEObject jweObject = new JWEObject(
            // https://stackoverflow.com/questions/40104464/generating-signed-and-encrypted-jwt
//            new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
            new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM)
                .contentType(contentType) // required to indicate nested JWT
                .build(),
            new Payload(payload));

        // encrypt using the public key
        jweObject.encrypt(new RSAEncrypter(publicKey));

        // compact, url-safe serialization
        return jweObject.serialize();
    }
    
    public String decrypt(String payload, RSAPrivateKey privateKey) throws Exception {
        // Parse the JWE string
        JWEObject jweObject = JWEObject.parse(payload);

        // Decrypt with private key
        jweObject.decrypt(new RSADecrypter(privateKey));

        // Extract payload
        return jweObject.getPayload().toString();
    }
}
