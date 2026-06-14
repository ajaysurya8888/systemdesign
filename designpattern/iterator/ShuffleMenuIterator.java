package iterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Concrete Iterator 2 — traverses items in random shuffled order
public class ShuffleMenuIterator implements MenuIterator {

    private final List<MenuItem> original;
    private List<MenuItem> shuffled;
    private int position = 0;

    public ShuffleMenuIterator(List<MenuItem> items) {
        this.original = items;
        shuffle();
    }

    private void shuffle() {
        shuffled = new ArrayList<>(original);
        Collections.shuffle(shuffled);
        position = 0;
    }

    @Override
    public boolean hasNext() {
        return position < shuffled.size();
    }

    @Override
    public MenuItem next() {
        return shuffled.get(position++);
    }

    @Override
    public void reset() {
        shuffle();   // re-shuffle on reset for a fresh random order
    }
}
