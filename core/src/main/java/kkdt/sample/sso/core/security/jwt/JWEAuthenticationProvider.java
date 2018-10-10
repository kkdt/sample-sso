/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core.security.jwt;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.nimbusds.jwt.SignedJWT;

import kkdt.sample.sso.core.security.AuthInfoSecured;

public class JWEAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = Logger.getLogger(JWEAuthenticationProvider.class);
    
    private final JWECrypto jweCrypto;
    
    public JWEAuthenticationProvider(JWECrypto jweCrypto) {
        this.jweCrypto = jweCrypto;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authenticated = null;
        if(authentication != null) {
            JWEAuthenticationRequest _authentication = (JWEAuthenticationRequest)authentication;
            
            String idToken = (String)_authentication.getCredentials();
            logger.info("Received id_token (JWE): " + idToken);
            
            boolean verified = false;
            try {
                String decrypted = jweCrypto.decrypt(idToken);
                
                // verify the signature
                verified = new ClasspathResourceJWSVerifier("server.crt")
                    .verifyJWS(decrypted);
                
                // TODO: Verify claims as appropriate
                
                SignedJWT signed = SignedJWT.parse(decrypted);
                authenticated = new AuthInfoSecured(signed.getJWTClaimsSet().getIssuer(),
                    verified ? idToken : null, 
                    Stream.of(new SimpleGrantedAuthority(verified ? "JWE" : "UNAUTHORIZED")).collect(Collectors.toList()));
            } catch (Exception e) {
                throw new BadCredentialsException("Cannot verify id_token", e);
            }
        }
        return authenticated;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JWEAuthenticationRequest.class.isAssignableFrom(authentication));
    }

}