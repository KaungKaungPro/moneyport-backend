package sg.nus.iss.adproject.services.forum;

public interface EmailService {

    void sendEmail(String to, String subject, String content);
}
