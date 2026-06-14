package facade;

// FACADE — single entry point for user registration
// Client calls one method; facade coordinates all 6 subsystems internally
public class AuthFacade {

    private final ValidationService validationService = new ValidationService();
    private final PasswordService   passwordService   = new PasswordService();
    private final UserRepository    userRepository    = new UserRepository();
    private final EmailService      emailService      = new EmailService();
    private final LoyaltyService    loyaltyService    = new LoyaltyService();
    private final AnalyticsService  analyticsService  = new AnalyticsService();

    public User register(String name, String email, String password) {
        System.out.println("-------- Registration Started --------");

        // Step 1 — validate input
        validationService.validate(name, email, password);

        // Step 2 — hash password
        String hashedPassword = passwordService.hashPassword(password);

        // Step 3 — save user
        User user = userRepository.save(name, email, hashedPassword);

        // Step 4 — send welcome email
        emailService.sendWelcomeMail(name, email);

        // Step 5 — create loyalty wallet
        loyaltyService.createWallet(user.getUserId(), name);

        // Step 6 — track signup event
        analyticsService.trackSignup(user.getUserId(), email);

        System.out.println("-------- Registration Complete --------");
        return user;
    }
}