package iterator;

public class Main {

    // Client only talks to MenuIterator — never touches the internal list
    static void printMenu(String label, MenuIterator iterator) {
        System.out.println("\n  -- " + label + " --");
        System.out.printf("  %-25s | %-12s | %-9s | %s%n",
                "Name", "Category", "Price", "Type");
        System.out.println("  " + "-".repeat(65));
        int count = 1;
        while (iterator.hasNext()) {
            MenuItem item = iterator.next();
            System.out.println("  " + count++ + ". " + item);
        }
    }

    public static void main(String[] args) {

        // Build South Indian menu
        RestaurantMenu southIndianMenu = new RestaurantMenu("South Indian Menu");
        southIndianMenu.addItem(new MenuItem("Idly",              "Breakfast",  40.0,  true));
        southIndianMenu.addItem(new MenuItem("Masala Dosa",       "Breakfast",  70.0,  true));
        southIndianMenu.addItem(new MenuItem("Vada",              "Snacks",     35.0,  true));
        southIndianMenu.addItem(new MenuItem("Pongal",            "Breakfast",  50.0,  true));
        southIndianMenu.addItem(new MenuItem("Chicken Chettinad", "Main",      220.0, false));
        southIndianMenu.addItem(new MenuItem("Sambhar Rice",      "Main",       90.0,  true));
        southIndianMenu.addItem(new MenuItem("Filter Coffee",     "Beverages",  30.0,  true));

        // Build Biryani menu
        RestaurantMenu biryaniMenu = new RestaurantMenu("Biryani Menu");
        biryaniMenu.addItem(new MenuItem("Veg Biryani",          "Main",       180.0, true));
        biryaniMenu.addItem(new MenuItem("Chicken Biryani",      "Main",       280.0, false));
        biryaniMenu.addItem(new MenuItem("Mutton Biryani",       "Main",       380.0, false));
        biryaniMenu.addItem(new MenuItem("Egg Biryani",          "Main",       220.0, false));
        biryaniMenu.addItem(new MenuItem("Onion Raitha",         "Sides",       40.0, true));
        biryaniMenu.addItem(new MenuItem("Brinjal Curry",        "Sides",       60.0, true));

        // ===== Sequential Iterator =====
        System.out.println("========== Sequential Traversal ==========");
        printMenu(southIndianMenu.getMenuName(), southIndianMenu.getSequentialIterator());
        printMenu(biryaniMenu.getMenuName(),     biryaniMenu.getSequentialIterator());

        // ===== Shuffle Iterator =====
        System.out.println("\n========== Shuffle Traversal ==========");
        printMenu(southIndianMenu.getMenuName() + " [Shuffled]", southIndianMenu.getShuffleIterator());
        printMenu(biryaniMenu.getMenuName()     + " [Shuffled]", biryaniMenu.getShuffleIterator());

        // ===== Reset and re-shuffle =====
        System.out.println("\n========== Shuffle Again (after reset) ==========");
        MenuIterator shuffleIt = southIndianMenu.getShuffleIterator();
        printMenu("South Indian [Shuffle Round 1]", shuffleIt);
        shuffleIt.reset();
        printMenu("South Indian [Shuffle Round 2 after reset]", shuffleIt);

        // ===== Sequential reset =====
        System.out.println("\n========== Sequential Reset Demo ==========");
        MenuIterator seqIt = biryaniMenu.getSequentialIterator();
        System.out.println("\n  First 3 items:");
        int count = 0;
        while (seqIt.hasNext() && count < 3) {
            System.out.println("    → " + seqIt.next().getName());
            count++;
        }
        seqIt.reset();
        System.out.println("  After reset — first item again: " + seqIt.next().getName());
    }
}
