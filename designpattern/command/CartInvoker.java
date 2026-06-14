package command;

import java.util.Stack;

// Invoker — holds undo and redo stacks, never touches Cart directly
public class CartInvoker {

    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command command) {
        System.out.println("\n>> Execute: " + command.getDescription());
        command.execute();
        undoStack.push(command);
        redoStack.clear();   // new action clears redo history
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("\n>> Undo: Nothing to undo.");
            return;
        }
        Command command = undoStack.pop();
        System.out.println("\n>> Undo: " + command.getDescription());
        command.undo();
        redoStack.push(command);
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("\n>> Redo: Nothing to redo.");
            return;
        }
        Command command = redoStack.pop();
        System.out.println("\n>> Redo: " + command.getDescription());
        command.execute();
        undoStack.push(command);
    }

    public void printHistory() {
        System.out.println("\n-- Undo Stack (top = last action) --");
        if (undoStack.isEmpty()) { System.out.println("   (empty)"); return; }
        for (int i = undoStack.size() - 1; i >= 0; i--) {
            System.out.println("   " + (undoStack.size() - i) + ". " + undoStack.get(i).getDescription());
        }
    }
}
