package templatemethod;

public abstract class OrderFulfillment {

    // Template method — fixed order, subclasses cannot reorder steps
    public final void fulfillOrder() {
        receiveOrder();
        prepareFood();
        packOrder();
        assignDeliveryAgent();
        deliverOrder();
        sendNotification();
    }

    protected abstract void receiveOrder();
    protected abstract void prepareFood();
    protected abstract void packOrder();

    protected void assignDeliveryAgent() {
        System.out.println("Assigning nearest available delivery agent...");
    }

    protected void deliverOrder() {
        System.out.println("Delivering to customer address...");
    }

    protected void sendNotification() {
        System.out.println("Sending 'Order Delivered' notification via app.");
    }
}