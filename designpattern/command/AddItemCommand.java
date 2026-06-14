package command;

public class AddItemCommand implements Command {

    private final Cart cart;
    private final CartItem item;

    public AddItemCommand(Cart cart, CartItem item) {
        this.cart = cart;
        this.item = item;
    }

    @Override
    public void execute() {
        cart.addItem(item);
    }

    @Override
    public void undo() {
        cart.removeItem(item.getName());
    }

    @Override
    public String getDescription() {
        return "Add [" + item.getName() + " x" + item.getQuantity() + "]";
    }
}
