package mediator;

public class Customer extends Colleague {

    private final String name;
    private final String address;

    public Customer(String name, String address, LogisticsMediator mediator) {
        super(mediator);
        this.name    = name;
        this.address = address;
    }

    public void placeOrder(String orderId) {
        System.out.println("\n[Customer - " + name + "] Placing order: " + orderId);
        mediator.notify(this, "ORDER_PLACED", orderId);
    }

    public void receiveNotification(String message) {
        System.out.println("[Customer - " + name + "] Notification: " + message);
    }

    public String getAddress() { return address; }

    @Override
    public String getName() { return name; }
}
