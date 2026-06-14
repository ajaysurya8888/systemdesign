package factory;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final String orderId;
    private final String customerName;
    private final List<String> items;
    private final double totalAmount;
    private final String status;
    private final LocalDateTime placedAt;

    public Order(String orderId, String customerName, List<String> items,
                 double totalAmount, String status, LocalDateTime placedAt) {
        this.orderId      = orderId;
        this.customerName = customerName;
        this.items        = items;
        this.totalAmount  = totalAmount;
        this.status       = status;
        this.placedAt     = placedAt;
    }

    public String getOrderId()       { return orderId; }
    public String getCustomerName()  { return customerName; }
    public List<String> getItems()   { return items; }
    public double getTotalAmount()   { return totalAmount; }
    public String getStatus()        { return status; }
    public LocalDateTime getPlacedAt() { return placedAt; }
}