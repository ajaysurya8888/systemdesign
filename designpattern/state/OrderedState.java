package state;

public class OrderedState implements DeliveryState {

    @Override
    public void next(DeliveryOrder order) {
        System.out.println("[OrderedState]    → Order confirmed. Moving to In Progress...");
        order.setState(new InProgressState());
    }

    @Override
    public String getStatus() { return "ORDERED"; }
}