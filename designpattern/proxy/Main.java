package proxy;

public class Main {

    public static void main(String[] args) {

        Product iPhone   = new Product("iPhone 16", 999.99, "Electronics");
        Product laptop   = new Product("MacBook Pro", 2499.99, "Electronics");
        Product sneakers = new Product("Nike Air Max", 149.99, "Footwear");

        // --- Admin user ---
        User adminUser = new User("Alice", "ADMIN");
        ProductService adminService = new ProductServiceProxy(adminUser);

        System.out.println("=== Admin tries to add products ===");
        adminService.addProduct(iPhone);
        adminService.addProduct(laptop);

        System.out.println("\nCurrent products: " + adminService.getAllProducts());

        // --- Regular user ---
        User regularUser = new User("Bob", "USER");
        ProductService userService = new ProductServiceProxy(regularUser);

        System.out.println("\n=== Regular user tries to add a product ===");
        userService.addProduct(sneakers);

        System.out.println("\nCurrent products (unchanged): " + adminService.getAllProducts());
    }
}