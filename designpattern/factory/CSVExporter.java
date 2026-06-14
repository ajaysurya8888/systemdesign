package factory;

import java.util.List;

public class CSVExporter implements OrderExporter {

    @Override
    public void export(List<Order> orders) {
        System.out.println("--- Exporting as CSV ---");
        System.out.println("OrderId,CustomerName,Items,TotalAmount,Status,PlacedAt");
        for (Order o : orders) {
            System.out.printf("%s,%s,\"%s\",%.2f,%s,%s%n",
                    o.getOrderId(),
                    o.getCustomerName(),
                    String.join("|", o.getItems()),
                    o.getTotalAmount(),
                    o.getStatus(),
                    o.getPlacedAt());
        }
        System.out.println("[CSV] " + orders.size() + " orders exported to orders.csv");
    }

    @Override
    public String getFormat() { return "CSV"; }
}