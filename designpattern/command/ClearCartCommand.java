package command;

import java.util.List;

public class ClearCartCommand implements Command {

    private final Cart cart;
    private List<CartItem> snapshot;   // saved for undo

    public ClearCartCommand(Cart cart) {
        this.cart = cart;
    }

    @Override
    public void execute() {
        snapshot = cart.clearAll();   // save before clearing
    }

    @Override
    public void undo() {
        cart.restoreAll(snapshot);
    }

    @Override
    public String getDescription() {
        return "Clear Cart";
    }
}
