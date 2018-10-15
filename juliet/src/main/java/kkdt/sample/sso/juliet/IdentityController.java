/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.juliet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kkdt.sample.sso.core.ClasspathResourceJWSVerifier;
import kkdt.sample.sso.core.ClasspathResourceRSAKey;
import kkdt.sample.sso.core.IdentityBroker;
import kkdt.sample.sso.core.JWECrypto;
import kkdt.sample.sso.core.JWECrypto.Output;

@RestController
@SuppressWarnings("unused")
public class IdentityController {
    private static final Logger logger = Logger.getLogger(IdentityController.class);
    
    @Autowired
    private IdentityBroker identityBroker;
    
    @Autowired
    private ClasspathResourceRSAKey rsaKey;
    
    @Autowired
    private JWECrypto jweCrypto;
    
    @Autowired
    private ClasspathResourceJWSVerifier jwsVerifier;
    
    /**
     * Generate an cookie with the encrypted id_token.
     * 
     * @param payload
     * @return
     */
    private Cookie generateEncryptedCookie(String payload) {
        Cookie cookie = new Cookie("juliet", payload);
        /*
         * Sends the cookie from the browser to the server only when using a 
         * secure protocol
         */
        cookie.setSecure(true);
        /*
         * -1 indicates not stored and is a session cookie and will be deleted
         * after browser is closed
         */
        cookie.setMaxAge(-1);
        /*
         * The HttpOnly flag in cookies makes it so that changing cookie values 
         * through JavaScript is not possible - mitigates cross-site scripting 
         * (XSS) attacks.
         */
        cookie.setHttpOnly(true);
        cookie.setPath("/sso");
        cookie.setVersion(1);
        return cookie;
    }
    
    @RequestMapping(path="/login", method=RequestMethod.GET)
    public void login(@RequestParam("url")String destination, @RequestParam("user") String userId,
        HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
        /*
         * This endpoint needs to do more but for the purpose of prototype, all
         * it is assumed that 'juliet' is an *internal* webapp, closed off from
         * any external access.
         */
        
        try {
            String idToken = identityBroker.idToken(userId, Output.Encrypted);
            Cookie cookie = generateEncryptedCookie(idToken);
            response.addCookie(cookie);
            response.sendRedirect(destination);
        } catch (Exception e) {
            logger.error(e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    
    @RequestMapping(path="/jwk", method=RequestMethod.GET, produces="application/json")
    public String jwk(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return rsaKey.getKey().toPublicJWK().toJSONString();
    }
    
    @RequestMapping(path="/redirect", method=RequestMethod.GET)
    public void redirect(@RequestParam("url")String destination, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String value = identityBroker.idToken("juliet", Output.Encrypted);
        Cookie cookie = generateEncryptedCookie(value);
        response.addCookie(cookie);
        response.sendRedirect(destination);
    }
    
}
