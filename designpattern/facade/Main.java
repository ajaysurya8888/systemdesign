package facade;

public class Main {

    public static void main(String[] args) {

        AuthFacade authFacade = new AuthFacade();

        // --- Successful registration ---
        System.out.println("========== User 1: Full Registration ==========");
        User alice = authFacade.register("Alice", "alice@foodapp.com", "alice@123");
        System.out.println("\nRegistered User → ID: " + alice.getUserId()
                + " | Name: " + alice.getName()
                + " | Email: " + alice.getEmail());

        System.out.println("\n========== User 2: Another Registration ==========");
        User bob = authFacade.register("Bob", "bob@foodapp.com", "bob@456");
        System.out.println("\nRegistered User → ID: " + bob.getUserId()
                + " | Name: " + bob.getName()
                + " | Email: " + bob.getEmail());

        // --- Invalid email ---
        System.out.println("\n========== User 3: Invalid Email ==========");
        try {
            authFacade.register("Carol", "carol-invalid", "carol@789");
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }

        // --- Weak password ---
        System.out.println("\n========== User 4: Weak Password ==========");
        try {
            authFacade.register("Dave", "dave@foodapp.com", "123");
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }
}