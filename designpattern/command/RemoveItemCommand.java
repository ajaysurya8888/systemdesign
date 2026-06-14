package command;

public class RemoveItemCommand implements Command {

    private final Cart cart;
    private final String itemName;
    private CartItem removedItem;   // saved for undo

    public RemoveItemCommand(Cart cart, String itemName) {
        this.cart     = cart;
        this.itemName = itemName;
    }

    @Override
    public void execute() {
        removedItem = cart.findItem(itemName);  // snapshot before removing
        cart.removeItem(itemName);
    }

    @Override
    public void undo() {
        if (removedItem != null) {
            cart.addItem(removedItem);
        }
    }

    @Override
    public String getDescription() {
        return "Remove [" + itemName + "]";
    }
}
