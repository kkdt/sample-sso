/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.romeo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import kddt.sample.sso.core.security.SecuredConfiguration;

@SpringBootApplication
@Import(value=SecuredConfiguration.class)
public class RomeoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(RomeoApplication.class, args);
    }
}
