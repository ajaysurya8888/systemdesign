package facade;

// Subsystem 5 — creates a loyalty wallet for the new user
public class LoyaltyService {

    public void createWallet(String userId, String name) {
        System.out.println("[LoyaltyService]    Creating loyalty wallet for " + name + "...");
        System.out.println("[LoyaltyService]    Wallet created. Starting balance: 100 points (welcome bonus).");
    }
}