package mediator;

// Mediator interface — all colleagues talk through this
public interface LogisticsMediator {
    void notify(Colleague sender, String event, String orderId);
    void registerCustomer(Customer customer);
    void registerRestaurant(Restaurant restaurant);
    void registerDriver(Driver driver);
    void registerTracking(TrackingSystem tracking);
}
