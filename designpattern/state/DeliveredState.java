package state;

public class DeliveredState implements DeliveryState {

    @Override
    public void next(DeliveryOrder order) {
        System.out.println("[DeliveredState]  → Order already delivered. No further transitions.");
    }

    @Override
    public String getStatus() { return "DELIVERED"; }
}