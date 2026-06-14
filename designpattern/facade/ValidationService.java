package facade;

// Subsystem 1 — validates user input
public class ValidationService {

    public void validate(String name, String email, String password) {
        System.out.println("[ValidationService] Validating user input...");

        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name cannot be empty.");

        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email: " + email);

        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("Password must be at least 6 characters.");

        System.out.println("[ValidationService] Validation passed.");
    }
}