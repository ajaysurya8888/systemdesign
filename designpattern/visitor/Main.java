package visitor;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Order items — element classes never change for new operations
        List<MenuItemElement> orderItems = Arrays.asList(
                new VegItem      ("Masala Dosa",          70.0,  2),
                new VegItem      ("Paneer Butter Masala", 180.0, 1),
                new NonVegItem   ("Chicken Biryani",      280.0, 2),
                new NonVegItem   ("Mutton Curry",         320.0, 1),
                new BeverageItem ("Mango Lassi",           60.0, 2),
                new BeverageItem ("Filter Coffee",         30.0, 1),
                new AlcoholicItem("Kingfisher Beer",      150.0, 2)
        );

        // ===== Visitor 1: Tax Calculator =====
        System.out.println("========== TAX BREAKDOWN ==========");
        System.out.printf("  %-25s | %-8s | %-15s%n", "Item", "Tax Rate", "Subtotal → Tax");
        System.out.println("  " + "-".repeat(60));

        TaxCalculatorVisitor taxVisitor = new TaxCalculatorVisitor();
        for (MenuItemElement item : orderItems) {
            item.accept(taxVisitor);       // each item tells visitor its type via accept()
        }
        System.out.printf("%n  Total Tax Collected: ₹%.2f%n", taxVisitor.getTotalTax());

        // ===== Visitor 2: Invoice Generator =====
        System.out.println("\n========== INVOICE ==========");
        InvoiceGeneratorVisitor invoiceVisitor = new InvoiceGeneratorVisitor();
        invoiceVisitor.printHeader("ORD2024001", "Alice", "The Grand Food Court");

        for (MenuItemElement item : orderItems) {
            item.accept(invoiceVisitor);   // same elements, different visitor, different operation
        }

        invoiceVisitor.printFooter(30.0, "SAVE50", 50.0);
        invoiceVisitor.printInvoice();

        // ===== Key point: element classes were never modified =====
        System.out.println("========== Pattern Summary ==========");
        System.out.println("  Same 7 item objects → accepted 2 different visitors");
        System.out.println("  TaxCalculatorVisitor  → computed tax per type");
        System.out.println("  InvoiceGeneratorVisitor → generated full invoice");
        System.out.println("  Zero changes to VegItem / NonVegItem / BeverageItem / AlcoholicItem");
    }
}
