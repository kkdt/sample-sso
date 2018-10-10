/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.authserver;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.junit.BeforeClass;
import org.junit.Test;

import kkdt.sample.sso.core.AuthenticationInfo;
import kkdt.sample.sso.core.IdentityBroker;
import kkdt.sample.sso.core.security.jwt.ClasspathResourceRSAKey;
import kkdt.sample.sso.core.security.jwt.IDBroker;

public class AuthenticationServiceTest {
    private static AuthenticationService service;
    private static IdentityBroker idBroker;
    
    @BeforeClass
    public static void before() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException 
    {
        idBroker = new IDBroker(new ClasspathResourceRSAKey("server.p12", "server", "changeit"));
        service = new AuthenticationService("admin", idBroker);
    }
    
    @Test
    public void testSuccessfulAuthentication() {
        AuthenticationInfo auth = service.authenticate("admin", "password".toCharArray(), "");
        assertTrue(auth != null && auth.getSession() != null && auth.getIdToken() != null);
    }
    
    @Test
    public void testUnsuccessfulAuthentication() {
        AuthenticationInfo auth = service.authenticate("hello", "password".toCharArray(), "");
        assertTrue(auth != null && auth.getSession() == null && auth.getIdToken() == null);
    }
}
