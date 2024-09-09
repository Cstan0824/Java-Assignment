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

    private boolean MailStatus = false;

    // Getters and Setters
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

    public boolean getMailStatus() {
        return this.MailStatus;
    }

    public boolean Send() {
        MailStatus = false;
        try {
            // Set the email content to the message
            this.message.setContent(this.multipart);

            // Send the message
            Transport.send(this.message);
            MailStatus = true;
        } catch (MessagingException e) {
            System.out.println("MailSender Error: " + e.getMessage());
        }
        return MailStatus;
    }

    public void AttachFile(File _file) {
        try {
            // Create a new MimeBodyPart for the file
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(_file);

            // Add the file part to the multipart
            this.multipart.addBodyPart(attachmentPart);

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

        // Mail Server Configuration
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
            this.multipart = new MimeMultipart(); // Initialize multipart for the message

            this.message.setFrom(new InternetAddress(this.Sender));
            this.message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.Recipient));
            this.message.setSubject(this.Subject);

            // Create a body part for the email content
            this.mimeBodyPart = new MimeBodyPart();
            this.mimeBodyPart.setContent(this.Body, "text/html; charset=utf-8");

            // Add the body part to the multipart
            this.multipart.addBodyPart(this.mimeBodyPart);

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

    public MailSender(String _Recipient, String _Subject, MailTemplate _mailTemplate) {
        this.Recipient = _Recipient;
        this.Subject = _Subject;
        this.Body = _mailTemplate.getTemplate();

        Session session = this.MailServerConfiguration();
        this.MailInitialization(session);
    }

}
