package iterator;

import java.util.List;

// Concrete Iterator 1 — traverses items in original order (top to bottom)
public class SequentialMenuIterator implements MenuIterator {

    private final List<MenuItem> items;
    private int position = 0;

    public SequentialMenuIterator(List<MenuItem> items) {
        this.items = items;
    }

    @Override
    public boolean hasNext() {
        return position < items.size();
    }

    @Override
    public MenuItem next() {
        return items.get(position++);
    }

    @Override
    public void reset() {
        position = 0;
    }
}
