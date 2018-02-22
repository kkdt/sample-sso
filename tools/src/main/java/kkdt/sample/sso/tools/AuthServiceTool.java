/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.tools;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.stream.Stream;

import kkdt.sample.sso.core.AuthenticationService;
import kkdt.sample.sso.core.AuthenticationServiceLocator;

public class AuthServiceTool {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            Stream.of(registry.list()).forEach(s -> System.out.print(s));
            
            AuthenticationService service = new AuthenticationServiceLocator.Builder()
                .withHost("localhost")
                .withPort(1099)
                .build();
            System.out.println("validUser(admin): " + service.validUser("admin"));
            System.out.println("authenticate(admin): " + service.authenticate("admin", "password".toCharArray()));
            
            System.out.println("validUser(foo): " + service.validUser("foo"));
            System.out.println("authenticate(foo): " + service.authenticate("foo", "password".toCharArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
