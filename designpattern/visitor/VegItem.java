package visitor;

// Concrete Element — calls visitor.visit(this) — never changes for new operations
public class VegItem implements MenuItemElement {

    private final String name;
    private final double price;
    private final int    quantity;

    public VegItem(String name, double price, int quantity) {
        this.name     = name;
        this.price    = price;
        this.quantity = quantity;
    }

    @Override
    public void accept(Visitor visitor) { visitor.visit(this); }

    @Override public String getName()    { return name; }
    @Override public double getPrice()   { return price; }
    @Override public int getQuantity()   { return quantity; }
}
