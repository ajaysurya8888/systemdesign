package builder;

public class Main {

    public static void main(String[] args) {

        // --- User 1: only required fields (minimal signup) ---
        System.out.println("===== User 1: Minimal Signup =====");
        User basicUser = new User.Builder("Alice", "alice@gmail.com", "9876543210")
                .build();
        System.out.println(basicUser);

        // --- User 2: full profile ---
        System.out.println("\n===== User 2: Full Profile =====");
        User fullUser = new User.Builder("Bob", "bob@gmail.com", "9123456780")
                .address("12, Anna Nagar East")
                .city("Chennai")
                .pincode("600102")
                .profilePicUrl("https://cdn.foodapp.com/users/bob.jpg")
                .isVeg(true)
                .preferredCuisine("South Indian")
                .referralCode("BOB2024")
                .loyaltyPoints(500)
                .build();
        System.out.println(fullUser);

        // --- User 3: partial profile (city + veg preference only) ---
        System.out.println("\n===== User 3: Partial Profile =====");
        User partialUser = new User.Builder("Carol", "carol@gmail.com", "9988776655")
                .city("Coimbatore")
                .isVeg(false)
                .preferredCuisine("Biryani")
                .loyaltyPoints(150)
                .build();
        System.out.println(partialUser);

        // --- User 4: referred user ---
        System.out.println("\n===== User 4: Referred User =====");
        User referredUser = new User.Builder("Dave", "dave@gmail.com", "9090909090")
                .address("45, T.Nagar")
                .city("Chennai")
                .pincode("600017")
                .referralCode("ALICE500")
                .loyaltyPoints(100)
                .build();
        System.out.println(referredUser);

        // --- Validation: missing required field ---
        System.out.println("\n===== Validation: Missing Required Field =====");
        try {
            User invalid = new User.Builder("", "test@gmail.com", "9999999999").build();
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}