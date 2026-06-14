package facade;

// Subsystem 2 — hashes the plain-text password
public class PasswordService {

    public String hashPassword(String plainPassword) {
        System.out.println("[PasswordService]   Hashing password...");
        // Simulated hash (real app would use BCrypt)
        String hashed = "HASHED_" + Integer.toHexString(plainPassword.hashCode()).toUpperCase();
        System.out.println("[PasswordService]   Password hashed successfully.");
        return hashed;
    }
}