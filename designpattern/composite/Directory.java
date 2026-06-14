package composite;

import java.util.ArrayList;
import java.util.List;

// Composite — can hold Files and other Directories
public class Directory implements FileSystemComponent {

    private final String name;
    private final List<FileSystemComponent> children = new ArrayList<>();

    public Directory(String name) {
        this.name = name;
    }

    public void add(FileSystemComponent component) {
        children.add(component);
    }

    public void remove(FileSystemComponent component) {
        children.remove(component);
    }

    @Override
    public String getName() { return name; }

    // Size = sum of all children sizes recursively
    @Override
    public long getSizeKB() {
        long total = 0;
        for (FileSystemComponent child : children) {
            total += child.getSizeKB();
        }
        return total;
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "📁 " + name + "/  (" + getSizeKB() + " KB)");
        for (FileSystemComponent child : children) {
            child.display(indent + "    ");
        }
    }
}