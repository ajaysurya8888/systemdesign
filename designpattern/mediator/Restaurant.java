package mediator;

public class Restaurant extends Colleague {

    private final String name;

    public Restaurant(String name, LogisticsMediator mediator) {
        super(mediator);
        this.name = name;
    }

    public void acceptOrder(String orderId) {
        System.out.println("[Restaurant - " + name + "] Order " + orderId + " accepted. Preparing...");
        mediator.notify(this, "ORDER_ACCEPTED", orderId);
    }

    public void markOrderReady(String orderId) {
        System.out.println("[Restaurant - " + name + "] Order " + orderId + " is ready for pickup.");
        mediator.notify(this, "ORDER_READY", orderId);
    }

    public void receiveNotification(String message) {
        System.out.println("[Restaurant - " + name + "] Notification: " + message);
    }

    @Override
    public String getName() { return name; }
}
