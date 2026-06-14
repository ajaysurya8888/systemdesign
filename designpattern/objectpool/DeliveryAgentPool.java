package objectpool;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAgentPool {
    private List<DeliveryAgent> pool = new ArrayList<>();
    private int maxSize;

    public DeliveryAgentPool(int maxSize) {
        this.maxSize = maxSize;
        for (int i = 1; i <= maxSize; i++) {
            pool.add(new DeliveryAgent("Agent-" + i));
        }
    }

    // Borrow an available agent from the pool
    public DeliveryAgent acquire() {
        for (DeliveryAgent agent : pool) {
            if (!agent.isInUse()) {
                agent.setInUse(true);
                System.out.println("[Pool] Acquired: " + agent.getName());
                return agent;
            }
        }
        System.out.println("[Pool] No agents available. Please wait.");
        return null;
    }

    // Return agent back to pool after delivery
    public void release(DeliveryAgent agent) {
        if (agent != null) {
            agent.setInUse(false);
            System.out.println("[Pool] Released: " + agent.getName());
        }
    }

    public void printStatus() {
        System.out.println("[Pool Status]");
        for (DeliveryAgent agent : pool) {
            System.out.println("  " + agent.getName() + " -> " + (agent.isInUse() ? "BUSY" : "FREE"));
        }
    }
}