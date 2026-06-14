package memento;

import java.util.Stack;

// Caretaker — manages save points, never reads or modifies memento internals
public class GameCaretaker {

    private final Stack<GameMemento> savePoints = new Stack<>();

    public void save(GameMemento memento) {
        savePoints.push(memento);
    }

    public GameMemento getLastSave() {
        if (savePoints.isEmpty()) {
            System.out.println("  [Caretaker] No save points found!");
            return null;
        }
        return savePoints.peek();
    }

    public GameMemento popLastSave() {
        if (savePoints.isEmpty()) {
            System.out.println("  [Caretaker] No save points to restore!");
            return null;
        }
        return savePoints.pop();
    }

    public void listSavePoints() {
        if (savePoints.isEmpty()) {
            System.out.println("  [Caretaker] No save points.");
            return;
        }
        System.out.println("  [Caretaker] All save points (" + savePoints.size() + "):");
        for (int i = savePoints.size() - 1; i >= 0; i--) {
            System.out.println("    " + (savePoints.size() - i) + ". " + savePoints.get(i));
        }
    }

    public boolean hasSavePoints() {
        return !savePoints.isEmpty();
    }

    public int getSaveCount() {
        return savePoints.size();
    }
}
