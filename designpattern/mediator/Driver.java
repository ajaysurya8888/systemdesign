package mediator;

public class Driver extends Colleague {

    private final String name;
    private boolean available;

    public Driver(String name, LogisticsMediator mediator) {
        super(mediator);
        this.name      = name;
        this.available = true;
    }

    public void pickUpOrder(String orderId) {
        this.available = false;
        System.out.println("[Driver - " + name + "] Picked up order " + orderId + ". Heading to customer...");
        mediator.notify(this, "ORDER_PICKED_UP", orderId);
    }

    public void deliverOrder(String orderId) {
        this.available = true;
        System.out.println("[Driver - " + name + "] Order " + orderId + " delivered successfully.");
        mediator.notify(this, "ORDER_DELIVERED", orderId);
    }

    public void receiveAssignment(String message) {
        System.out.println("[Driver - " + name + "] Assignment: " + message);
    }

    public boolean isAvailable() { return available; }

    @Override
    public String getName() { return name; }
}
