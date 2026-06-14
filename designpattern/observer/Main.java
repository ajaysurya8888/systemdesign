package observer;

public class Main {

    public static void main(String[] args) {
        // Create products
        Product iPhone = new Product("iPhone 16");
        Product laptop = new Product("MacBook Pro");

        // Create users (observers)
        User alice = new User("Alice");
        User bob   = new User("Bob");
        User carol = new User("Carol");

        // Alice and Bob subscribe to iPhone alerts
        iPhone.addObserver(alice);
        iPhone.addObserver(bob);

        // Bob and Carol subscribe to laptop alerts
        laptop.addObserver(bob);
        laptop.addObserver(carol);

        System.out.println("=== iPhone 16 becomes available ===");
        iPhone.setAvailable(true);

        System.out.println("\n=== MacBook Pro becomes available ===");
        laptop.setAvailable(true);

        System.out.println("\n=== Alice unsubscribes from iPhone alerts ===");
        iPhone.removeObserver(alice);

        System.out.println("\n=== iPhone 16 goes out of stock ===");
        iPhone.setAvailable(false);

        System.out.println("\n=== MacBook Pro goes out of stock ===");
        laptop.setAvailable(false);
    }
}
