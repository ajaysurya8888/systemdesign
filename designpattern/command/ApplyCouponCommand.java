package command;

public class ApplyCouponCommand implements Command {

    private final Cart cart;
    private final String couponCode;
    private final double discountAmount;

    public ApplyCouponCommand(Cart cart, String couponCode, double discountAmount) {
        this.cart           = cart;
        this.couponCode     = couponCode;
        this.discountAmount = discountAmount;
    }

    @Override
    public void execute() {
        cart.applyCoupon(couponCode, discountAmount);
    }

    @Override
    public void undo() {
        cart.removeCoupon();
    }

    @Override
    public String getDescription() {
        return "Apply Coupon [" + couponCode + " → ₹" + discountAmount + " off]";
    }
}
