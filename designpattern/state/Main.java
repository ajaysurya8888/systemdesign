package state;

public class Main {

    public static void main(String[] args) {

        System.out.println("========== Order 1 — Full Flow ==========");
        DeliveryOrder order1 = new DeliveryOrder("ORD001", "Alice");
        System.out.println();
        order1.nextStep();   // ORDERED → IN_PROGRESS
        System.out.println();
        order1.nextStep();   // IN_PROGRESS → DELIVERED
        System.out.println();
        order1.nextStep();   // DELIVERED → no transition (terminal)

        System.out.println("\n========== Order 2 — Trying extra step after delivery ==========");
        DeliveryOrder order2 = new DeliveryOrder("ORD002", "Bob");
        System.out.println();
        order2.nextStep();
        System.out.println();
        order2.nextStep();
        System.out.println();
        order2.nextStep();   // already delivered — blocked
        order2.nextStep();   // still blocked
    }
}