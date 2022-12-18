package TaskManager.utils.emailUtils;


import TaskManager.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailActivationFacade {
    @Autowired
    private EmailSenderService emailSenderService;

    public void sendVerificationEmail(String email) {
        SimpleMailMessage verificationEmail = createVerificationEmail(email);
        emailSenderService.sendEmail(verificationEmail);
    }

    private SimpleMailMessage createVerificationEmail(String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("wanikhaled27@gmail.com");
        mailMessage.setText("Thank you for registering to the ChatApp Application.\n"
                + "This is a verification email, please click the link to verify your email address\n"
                + "http://localhost:8080/auth/confirm-account?token="
                + "\nThank you...");
        return mailMessage;
    }


}