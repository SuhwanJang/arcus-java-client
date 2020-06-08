package com.jam2in.arcus.admin.tool.domain.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EmailService {

  private MailSender mailSender;

  @Value("${mail.from}")
  private String from;

  public EmailService(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void mailSend(String subject, String to, String text) {
    SimpleMailMessage mail = new SimpleMailMessage();
    mail.setSubject(subject);
    mail.setTo(to);
    mail.setText(text);
    mail.setFrom(from);
    mailSender.send(mail);
  }
}
