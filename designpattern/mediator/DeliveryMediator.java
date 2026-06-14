package mediator;

import java.util.ArrayList;
import java.util.List;

// Concrete Mediator — coordinates all colleagues, none of them talk to each other
public class DeliveryMediator implements LogisticsMediator {

    private Customer       customer;
    private Restaurant     restaurant;
    private List<Driver>   drivers    = new ArrayList<>();
    private TrackingSystem tracking;

    @Override
    public void registerCustomer(Customer customer)       { this.customer    = customer; }

    @Override
    public void registerRestaurant(Restaurant restaurant) { this.restaurant  = restaurant; }

    @Override
    public void registerDriver(Driver driver)             { drivers.add(driver); }

    @Override
    public void registerTracking(TrackingSystem tracking) { this.tracking    = tracking; }

    @Override
    public void notify(Colleague sender, String event, String orderId) {
        System.out.println("  [Mediator] Event received: " + event + " from " + sender.getName());

        switch (event) {

            case "ORDER_PLACED":
                tracking.updateStatus(orderId, "Order placed by " + customer.getName());
                restaurant.acceptOrder(orderId);
                break;

            case "ORDER_ACCEPTED":
                tracking.updateStatus(orderId, "Order accepted by " + restaurant.getName());
                customer.receiveNotification("Your order has been accepted by " + restaurant.getName() + "!");
                restaurant.markOrderReady(orderId);
                break;

            case "ORDER_READY":
                tracking.updateStatus(orderId, "Order ready for pickup");
                Driver assigned = findAvailableDriver();
                if (assigned != null) {
                    assigned.receiveAssignment("Pick up order " + orderId
                            + " from " + restaurant.getName()
                            + " → deliver to " + customer.getAddress());
                    customer.receiveNotification("Driver " + assigned.getName() + " is heading to the restaurant.");
                    tracking.updateStatus(orderId, "Driver " + assigned.getName() + " assigned");
                    assigned.pickUpOrder(orderId);
                } else {
                    System.out.println("  [Mediator] No drivers available. Order queued.");
                }
                break;

            case "ORDER_PICKED_UP":
                tracking.updateStatus(orderId, "Order picked up — out for delivery");
                customer.receiveNotification("Your order is out for delivery!");
                Driver driver = (Driver) sender;
                driver.deliverOrder(orderId);
                break;

            case "ORDER_DELIVERED":
                tracking.updateStatus(orderId, "Delivered to " + customer.getAddress());
                customer.receiveNotification("Your order has been delivered. Enjoy your meal!");
                restaurant.receiveNotification("Order " + orderId + " delivered successfully.");
                break;
        }
    }

    private Driver findAvailableDriver() {
        return drivers.stream()
                .filter(Driver::isAvailable)
                .findFirst()
                .orElse(null);
    }
}
