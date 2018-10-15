/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
        
        JRadioButton jws = new JRadioButton("jws");
        JRadioButton jwe = new JRadioButton("jwe");
        JRadioButton sso = new JRadioButton("sso");
        
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(jws);
        btnGroup.add(jwe);
        btnGroup.add(sso);
        jws.setSelected(true); // default
        
        JPanel sources = new JPanel();
        sources.setLayout(new BoxLayout(sources, BoxLayout.X_AXIS));
        sources.add(jws);
        sources.add(jwe);
        sources.add(sso);
        
        final JButton exitBtn = new JButton("Exit");
        final JButton actionBtn = new JButton("Login");
        final JButton launchBtn = new JButton("Launch");
        final JTextField name = new JTextField(15);
        final JTextArea textArea = new JTextArea(5, 20);
        final JTextField url = new JTextField(35);
        
        JPanel inputs = new JPanel();
        inputs.add(new JLabel("Username: "));
        inputs.add(name);
        inputs.add(sources);
        inputs.add(actionBtn);
        inputs.add(exitBtn);
        
        JPanel urlInputs = new JPanel();
        urlInputs.add(new JLabel("URL"));
        urlInputs.add(url);
        urlInputs.add(launchBtn);
        
        JScrollPane scrollPane = new JScrollPane(textArea); 
        textArea.setEditable(false);
        
        JPanel feedback = new JPanel(new BorderLayout());
        feedback.add(scrollPane, BorderLayout.CENTER);
        feedback.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(inputs, BorderLayout.NORTH);
        frame.getContentPane().add(feedback, BorderLayout.CENTER);
        frame.getContentPane().add(urlInputs, BorderLayout.SOUTH);
        
        SwingUtilities.invokeLater(() -> {
            final Dimension d = new Dimension(650, 300);
            frame.setPreferredSize(d);
            frame.setSize(d);
            frame.pack();
        });
        
        url.setText("https://localhost:8991/jws?token=");
        
        if(consoleController != null) {
            consoleController
                // user input fields need to be registerd to controller
                .userName(name::getText)
                .url(url::getText) 
                // feedback gets output to the text area
                .feedback(s -> textArea.append(s + "\n")) 
                // callback to let UI handle authentication events
                .authenticationListener(event -> {
                    boolean authenticated = event.getAuthentication().isAuthenticated();
                    SwingUtilities.invokeLater(() -> {
                        name.setEnabled(!authenticated);
                        jwe.setEnabled(!authenticated);
                        jws.setEnabled(!authenticated);
                        sso.setEnabled(!authenticated);
                        actionBtn.setText(authenticated ? "Logout" : "Login");
                        frame.pack();
                    });
                })
                // additional logging callback
                .authenticationListener(event -> {
                    SecurityContext context = SecurityContextHolder.getContext();
                    if(event.getAuthentication() != null) {
                        actionBtn.setText("Logout");
                        jwe.setEnabled(false);
                        jws.setEnabled(false);
                        sso.setEnabled(false);
                        
                        if(sso.isSelected()) {
                            url.setText("https://localhost:8992/login?url=https://localhost:8991/sso&user=" + name.getText());
                        }
                    }
                    logger.info("Event.authentication: " + event.getAuthentication());
                    logger.info("SecurityContext.getAuthentication: " + context.getAuthentication());
                });
            
            exitBtn.addActionListener(consoleController);
            actionBtn.addActionListener(consoleController);
            launchBtn.addActionListener(consoleController);
            
            jws.addActionListener(consoleController);
            jws.addActionListener(a -> url.setText("https://localhost:8991/jws?token="));
            jwe.addActionListener(consoleController);
            jwe.addActionListener(a -> url.setText("https://localhost:8991/jwe?token="));
            sso.addActionListener(a -> url.setText(""));
        }
        return frame;
    }
}
