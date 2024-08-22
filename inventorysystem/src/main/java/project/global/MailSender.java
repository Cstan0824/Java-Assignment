package project.global;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



public class MailSender {
    //
    private String Sender = "tarumtmoviesociety@gmail.com";
    private String Recipient;
    private String Subject;
    private String Body;

    private MimeMessage message;
    private MimeBodyPart mimeBodyPart;
    private Multipart multipart;

    private final String HOST = "smtp.gmail.com";
    private final String MAIL_SERVER = "mail.smtp.host";
    private final String MAIL_PORT = "mail.smtp.port";
    private final String MAIL_AUTH = "mail.smtp.auth";
    private final String MAIL_STARTTLS = "mail.smtp.starttls.enable";

    //getter and setter
    public String getSender() {
        return this.Sender;
    }

    public void setSender(String _Sender) {
        this.Sender = _Sender;
    }

    public String getRecipient() {
        return this.Recipient;
    }

    public void setRecipient(String _Recipient) {
        this.Recipient = _Recipient;
    }

    public String getSubject() {
        return this.Subject;
    }

    public void setSubject(String _Subject) {
        this.Subject = _Subject;
    }

    public String getBody() {
        return this.Body;
    }

    public void setBody(String _Body) {
        this.Body = _Body;
    }

    //MailSender Error: IOException while sending message
    public boolean Send() {
        try {

            this.multipart.addBodyPart(this.mimeBodyPart);
            this.message.setContent(this.multipart);
            Transport.send(this.message);

        } catch (MessagingException e) {
            System.out.println("MailSender Error: " + e.getMessage());

        }
        return true;
    }

    public void AttachFile(String _file) {
        try {

            this.mimeBodyPart.attachFile(new File(_file));
            this.message.setContent(this.multipart);

        } catch (MessagingException | IOException e) {
            System.out.println("MailSender Error: " + e.getMessage());
        }
    }

    private Session MailServerConfiguration() {
        Properties properties = System.getProperties();

        // Setting up mail server 
        properties.setProperty(MAIL_SERVER, HOST);
        properties.setProperty(MAIL_PORT, "587"); // Use 465 for SSL, 587 for TLS
        properties.setProperty(MAIL_AUTH, "true");
        properties.setProperty(MAIL_STARTTLS, "true");

        //Mail Server Configuration 
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("tarumtmoviesociety@gmail.com", "zpyn szmr uxqy vwkk");
            }
        });

    }

    private void MailInitialization(Session _session) {
        try {
            this.message = new MimeMessage(_session);
            this.mimeBodyPart = new MimeBodyPart();
            this.multipart = new MimeMultipart();

            this.message.setFrom(new InternetAddress(this.Sender));
            this.message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.Recipient));
            this.message.setSubject(this.Subject);
            this.mimeBodyPart.setContent(this.Body, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            System.out.println("MailSender Error: " + e.getMessage());
        }
    }

    public MailSender() {
    }

    public MailSender(String _Sender, String _Recipient, String _Subject, String _Body) {
        this.Sender = _Sender;
        this.Recipient = _Recipient;
        this.Subject = _Subject;
        this.Body = _Body;

        Session session = this.MailServerConfiguration();
        this.MailInitialization(session);

    }

    public MailSender(String _Sender, String _Recipient, String _Subject, MailTemplate _mailTemplate) {
        this.Sender = _Sender;
        this.Recipient = _Recipient;
        this.Subject = _Subject;
        this.Body = _mailTemplate.getTemplate();

        Session session = this.MailServerConfiguration();
        this.MailInitialization(session);
    }

}
