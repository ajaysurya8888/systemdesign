package factory;

import java.util.List;

public class PDFExporter implements OrderExporter {

    @Override
    public void export(List<Order> orders) {
        System.out.println("--- Exporting as PDF ---");
        System.out.println("====================================");
        System.out.println("         ORDER REPORT (PDF)        ");
        System.out.println("====================================");
        for (Order o : orders) {
            System.out.println("Order ID    : " + o.getOrderId());
            System.out.println("Customer    : " + o.getCustomerName());
            System.out.println("Items       : " + String.join(", ", o.getItems()));
            System.out.printf ("Total       : ₹%.2f%n", o.getTotalAmount());
            System.out.println("Status      : " + o.getStatus());
            System.out.println("Placed At   : " + o.getPlacedAt());
            System.out.println("------------------------------------");
        }
        System.out.println("[PDF] " + orders.size() + " orders exported to orders.pdf");
    }

    @Override
    public String getFormat() { return "PDF"; }
}