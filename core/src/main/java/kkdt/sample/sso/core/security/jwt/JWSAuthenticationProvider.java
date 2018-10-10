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

/**
 * Authenticate a JWS token.
 * 
 * @author thinh ho
 *
 */
public class JWSAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = Logger.getLogger(JWSAuthenticationProvider.class);
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authenticated = null;
        if(authentication != null) {
            JWSAuthenticateRequest _authentication = (JWSAuthenticateRequest)authentication;
            
            String idToken = (String)_authentication.getCredentials();
            logger.info("Received id_token: " + idToken);
            
            boolean verified = false;
            try {
                // verify the signature
                verified = new ClasspathResourceJWSVerifier("server.crt")
                    .verifyJWS(idToken);
                
                // TODO: Verify claims as appropriate
                
                SignedJWT signed = SignedJWT.parse(idToken);
                authenticated = new AuthInfoSecured(signed.getJWTClaimsSet().getIssuer(),
                    verified ? idToken : null, 
                    Stream.of(new SimpleGrantedAuthority(verified ? "JWS" : "UNAUTHORIZED")).collect(Collectors.toList()));
            } catch (Exception e) {
                throw new BadCredentialsException("Cannot verify id_token");
            }
        }
        return authenticated;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JWSAuthenticateRequest.class.isAssignableFrom(authentication));
    }

}
