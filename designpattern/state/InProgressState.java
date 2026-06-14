package state;

public class InProgressState implements DeliveryState {

    @Override
    public void next(DeliveryOrder order) {
        System.out.println("[InProgressState] → Order picked up. Out for delivery...");
        order.setState(new DeliveredState());
    }

    @Override
    public String getStatus() { return "IN_PROGRESS"; }
}