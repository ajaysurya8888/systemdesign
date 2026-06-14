package facade;

public class User {
    private final String userId;
    private final String name;
    private final String email;
    private final String hashedPassword;

    public User(String userId, String name, String email, String hashedPassword) {
        this.userId         = userId;
        this.name           = name;
        this.email          = email;
        this.hashedPassword = hashedPassword;
    }

    public String getUserId()        { return userId; }
    public String getName()          { return name; }
    public String getEmail()         { return email; }
    public String getHashedPassword(){ return hashedPassword; }
}