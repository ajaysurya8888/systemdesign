package interpreter;

public class OrderAbove500 implements DiscountRule {
    public boolean isEligible(Order order) { return order.getAmount() > 500; }
}