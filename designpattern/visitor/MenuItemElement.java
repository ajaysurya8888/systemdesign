package visitor;

// Element interface — every menu item must accept a visitor
public interface MenuItemElement {
    void accept(Visitor visitor);
    String getName();
    double getPrice();
    int getQuantity();
}
