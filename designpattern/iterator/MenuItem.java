package iterator;

public class MenuItem {
    private final String name;
    private final String category;
    private final double price;
    private final boolean isVeg;

    public MenuItem(String name, String category, double price, boolean isVeg) {
        this.name     = name;
        this.category = category;
        this.price    = price;
        this.isVeg    = isVeg;
    }

    public String getName()     { return name; }
    public String getCategory() { return category; }
    public double getPrice()    { return price; }
    public boolean isVeg()      { return isVeg; }

    @Override
    public String toString() {
        return String.format("%-25s | %-12s | ₹%-7.2f | %s",
                name, category, price, isVeg ? "[VEG]" : "[NON-VEG]");
    }
}
