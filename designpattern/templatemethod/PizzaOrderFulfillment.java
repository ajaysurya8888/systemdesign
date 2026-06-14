package templatemethod;

public class PizzaOrderFulfillment extends OrderFulfillment {

    @Override
    protected void receiveOrder() {
        System.out.println("[Pizza] Order received — customizing toppings.");
    }

    @Override
    protected void prepareFood() {
        System.out.println("[Pizza] Baking pizza at 400°F for 15 mins.");
    }

    @Override
    protected void packOrder() {
        System.out.println("[Pizza] Placing in insulated pizza box.");
    }
}