package TaskManager.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

//@Component
public class EmailNotification {
    private static String to;
    private static final String from = "graychairs1982@gmail.com";
    //    @Value("password")
//    private String password;
    private static final Properties properties = System.getProperties();

    Session session = Session.getInstance(properties, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
//            Properties properties = new Properties();
//            try (InputStream inputStream = taskManager.pro.class.getResourceAsStream("/application.properties")) {
//
//                properties.load(inputStream);
//
//            } catch (IOException e) {
//                e.printStackTrace(System.out);
//            }
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
        properties.put("spring.mail.password", "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");
        session.setDebug(true);
        System.out.println("ssssssssssssssssss : " + properties.getProperty("spring.mail.password"));
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
