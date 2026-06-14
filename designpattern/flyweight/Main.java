package flyweight;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("========== Creating Flyweights ==========");

        // Flyweights created only ONCE — shared across all restaurants
        FoodItem idly       = FoodItemFactory.getFoodItem("Idly",           "South Indian", "Breakfast", true,  "cdn/idly.jpg");
        FoodItem dosa       = FoodItemFactory.getFoodItem("Masala Dosa",    "South Indian", "Breakfast", true,  "cdn/dosa.jpg");
        FoodItem biryani    = FoodItemFactory.getFoodItem("Veg Biryani",    "Mughlai",      "Main",      true,  "cdn/veg-biryani.jpg");
        FoodItem chickenBiryani = FoodItemFactory.getFoodItem("Chicken Biryani", "Mughlai", "Main",      false, "cdn/chicken-biryani.jpg");
        FoodItem poori      = FoodItemFactory.getFoodItem("Poori",          "North Indian", "Breakfast", true,  "cdn/poori.jpg");
        FoodItem paneer     = FoodItemFactory.getFoodItem("Paneer Butter Masala", "North Indian", "Main", true, "cdn/paneer.jpg");

        // Reuse flyweights — no new objects created
        System.out.println();
        FoodItemFactory.getFoodItem("Idly",           "South Indian", "Breakfast", true,  "cdn/idly.jpg");
        FoodItemFactory.getFoodItem("Veg Biryani",    "Mughlai",      "Main",      true,  "cdn/veg-biryani.jpg");
        FoodItemFactory.getFoodItem("Chicken Biryani","Mughlai",      "Main",      false, "cdn/chicken-biryani.jpg");

        // Build restaurant menus — each passes its own extrinsic state
        System.out.println("\n========== Building Restaurant Menus ==========");
        List<RestaurantMenuItem> allMenuItems = new ArrayList<>();

        // Murugan Idli Shop — their own prices and availability
        allMenuItems.add(new RestaurantMenuItem(idly,    "Murugan Idli Shop",  40.0,  true));
        allMenuItems.add(new RestaurantMenuItem(dosa,    "Murugan Idli Shop",  70.0,  true));
        allMenuItems.add(new RestaurantMenuItem(poori,   "Murugan Idli Shop",  60.0,  false));

        // Saravana Bhavan — their own prices
        allMenuItems.add(new RestaurantMenuItem(idly,    "Saravana Bhavan",    55.0,  true));
        allMenuItems.add(new RestaurantMenuItem(dosa,    "Saravana Bhavan",    90.0,  true));
        allMenuItems.add(new RestaurantMenuItem(paneer,  "Saravana Bhavan",   180.0,  true));

        // Biryani Blues
        allMenuItems.add(new RestaurantMenuItem(biryani,        "Biryani Blues",  200.0, true));
        allMenuItems.add(new RestaurantMenuItem(chickenBiryani, "Biryani Blues",  280.0, true));

        // Paradise Biryani — same flyweights, different prices
        allMenuItems.add(new RestaurantMenuItem(biryani,        "Paradise Biryani", 220.0, true));
        allMenuItems.add(new RestaurantMenuItem(chickenBiryani, "Paradise Biryani", 320.0, false));

        // Behrouz — premium pricing
        allMenuItems.add(new RestaurantMenuItem(chickenBiryani, "Behrouz",        450.0, true));
        allMenuItems.add(new RestaurantMenuItem(paneer,         "Behrouz",        350.0, true));

        // Display all menu items
        System.out.println();
        System.out.printf("  %-25s | %-20s | %-12s | %-10s | %-10s | %-11s | %s%n",
                "Restaurant", "Item", "Cuisine", "Category", "Price", "Type", "Status");
        System.out.println("  " + "-".repeat(110));
        for (RestaurantMenuItem item : allMenuItems) {
            item.display();
        }

        // Memory summary
        System.out.println("\n========== Memory Summary ==========");
        System.out.println("Total menu entries (RestaurantMenuItem objects) : " + allMenuItems.size());
        System.out.println("Total FoodItem flyweights created                : " + FoodItemFactory.getTotalFlyweights());
        System.out.println("Flyweights reused (entries - flyweights)         : "
                + (allMenuItems.size() - FoodItemFactory.getTotalFlyweights()) + " times");
        System.out.println("\nWithout Flyweight: " + allMenuItems.size() + " full FoodItem objects in memory");
        System.out.println("With Flyweight   : " + FoodItemFactory.getTotalFlyweights() + " shared FoodItem objects in memory");
    }
}
