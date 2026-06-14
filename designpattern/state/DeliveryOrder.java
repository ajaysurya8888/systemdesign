package state;

// Context — holds current state and delegates transitions to it
public class DeliveryOrder {
    private final String orderId;
    private final String customerName;
    private DeliveryState currentState;

    public DeliveryOrder(String orderId, String customerName) {
        this.orderId      = orderId;
        this.customerName = customerName;
        this.currentState = new OrderedState();   // initial state
        System.out.println("Order placed — ID: " + orderId + " | Customer: " + customerName);
        System.out.println("Current Status: " + currentState.getStatus());
    }

    public void setState(DeliveryState state) {
        this.currentState = state;
        System.out.println("Current Status: " + currentState.getStatus());
    }

    public void nextStep() {
        currentState.next(this);
    }

    public String getStatus()      { return currentState.getStatus(); }
    public String getOrderId()     { return orderId; }
    public String getCustomerName(){ return customerName; }
}