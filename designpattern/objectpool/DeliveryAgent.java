package objectpool;

public class DeliveryAgent {
    private String name;
    private boolean inUse;

    public DeliveryAgent(String name) {
        this.name = name;
        this.inUse = false;
    }

    public String getName()       { return name; }
    public boolean isInUse()      { return inUse; }
    public void setInUse(boolean inUse) { this.inUse = inUse; }

    public void deliver(String orderId) {
        System.out.println(name + " is delivering order: " + orderId);
    }
}