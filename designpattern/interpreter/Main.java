package interpreter;

public class Main {
    public static void main(String[] args) {

        // Rule: new user AND order > 500 AND NOT peak hour
        DiscountRule rule = new AndRule(
            new AndRule(new IsNewUser(), new OrderAbove500()),
            new NotRule(new IsPeakHour())
        );

        Order order1 = new Order(true, 600, false);   // new user, 600, not peak hour
        Order order2 = new Order(true, 600, true);    // new user, 600, peak hour
        Order order3 = new Order(false, 700, false);  // not new user, 700, not peak hour

        System.out.println("=== Discount Eligibility ===");
        System.out.println("Order1 (new user, 600, off-peak) : " + rule.isEligible(order1)); // true
        System.out.println("Order2 (new user, 600, peak hour): " + rule.isEligible(order2)); // false
        System.out.println("Order3 (old user, 700, off-peak) : " + rule.isEligible(order3)); // false
    }
}