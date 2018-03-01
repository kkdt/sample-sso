/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.console;

import java.awt.BorderLayout;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * UI builder.
 * 
 * @author thinh ho
 *
 */
public class ConsoleBuilder {
    private static final Logger logger = Logger.getLogger(ConsoleBuilder.class);
    
    private ConsoleController consoleController;
    
    public ConsoleBuilder consoleController(ConsoleController consoleController) {
        this.consoleController = consoleController;
        return this;
    }
    
    /**
     * Build a window with the application contents.
     * 
     * @param frame
     * @return
     */
    public JFrame build(JFrame frame) {
        Objects.requireNonNull(consoleController, "Require a Console Controller");
        
        final JButton exitBtn = new JButton("Exit");
        final JButton actionBtn = new JButton("Login");
        final JTextField name = new JTextField(15);
        final JTextArea textArea = new JTextArea(5, 20);
        
        JPanel inputs = new JPanel();
        inputs.add(new JLabel("Username: "));
        inputs.add(name);
        inputs.add(actionBtn);
        inputs.add(exitBtn);
        
        JScrollPane scrollPane = new JScrollPane(textArea); 
        textArea.setEditable(false);
        
        JPanel feedback = new JPanel(new BorderLayout());
        feedback.add(scrollPane, BorderLayout.CENTER);
        feedback.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(inputs, BorderLayout.NORTH);
        frame.getContentPane().add(feedback, BorderLayout.CENTER);
        frame.pack();
        
        if(consoleController != null) {
            exitBtn.addActionListener(consoleController);
            actionBtn.addActionListener(consoleController);
            
            // the user name is pulled from the text field 
            consoleController.userName(name::getText);
            
            // feedback gets output to the text area
            consoleController.feedback(s -> textArea.append(s + "\n"));
            
            // callback to let UI handle authentication events
            consoleController.authenticationListener(event -> {
                boolean authenticated = event.getAuthentication().isAuthenticated();
                SwingUtilities.invokeLater(() -> {
                    actionBtn.setText(authenticated ? "Logout" : "Login");
                    name.setEnabled(!authenticated);
                    frame.pack();
                });
            });
            consoleController.authenticationListener(event -> {
                
            });
            // additional logging callback
            consoleController.authenticationListener(event -> {
                SecurityContext context = SecurityContextHolder.getContext();
                logger.info("Event.authentication: " + event.getAuthentication());
                logger.info("SecurityContext.getAuthentication: " + context.getAuthentication());
            });
        }
        return frame;
    }
}
