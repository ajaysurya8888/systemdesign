package templatemethod;

public class GroceryOrderFulfillment extends OrderFulfillment {

    @Override
    protected void receiveOrder() {
        System.out.println("[Grocery] Order received — checking inventory.");
    }

    @Override
    protected void prepareFood() {
        System.out.println("[Grocery] Picking items from shelves.");
    }

    @Override
    protected void packOrder() {
        System.out.println("[Grocery] Packing in bags by category.");
    }

    // Grocery supports self-pickup — no delivery agent needed
    @Override
    protected void assignDeliveryAgent() {
        System.out.println("[Grocery] Self-pickup selected — no agent needed.");
    }
}