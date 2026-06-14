package objectpool;

public class Main {
    public static void main(String[] args) {
        DeliveryAgentPool pool = new DeliveryAgentPool(3);

        System.out.println("=== Initial Pool Status ===");
        pool.printStatus();

        System.out.println("\n=== Assigning Orders ===");
        DeliveryAgent agent1 = pool.acquire();
        agent1.deliver("ORDER-101");

        DeliveryAgent agent2 = pool.acquire();
        agent2.deliver("ORDER-102");

        DeliveryAgent agent3 = pool.acquire();
        agent3.deliver("ORDER-103");

        System.out.println("\n=== Pool Status (all busy) ===");
        pool.printStatus();

        System.out.println("\n=== New Order Arrives (pool full) ===");
        DeliveryAgent agent4 = pool.acquire(); // should return null

        System.out.println("\n=== Agent-1 Completes Delivery ===");
        pool.release(agent1);

        System.out.println("\n=== New Order Retried ===");
        DeliveryAgent agent5 = pool.acquire();
        if (agent5 != null) agent5.deliver("ORDER-104");

        System.out.println("\n=== Final Pool Status ===");
        pool.printStatus();
    }
}