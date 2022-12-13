//package chatApp.email;
//
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.googleapis.json.GoogleJsonError;
//import com.google.api.client.googleapis.json.GoogleJsonResponseException;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.gmail.Gmail;
//import com.google.api.services.gmail.model.Message;
//import org.apache.commons.codec.binary.Base64;
//import org.springframework.stereotype.Service;
//
//import javax.mail.MessagingException;
//import javax.mail.Multipart;
//import javax.mail.Session;
//import javax.mail.internet.*;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.file.AccessDeniedException;
//import java.nio.file.Paths;
//import java.security.GeneralSecurityException;
//import java.util.Objects;
//import java.util.Properties;
//import java.util.Set;
//
//import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
//import static javax.mail.Message.RecipientType.TO;
//
//@Service
//public class GMailer {
//
//    private final Gmail service;
//    private static final String link = "http://localhost:8080/register/confirm?token=";
//    private final String ERROR_MESSAGE="Mail wasn't sent something went wrong! ";
//
//    public GMailer()  {
//        NetHttpTransport httpTransport = null;
//        try {
//            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//            GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
//            service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
//                    .setApplicationName("Test Mailer")
//                    .build();
//        } catch (GeneralSecurityException | IOException e) {
//            throw new RuntimeException(ERROR_MESSAGE+e.getMessage(),e);
//        }
//    }
//
//    /**
//     * Create an authorized credential object
//     * @param httpTransport the network httpTransport
//     * @param jsonFactory json data
//     * @return an authorized credential object
//     * @throws IOException if the json data cannot be loaded
//     */
//    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory) throws IOException {
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(Objects.requireNonNull(GMailer.class.getResourceAsStream("/client_secret_628614613092-ehr33pa4c20535q5qk007ms2n4go8kjm.apps.googleusercontent.com.json"))));
//
//        //Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
//                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
//                .setAccessType("offline")
//                .build();
//
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//    }
//
//    /**
//     * Creates an email confirmation with the given subject and token
//     *      and sends it to the given email (to)
//     * @param subject the email subject
//     * @param token the user's confirmation token
//     * @param to the email address to send the email to
//     * @throws AccessDeniedException if the message was not send
//     */
//    public void sendMail(String subject, String token, String to) throws AccessDeniedException {
//
//        try {
//            Properties props = new Properties();
//            Session session = Session.getDefaultInstance(props, null);
//            MimeMessage email = new MimeMessage(session);
//            email.setFrom(new InternetAddress("chatappgroup11@gmail.com"));
//            email.addRecipient(TO, new InternetAddress(to));
//            email.setSubject(subject);
//            Multipart multipart = new MimeMultipart("mixed");
//            MimeBodyPart htmlBodyPart = new MimeBodyPart();
//            String htmlString = EmailBuilder.buildEmail("khaledwaaaaani", link + token + "&email=" + to);
//            htmlBodyPart.setContent(htmlString, "text/html; charset=utf-8");
//            multipart.addBodyPart(htmlBodyPart, 0);
//            email.setContent(multipart);
//            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//            email.writeTo(buffer);
//            byte[] rawMessageBytes = buffer.toByteArray();
//            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
//            Message msg = new Message();
//            msg.setRaw(encodedEmail);
//            msg = service.users().messages().send("me", msg).execute();
//            System.out.println("Message id: " + msg.getId());
//            System.out.println(msg.toPrettyString());
//
//        } catch (GoogleJsonResponseException e) {
//            GoogleJsonError error = e.getDetails();
//            if (error.getCode() == 403) {
//                System.err.println("Unable to send message: " + e.getDetails());
//                throw new AccessDeniedException(ERROR_MESSAGE + e.getDetails());
//            } else {
//                throw new RuntimeException(ERROR_MESSAGE+e.getMessage(), e);
//            }
//        } catch (MessagingException | IOException e) {
//            throw new RuntimeException(ERROR_MESSAGE+e.getMessage(),e);
//        }
//    }
//}