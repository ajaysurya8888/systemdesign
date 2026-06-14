package visitor;

// Concrete Visitor 1 — tax calculation logic per item type
// VegItem: 5% | NonVegItem: 12% | BeverageItem: 18% | AlcoholicItem: 28%
public class TaxCalculatorVisitor implements Visitor {

    private double totalTax = 0.0;

    @Override
    public void visit(VegItem item) {
        double tax = calculateTax(item, 5.0);
        System.out.printf("  %-25s | GST  5%% | ₹%6.2f → Tax: ₹%.2f%n",
                item.getName(), item.getPrice() * item.getQuantity(), tax);
    }

    @Override
    public void visit(NonVegItem item) {
        double tax = calculateTax(item, 12.0);
        System.out.printf("  %-25s | GST 12%% | ₹%6.2f → Tax: ₹%.2f%n",
                item.getName(), item.getPrice() * item.getQuantity(), tax);
    }

    @Override
    public void visit(BeverageItem item) {
        double tax = calculateTax(item, 18.0);
        System.out.printf("  %-25s | GST 18%% | ₹%6.2f → Tax: ₹%.2f%n",
                item.getName(), item.getPrice() * item.getQuantity(), tax);
    }

    @Override
    public void visit(AlcoholicItem item) {
        double tax = calculateTax(item, 28.0);
        System.out.printf("  %-25s | GST 28%% | ₹%6.2f → Tax: ₹%.2f%n",
                item.getName(), item.getPrice() * item.getQuantity(), tax);
    }

    private double calculateTax(MenuItemElement item, double taxRate) {
        double subtotal = item.getPrice() * item.getQuantity();
        double tax      = subtotal * taxRate / 100.0;
        totalTax       += tax;
        return tax;
    }

    public double getTotalTax() { return totalTax; }

    public void reset() { totalTax = 0.0; }
}
