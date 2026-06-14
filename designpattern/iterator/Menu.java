package iterator;

// Iterable interface — collection exposes iterators
public interface Menu {
    void addItem(MenuItem item);
    MenuIterator getSequentialIterator();
    MenuIterator getShuffleIterator();
    String getMenuName();
}
