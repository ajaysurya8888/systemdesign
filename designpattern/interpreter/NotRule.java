package interpreter;

public class NotRule implements DiscountRule {
    private DiscountRule rule;

    public NotRule(DiscountRule rule) {
        this.rule = rule;
    }

    public boolean isEligible(Order order) {
        return !rule.isEligible(order);
    }
}