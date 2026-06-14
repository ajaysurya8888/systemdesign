package iterator;

import java.util.ArrayList;
import java.util.List;

// Concrete Collection — internally uses ArrayList (client doesn't know or care)
public class RestaurantMenu implements Menu {

    private final String menuName;
    private final List<MenuItem> items = new ArrayList<>();

    public RestaurantMenu(String menuName) {
        this.menuName = menuName;
    }

    @Override
    public void addItem(MenuItem item) {
        items.add(item);
    }

    @Override
    public MenuIterator getSequentialIterator() {
        return new SequentialMenuIterator(items);
    }

    @Override
    public MenuIterator getShuffleIterator() {
        return new ShuffleMenuIterator(items);
    }

    @Override
    public String getMenuName() { return menuName; }
}
