package mediator;

public class Main {

    public static void main(String[] args) {

        // Create mediator
        DeliveryMediator mediator = new DeliveryMediator();

        // Create colleagues — each only knows the mediator
        Customer       alice      = new Customer("Alice",    "12, Anna Nagar, Chennai", mediator);
        Restaurant     murugan    = new Restaurant("Murugan Idli Shop",                 mediator);
        Driver         ravi       = new Driver("Ravi",                                  mediator);
        Driver         kumar      = new Driver("Kumar",                                 mediator);
        TrackingSystem tracking   = new TrackingSystem(mediator);

        // Register all colleagues with mediator
        mediator.registerCustomer(alice);
        mediator.registerRestaurant(murugan);
        mediator.registerDriver(ravi);
        mediator.registerDriver(kumar);
        mediator.registerTracking(tracking);

        // === Order 1 ===
        System.out.println("==================== ORDER 1 ====================");
        alice.placeOrder("ORD001");

        // === Order 2 — different customer, same setup ===
        System.out.println("\n==================== ORDER 2 ====================");
        Customer       bob        = new Customer("Bob",      "45, T.Nagar, Chennai",    mediator);
        Restaurant     biryaniHub = new Restaurant("Biryani Hub",                       mediator);
        Driver         priya      = new Driver("Priya",                                 mediator);

        mediator.registerCustomer(bob);
        mediator.registerRestaurant(biryaniHub);
        mediator.registerDriver(priya);

        bob.placeOrder("ORD002");

        // === Full tracking log ===
        tracking.printLog();

        // === Show no direct connections ===
        System.out.println("\n==================== Communication Map ====================");
        System.out.println("  Alice    → speaks only to → Mediator");
        System.out.println("  Murugan  → speaks only to → Mediator");
        System.out.println("  Ravi     → speaks only to → Mediator");
        System.out.println("  Tracking → speaks only to → Mediator");
        System.out.println("  Mediator → coordinates all without them knowing each other");
    }
}
