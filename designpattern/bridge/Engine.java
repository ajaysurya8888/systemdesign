package bridge;

// Implementor — engine hierarchy varies independently of vehicle hierarchy
public interface Engine {
    void start();
    void stop();
    String getType();
}