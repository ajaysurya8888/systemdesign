package templatemethod;

public class Main {

    public static void main(String[] args) {
        OrderFulfillment pizza = new PizzaOrderFulfillment();
        OrderFulfillment grocery = new GroceryOrderFulfillment();
        OrderFulfillment cloudKitchen = new CloudKitchenOrderFulfillment();

        System.out.println("=== Pizza Order ===");
        pizza.fulfillOrder();

        System.out.println("\n=== Grocery Order ===");
        grocery.fulfillOrder();

        System.out.println("\n=== Cloud Kitchen Order ===");
        cloudKitchen.fulfillOrder();
    }
}