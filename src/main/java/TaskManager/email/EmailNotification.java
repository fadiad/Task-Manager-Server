package TaskManager.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailNotification {
    private static String to;
    private static final String from = "graychairs1982@gmail.com";
    private static final Properties properties = System.getProperties();

    static Session session = Session.getInstance(properties, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            System.out.println();
            return new PasswordAuthentication("apikey", properties.getProperty("Aa123456789!"));
        }
    });

    private static MimeMessage prepare() {
        String host = "smtp.sendgrid.net";
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.fallback", "true");
        properties.put("spring.mail.password", "XXXXXXXXXX");
        session.setDebug(true);
        return new MimeMessage(session);
    }

    public static void sendEmailNotification(String mail, String text) { // The function that send the notification
        to = mail;
        MimeMessage message = prepare();

        try {
            prepareThisMessage(message, text);
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    private static void prepareThisMessage(MimeMessage message, String messageText) throws MessagingException {
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Hello, this is a massage from Trello application");
        message.setText(messageText);
    }

//    public static void main(String[] args) {
//        new EmailNotification().sendEmailNotification("saraysara1996@gmail.com", "text");
//    }
}
