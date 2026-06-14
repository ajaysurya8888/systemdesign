package proxy;

public class User {
    private final String name;
    private final String role;  // "ADMIN" or "USER"

    public User(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() { return name; }
    public String getRole() { return role; }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
}