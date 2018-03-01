/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.console;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class Console {
    private static final Logger logger = Logger.getLogger(Console.class);
    
    public static void main(String[] args) {
        /*
         * Let Spring Security know to use the 
         *  org.springframework.security.core.context.GlobalSecurityContextHolderStrategy
         * that is recommended for thick client applications.
         */
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);
        
        String[] configurations = new String[] {
           "classpath:spring/console.xml"
        };
        
        logger.info("Creating client application context...");
        ApplicationContext appContext = null;
        
        try {
            appContext = new ClassPathXmlApplicationContext(configurations);
            ConsoleController consoleController = appContext.getBean(ConsoleController.class);
            
            JFrame frame = new ConsoleBuilder()
                .consoleController(consoleController)
                .build(new JFrame("Console"));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(500, 300));
            frame.setVisible(true);
        } catch (Exception e) {
            logger.error(e);
            System.exit(-1);
        }
    }
}
