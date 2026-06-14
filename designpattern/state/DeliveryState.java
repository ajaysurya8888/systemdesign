package state;

public interface DeliveryState {
    void next(DeliveryOrder order);
    String getStatus();
}