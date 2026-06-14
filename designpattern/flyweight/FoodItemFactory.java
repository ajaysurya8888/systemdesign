package flyweight;

import java.util.HashMap;
import java.util.Map;

// Flyweight Factory — caches and reuses FoodItem flyweights
public class FoodItemFactory {

    private static final Map<String, FoodItem> cache = new HashMap<>();

    public static FoodItem getFoodItem(String name, String cuisine,
                                       String category, boolean isVeg, String imageUrl) {
        if (!cache.containsKey(name)) {
            cache.put(name, new FoodItem(name, cuisine, category, isVeg, imageUrl));
        } else {
            System.out.println("[FoodItemFactory] Reusing flyweight for: " + name);
        }
        return cache.get(name);
    }

    public static int getTotalFlyweights() {
        return cache.size();
    }
}
