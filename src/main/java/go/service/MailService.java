package go.service;

import eco.m1.annotate.Prop;
import eco.m1.annotate.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class MailService extends Thread {

    @Prop("business")
    private String business;

    @Prop("smtp.host")
    private String host;

    @Prop("smtp.port")
    private String port;

    @Prop("smtp.username")
    private String username;

    @Prop("smtp.password")
    private String password;

    @Prop("smtp.auth")
    private String auth;

    @Prop("smtp.starttls.enabled")
    private String starttls;

    private String protocol = "smtp";

    public boolean send(String to, String subject, String body){
        return send(to, username, subject, body);
    }

    public boolean send(String toAddress, String fromAddress, String subject, String emailBody){

        Properties props = new Properties();
        props.put("mail.smtp.auth",              auth);
        props.put("mail.smtp.starttls.enable",   starttls);
        props.put("mail.smtp.host",              host);
        props.put("mail.smtp.port",              port);
        props.put("mail.transport.protocol",     protocol);
        props.put("mail.smtp.timeout",           "10000");
        props.put("mail.smtp.connectiontimeout", "10000");


        // SSL Factory
        //props.put("mail.smtp.socketFactory.class",
        //        "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        //session.setDebug(true);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress, business));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            message.setSubject(subject);
            ((MimeMessage) message).setText(emailBody, "utf-8", "html");

            Transport.send(message);


        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }
}
