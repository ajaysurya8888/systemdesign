package command;

public class Main {

    static void printCart(Cart cart) {
        System.out.println("  Cart:");
        cart.printCart();
    }

    public static void main(String[] args) {

        Cart cart         = new Cart();
        CartInvoker invoker = new CartInvoker();

        // ===== Add items =====
        System.out.println("========== Adding Items ==========");
        invoker.executeCommand(new AddItemCommand(cart, new CartItem("Chicken Biryani", 280.0, 1)));
        invoker.executeCommand(new AddItemCommand(cart, new CartItem("Masala Dosa",      70.0, 2)));
        invoker.executeCommand(new AddItemCommand(cart, new CartItem("Paneer Butter Masala", 180.0, 1)));
        printCart(cart);

        // ===== Apply coupon =====
        System.out.println("\n========== Apply Coupon ==========");
        invoker.executeCommand(new ApplyCouponCommand(cart, "SAVE50", 50.0));
        printCart(cart);

        // ===== Undo coupon =====
        System.out.println("\n========== Undo Apply Coupon ==========");
        invoker.undo();
        printCart(cart);

        // ===== Redo coupon =====
        System.out.println("\n========== Redo Apply Coupon ==========");
        invoker.redo();
        printCart(cart);

        // ===== Remove an item =====
        System.out.println("\n========== Remove Item ==========");
        invoker.executeCommand(new RemoveItemCommand(cart, "Masala Dosa"));
        printCart(cart);

        // ===== Undo remove =====
        System.out.println("\n========== Undo Remove ==========");
        invoker.undo();
        printCart(cart);

        // ===== Clear cart =====
        System.out.println("\n========== Clear Cart ==========");
        invoker.executeCommand(new ClearCartCommand(cart));
        printCart(cart);

        // ===== Undo clear =====
        System.out.println("\n========== Undo Clear Cart ==========");
        invoker.undo();
        printCart(cart);

        // ===== Multiple undos =====
        System.out.println("\n========== Multiple Undos ==========");
        invoker.undo();   // undo remove item
        invoker.undo();   // undo coupon
        invoker.undo();   // undo add Paneer
        printCart(cart);

        // ===== Redo =====
        System.out.println("\n========== Redo ==========");
        invoker.redo();   // redo add Paneer
        printCart(cart);

        // ===== Undo history =====
        invoker.printHistory();

        // ===== Nothing left to undo =====
        System.out.println("\n========== Undo beyond history ==========");
        invoker.undo();
        invoker.undo();
        invoker.undo();
        invoker.undo();
        invoker.undo();
    }
}
