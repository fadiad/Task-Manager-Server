package TaskManager.utils.emailUtils;


import TaskManager.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderFacade {
    @Autowired
    private EmailSenderService emailSenderService;

    public void sendEmail(String email) {
        SimpleMailMessage verificationEmail = createEmail(email);
        emailSenderService.sendEmail(verificationEmail);
    }

    private SimpleMailMessage createEmail(String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Trello Task Manager!");
        mailMessage.setFrom("taskmanagement.2023@gmail.com");
        mailMessage.setText("You have been added to a board please check your account" + "\nThank you...");
        return mailMessage;
    }


}