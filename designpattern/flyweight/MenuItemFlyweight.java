package flyweight;

// Flyweight interface — operation receives extrinsic state from client
public interface MenuItemFlyweight {
    void display(String restaurantName, double price, boolean isAvailable);
}
