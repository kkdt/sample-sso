/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kddt.sample.sso.core.security.jwt;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Exposes how to extract JWT information from an HTTP request.
 * 
 * @author thinh ho
 *
 */
public class JWTDetailsSource {
    /**
     * Authentication header containing the 'Bearer' being the id_token.
     */
    public static final Function<HttpServletRequest, JWSAuthenticateRequest> fromHeaders = r -> {
        String bearer = Optional.ofNullable(r.getHeader("Bearer")).orElse("");
        return new JWSAuthenticateRequest(bearer);
    };
    
    /**
     * The URL containing an 'token' parameter being the id_token - JWS.
     */
    public static final Function<HttpServletRequest, JWSAuthenticateRequest> jwsURL = r -> {
        String id = Optional.ofNullable(r.getParameter("token")).orElse("");
        return new JWSAuthenticateRequest(id);
    };
    
    /**
     * The URL containing an 'token' parameter being the id_token - JWE.
     */
    public static final Function<HttpServletRequest, JWEAuthenticationRequest> jweURL = r -> {
        String id = Optional.ofNullable(r.getParameter("token")).orElse("");
        return new JWEAuthenticationRequest(id);
    };
    
    public static final Function<HttpServletRequest, JWEAuthenticationRequest> jweCookie = r -> {
        String id = "";
        if(r.getCookies() != null) {
            Optional<Cookie> found = Stream.of(r.getCookies())
                .filter(c -> "juliet".equals(c.getName()))
                .findFirst();
            id = found.isPresent() ? found.get().getValue() : "";
        }
        return new JWEAuthenticationRequest(id);
    };
}
