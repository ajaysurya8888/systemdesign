package observer;

public class User implements Observer {

    private final String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void update(String productName, boolean isAvailable) {
        if (isAvailable) {
            System.out.println("Hey " + name + "! '" + productName + "' is now IN STOCK. Grab it before it's gone!");
        } else {
            System.out.println("Hey " + name + "! '" + productName + "' is now OUT OF STOCK.");
        }
    }

    public String getName() {
        return name;
    }
}
