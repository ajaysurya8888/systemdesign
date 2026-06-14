package flyweight;

// Context — holds EXTRINSIC state (unique per restaurant per item)
public class RestaurantMenuItem {

    private final MenuItemFlyweight flyweight;  // shared
    private final String restaurantName;        // extrinsic
    private final double price;                 // extrinsic
    private final boolean isAvailable;          // extrinsic

    public RestaurantMenuItem(MenuItemFlyweight flyweight, String restaurantName,
                               double price, boolean isAvailable) {
        this.flyweight      = flyweight;
        this.restaurantName = restaurantName;
        this.price          = price;
        this.isAvailable    = isAvailable;
    }

    public void display() {
        flyweight.display(restaurantName, price, isAvailable);
    }
}
