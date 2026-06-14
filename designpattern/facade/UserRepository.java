package facade;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Subsystem 3 — persists the user to the database
public class UserRepository {

    private final Map<String, User> store = new HashMap<>();

    public User save(String name, String email, String hashedPassword) {
        System.out.println("[UserRepository]    Saving user to database...");
        String userId = "USR_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        User user = new User(userId, name, email, hashedPassword);
        store.put(userId, user);
        System.out.println("[UserRepository]    User saved. ID: " + userId);
        return user;
    }

    public User findById(String userId) {
        return store.get(userId);
    }
}