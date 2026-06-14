package facade;

// Subsystem 4 — sends welcome email after registration
public class EmailService {

    public void sendWelcomeMail(String name, String email) {
        System.out.println("[EmailService]      Sending welcome email to " + email + "...");
        System.out.println("[EmailService]      Subject: Welcome to FoodApp, " + name + "!");
        System.out.println("[EmailService]      Email sent successfully.");
    }
}