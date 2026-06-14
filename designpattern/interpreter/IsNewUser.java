package interpreter;

public class IsNewUser implements DiscountRule {
    public boolean isEligible(Order order) { return order.isNewUser(); }
}