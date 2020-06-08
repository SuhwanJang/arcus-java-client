package com.jam2in.arcus.admin.tool.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Configuration
public class MailConfiguration {

  @Value("${mail.protocol}")
  private String protocol;

  @Value("${mail.host}")
  private String host;

  @Value("${mail.port}")
  private int port;

  @Value("${mail.smtp.auth}")
  private boolean auth;

  @Value("${mail.smtp.starttls.enable}")
  private boolean starttls;

  @Value("${mail.smtps.ssl.enable}")
  private boolean ssl;

  @Value("${mail.smtps.ssl.checkserveridentity}")
  private boolean checkserveridentity;

  @Value("${mail.smtps.ssl.trust}")
  private String trust;

  @Value("${mail.from}")
  private String from;

  @Value("${mail.username}")
  private String username;

  @Value("${mail.password}")
  private String password;

  @Bean
  public MailSender mailTlsSender() {
    Properties mailProperties = new Properties();
    mailProperties.put("mail.smtp.auth", auth);
    mailProperties.put("mail.smtp.starttls.enable", starttls);

    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setJavaMailProperties(mailProperties);
    mailSender.setHost(host);
    mailSender.setPort(port);
    mailSender.setProtocol(protocol);
    mailSender.setUsername(username);
    mailSender.setPassword(password);

    return mailSender;
  }

  @Bean
  public MailSender mailSslSender() {
    Properties mailProperties = new Properties();
    mailProperties.put("mail.smtps.ssl.enable", ssl);
    mailProperties.put("mail.smtps.ssl.checkserveridentity", checkserveridentity);
    mailProperties.put("mail.smtps.ssl.trust", trust);

    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setJavaMailProperties(mailProperties);
    mailSender.setHost(host);
    mailSender.setPort(port);
    mailSender.setProtocol(protocol);
    mailSender.setUsername(username);
    mailSender.setPassword(password);

    return mailSender;
  }

}
