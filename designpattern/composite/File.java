package composite;

// Leaf — no children, just holds its own size
public class File implements FileSystemComponent {

    private final String name;
    private final long sizeKB;

    public File(String name, long sizeKB) {
        this.name   = name;
        this.sizeKB = sizeKB;
    }

    @Override
    public String getName()   { return name; }

    @Override
    public long getSizeKB()   { return sizeKB; }

    @Override
    public void display(String indent) {
        System.out.println(indent + "📄 " + name + "  (" + sizeKB + " KB)");
    }
}