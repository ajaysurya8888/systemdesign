package interpreter;

public interface DiscountRule {
    boolean isEligible(Order order);
}