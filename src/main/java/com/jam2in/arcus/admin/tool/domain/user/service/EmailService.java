package com.jam2in.arcus.admin.tool.domain.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final MailSender mailSender;

  @Value("${mail.from}")
  private String from;

  public EmailService(MailSender mailTlsSender) {
    this.mailSender = mailTlsSender;
  }

  public void send(String subject, String to, String text) {
    SimpleMailMessage mail = new SimpleMailMessage();
    mail.setSubject(subject);
    mail.setTo(to);
    mail.setText(text);
    mail.setFrom(from);

    mailSender.send(mail);
  }

}
