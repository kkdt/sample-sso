/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kddt.sample.sso.core.security.jwt;

import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

/**
 * Filter all requests coming into the configured url-match to build an authentication
 * request for Spring Security - expecting a JWS token.
 * 
 * @author thinh ho
 *
 */
public class JWSAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger logger = Logger.getLogger(JWSAuthenticationProcessingFilter.class);
    
    /**
     * 
     * @param defaultFilterProcessesUrl
     * @param f how to extract the authentication request from the HTTP request.
     */
    public JWSAuthenticationProcessingFilter(String defaultFilterProcessesUrl, 
        final Function<HttpServletRequest, JWSAuthenticateRequest> f,
        final AuthenticationManager authenticationManager) 
    {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(authenticationManager);
        this.authenticationDetailsSource = new AuthenticationDetailsSource<HttpServletRequest, JWSAuthenticateRequest>() {
            @Override
            public JWSAuthenticateRequest buildDetails(HttpServletRequest context) {
                JWSAuthenticateRequest idToken = f.apply(context);
                logger.info("ID Token (JWS): " + idToken.getCredentials());
                return idToken;
            }
        };
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
        throws AuthenticationException 
    {
        JWSAuthenticateRequest authRequest = (JWSAuthenticateRequest)authenticationDetailsSource.buildDetails(request);
        
        // the authentication provider configured this authentication token will perform the authentication
        return this.getAuthenticationManager().authenticate(authRequest);
    }

}
