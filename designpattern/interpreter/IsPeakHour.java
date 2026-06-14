package interpreter;

public class IsPeakHour implements DiscountRule {
    public boolean isEligible(Order order) { return order.isPeakHour(); }
}