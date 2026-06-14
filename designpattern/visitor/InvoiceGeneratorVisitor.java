package visitor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Concrete Visitor 2 — generates formatted invoice line per item type
public class InvoiceGeneratorVisitor implements Visitor {

    private final StringBuilder invoice = new StringBuilder();
    private double subtotal  = 0.0;
    private double totalTax  = 0.0;

    private static final String LINE = "-".repeat(70);

    public void printHeader(String orderId, String customerName, String restaurant) {
        invoice.append("\n").append("=".repeat(70)).append("\n");
        invoice.append("              FOOD DELIVERY — TAX INVOICE\n");
        invoice.append("=".repeat(70)).append("\n");
        invoice.append(String.format("  Order ID   : %s%n", orderId));
        invoice.append(String.format("  Customer   : %s%n", customerName));
        invoice.append(String.format("  Restaurant : %s%n", restaurant));
        invoice.append(String.format("  Date       : %s%n",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
        invoice.append(LINE).append("\n");
        invoice.append(String.format("  %-22s %-6s %-10s %-8s %-10s%n",
                "Item", "Qty", "Price", "Tax%", "Total"));
        invoice.append(LINE).append("\n");
    }

    @Override
    public void visit(VegItem item) {
        appendLine(item, 5.0, "[VEG]");
    }

    @Override
    public void visit(NonVegItem item) {
        appendLine(item, 12.0, "[NON-VEG]");
    }

    @Override
    public void visit(BeverageItem item) {
        appendLine(item, 18.0, "[BEVERAGE]");
    }

    @Override
    public void visit(AlcoholicItem item) {
        appendLine(item, 28.0, "[ALCOHOLIC]");
    }

    private void appendLine(MenuItemElement item, double taxRate, String tag) {
        double itemSubtotal = item.getPrice() * item.getQuantity();
        double tax          = itemSubtotal * taxRate / 100.0;
        double total        = itemSubtotal + tax;

        subtotal += itemSubtotal;
        totalTax += tax;

        invoice.append(String.format("  %-22s %-6d ₹%-9.2f %-8s ₹%-9.2f  %s%n",
                item.getName(),
                item.getQuantity(),
                item.getPrice(),
                taxRate + "%",
                total,
                tag));
    }

    public void printFooter(double deliveryCharge, String couponCode, double discount) {
        invoice.append(LINE).append("\n");
        invoice.append(String.format("  %-38s ₹%.2f%n", "Subtotal:",        subtotal));
        invoice.append(String.format("  %-38s ₹%.2f%n", "Total GST:",       totalTax));
        invoice.append(String.format("  %-38s ₹%.2f%n", "Delivery Charge:", deliveryCharge));
        if (discount > 0) {
            invoice.append(String.format("  %-38s -₹%.2f  [%s]%n",
                    "Discount:", discount, couponCode));
        }
        invoice.append(LINE).append("\n");
        double grandTotal = subtotal + totalTax + deliveryCharge - discount;
        invoice.append(String.format("  %-38s ₹%.2f%n", "GRAND TOTAL:", grandTotal));
        invoice.append("=".repeat(70)).append("\n");
        invoice.append("  Thank you for your order! Enjoy your meal.\n");
        invoice.append("=".repeat(70)).append("\n");
    }

    public void printInvoice() {
        System.out.println(invoice.toString());
    }

    public double getSubtotal() { return subtotal; }
    public double getTotalTax() { return totalTax; }
}
