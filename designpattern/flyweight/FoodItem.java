package flyweight;

// Concrete Flyweight — stores INTRINSIC state (shared across all restaurants)
public class FoodItem implements MenuItemFlyweight {

    // Intrinsic — same for every restaurant that serves this item
    private final String name;
    private final String cuisine;
    private final String category;
    private final boolean isVeg;
    private final String imageUrl;

    public FoodItem(String name, String cuisine, String category,
                    boolean isVeg, String imageUrl) {
        this.name     = name;
        this.cuisine  = cuisine;
        this.category = category;
        this.isVeg    = isVeg;
        this.imageUrl = imageUrl;
        System.out.println("[FoodItem] Created flyweight for: " + name);
    }

    // Extrinsic state (restaurantName, price, isAvailable) passed in by client
    @Override
    public void display(String restaurantName, double price, boolean isAvailable) {
        System.out.printf("  %-25s | %-20s | %-12s | %-10s | ₹%-8.2f | %s | %s%n",
                restaurantName,
                name,
                cuisine,
                category,
                price,
                isVeg ? "[VEG]" : "[NON-VEG]",
                isAvailable ? "Available" : "Unavailable");
    }

    public String getName() { return name; }
}
