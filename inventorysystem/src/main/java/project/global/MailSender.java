package project.global;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;



public class MailSender {
    private String Sender;
    private String Recipient;
    private String Subject;
    private String Body;

    private MimeMessage message;
    private MimeBodyPart mimeBodyPart;
    private Multipart multipart;

    private final String HOST = "localhost";
    private final String MAIL_SERVER = "mail.smtp.host";

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

    public boolean Send() {
        try {
            this.multipart.addBodyPart(this.mimeBodyPart);
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

    private void MailConfiguration() {
        Properties properties = System.getProperties();

        // Setting up mail server 
        properties.setProperty(MAIL_SERVER, HOST);

        try {
            // creating session object to get properties 
            Session session = Session.getDefaultInstance(properties);

            this.message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.Sender));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(this.Recipient));
            message.setSubject(this.Subject);

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

        this.MailConfiguration();

    }

    public MailSender(String _Sender, String _Recipient, String _Subject, MailTemplate _mailTemplate) {
        this.Sender = _Sender;
        this.Recipient = _Recipient;
        this.Subject = _Subject;
        this.Body = _mailTemplate.getTemplate();

        this.MailConfiguration();
    }

}
