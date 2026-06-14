package composite;

public interface FileSystemComponent {
    String getName();
    long getSizeKB();
    void display(String indent);
}