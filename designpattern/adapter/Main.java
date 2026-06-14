package adapter;

public class Main {

    // Client only knows WeightInKG interface
    static void printWeightInKG(String itemName, WeightInKG weightProvider) {
        double kg = weightProvider.getWeightInKG();
        System.out.printf("  %-20s → %.4f kg%n", itemName, kg);
    }

    public static void main(String[] args) {

        System.out.println("========== Direct KG weights ==========");
        // Items already in KG — no adapter needed (implement WeightInKG directly)
        WeightInKG appleKG  = () -> 0.5;
        WeightInKG laptopKG = () -> 2.1;

        printWeightInKG("Apple",  appleKG);
        printWeightInKG("Laptop", laptopKG);

        System.out.println("\n========== Pound weights via Adapter ==========");
        // Items from a US supplier system — weight only available in pounds
        WeightInPound sugarPound  = new WeightInPound(5.0);    //  5.00 lbs
        WeightInPound dumbbellPound = new WeightInPound(22.0); // 22.00 lbs
        WeightInPound milkPound   = new WeightInPound(2.2);    //  2.20 lbs

        WeightInKG sugarAdapter    = new WeightAdapter(sugarPound);
        WeightInKG dumbbellAdapter = new WeightAdapter(dumbbellPound);
        WeightInKG milkAdapter     = new WeightAdapter(milkPound);

        printWeightInKG("Sugar (5 lbs)",    sugarAdapter);
        printWeightInKG("Dumbbell (22 lbs)",dumbbellAdapter);
        printWeightInKG("Milk (2.2 lbs)",   milkAdapter);

        System.out.println("\n========== Quick Conversion Table ==========");
        System.out.printf("%-10s %-15s %-15s%n", "Item", "Pounds", "Kilograms");
        System.out.println("-".repeat(42));

        double[] pounds = {1, 5, 10, 22, 50, 100};
        String[] items  = {"1 lb", "5 lbs", "10 lbs", "22 lbs", "50 lbs", "100 lbs"};

        for (int i = 0; i < pounds.length; i++) {
            WeightInKG adapter = new WeightAdapter(new WeightInPound(pounds[i]));
            System.out.printf("%-10s %-15.2f %-15.4f%n",
                    items[i], pounds[i], adapter.getWeightInKG());
        }
    }
}