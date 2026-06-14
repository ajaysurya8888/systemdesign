package factory;

import java.util.List;

public class ExcelExporter implements OrderExporter {

    @Override
    public void export(List<Order> orders) {
        System.out.println("--- Exporting as Excel ---");
        System.out.printf("%-12s %-15s %-30s %-10s %-12s %-20s%n",
                "Order ID", "Customer", "Items", "Total(₹)", "Status", "Placed At");
        System.out.println("-".repeat(105));
        for (Order o : orders) {
            System.out.printf("%-12s %-15s %-30s %-10.2f %-12s %-20s%n",
                    o.getOrderId(),
                    o.getCustomerName(),
                    String.join(", ", o.getItems()),
                    o.getTotalAmount(),
                    o.getStatus(),
                    o.getPlacedAt());
        }
        System.out.println("[Excel] " + orders.size() + " orders exported to orders.xlsx");
    }

    @Override
    public String getFormat() { return "EXCEL"; }
}