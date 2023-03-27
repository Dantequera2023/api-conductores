package com.sopra.flotas.flotasbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@ConfigurationProperties
public class MailConfiguration {

    @Value("${albia.email.sender.host}")
    private String host;
    @Value("${albia.email.sender.username}")
    private String user;
    @Value("${albia.email.sender.password}")
    private String password;
    @Value("${albia.email.sender.debug}")
    private Boolean debug;
    @Value("${albia.email.sender.port}")
    private int port;
    @Value("${albia.email.sender.protocol}")
    private String protocol;
    @Value("${albia.email.sender.defaultEncoding}")
    private String defaultEncoding;
    @Value("${albia.email.sender.properties.mail.smtp.auth}")
    private Boolean auth;
    @Value("${albia.email.sender.properties.mail.smtp.starttls.enable}")
    private Boolean starttls;


    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Set up Gmail config
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setProtocol(protocol);
        mailSender.setDefaultEncoding(defaultEncoding);



        // Set up email config (using udeesa email)
        mailSender.setUsername(user);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.debug", debug);
        return mailSender;
    }
}
