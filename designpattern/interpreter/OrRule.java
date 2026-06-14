package interpreter;

public class OrRule implements DiscountRule {
    private DiscountRule left, right;

    public OrRule(DiscountRule left, DiscountRule right) {
        this.left = left;
        this.right = right;
    }

    public boolean isEligible(Order order) {
        return left.isEligible(order) || right.isEligible(order);
    }
}