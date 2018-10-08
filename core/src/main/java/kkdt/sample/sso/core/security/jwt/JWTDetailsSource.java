/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.core.security.jwt;

import java.util.Optional;
import java.util.function.Function;

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
    public static final Function<HttpServletRequest, JWTAuthenticateRequest> fromHeaders = r -> {
        String bearer = Optional.ofNullable(r.getHeader("Bearer")).orElse("");
        return new JWTAuthenticateRequest(bearer);
    };
    
    /**
     * The URL containing an 'id' parameter being the id_token.
     */
    public static final Function<HttpServletRequest, JWTAuthenticateRequest> fromURL = r -> {
        String id = Optional.ofNullable(r.getParameter("id")).orElse("");
        return new JWTAuthenticateRequest(id);
    };
}
