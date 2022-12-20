package TaskManager.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailNotification {
    private static String to;
    private static final String from = "graychairs1982@gmail.com";
    private static final Properties properties = System.getProperties();

    Session session = Session.getInstance(properties, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            System.out.println();
            return new PasswordAuthentication("apikey", properties.getProperty("spring.mail.password"));
        }
    });

    private MimeMessage prepare() {
        String host = "smtp.sendgrid.net";
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.fallback", "true");
        properties.put("spring.mail.password", "ENTER YOUR PASSWORD");
        session.setDebug(true);
        return new MimeMessage(session);
    }

    public void sendEmailNotification(String mail) { // The function that send the notification
        to = mail;
        MimeMessage message = prepare();
        String content = "enter your massage in the email notification";

        try {
            prepareThisMessage(message, content);
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    private void prepareThisMessage(MimeMessage message, String messageText) throws MessagingException {
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("welcome to the application!");
        message.setText(messageText);
    }

    public static void main(String[] args) {
        new EmailNotification().sendEmailNotification("saraysara1996@gmail.com");
    }
}
