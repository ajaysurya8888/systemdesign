package command;

public class CartItem {
    private final String name;
    private final double price;
    private int quantity;

    public CartItem(String name, double price, int quantity) {
        this.name     = name;
        this.price    = price;
        this.quantity = quantity;
    }

    public String getName()     { return name; }
    public double getPrice()    { return price; }
    public int getQuantity()    { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotal()    { return price * quantity; }

    @Override
    public String toString() {
        return name + " x" + quantity + " (₹" + price + " each)";
    }
}
