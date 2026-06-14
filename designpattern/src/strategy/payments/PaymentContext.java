package strategy.payments;

public class PaymentContext {
    private PaymentStrategy strategy;
    private final String orderId;
    private final String userId;

    public PaymentContext(String orderId, String userId) {
        this.orderId = orderId;
        this.userId = userId;
    }

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public PaymentResponse executePayment(double amount) {
        if (strategy == null) {
            throw new IllegalStateException("Payment strategy not set.");
        }
        if (!strategy.validate()) {
            return new PaymentResponse(null, false, "Validation failed for the selected payment method.");
        }
        System.out.println("Executing payment for orderId=" + orderId + ", userId=" + userId + ", amount=₹" + amount);
        return strategy.pay(amount);
    }

    public boolean refundPayment(String txnId) {
        if (strategy == null) {
            throw new IllegalStateException("Payment strategy not set.");
        }
        return strategy.refund(txnId);
    }
}
