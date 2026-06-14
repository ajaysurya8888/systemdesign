package strategy.payments.upi;

import strategy.payments.PaymentStrategy;
import strategy.payments.PaymentResponse;

public abstract class UPIStrategy implements PaymentStrategy {

    protected final String upiId;
    protected boolean verified;

    public UPIStrategy(String upiId) {
        this.upiId = upiId;
        this.verified = false;
    }

    @Override
    public boolean validate() {
        if (upiId == null || !upiId.contains("@")) {
            System.out.println("Invalid UPI ID: " + upiId);
            return false;
        }
        this.verified = true;
        return true;
    }

    @Override
    public boolean refund(String txnId) {
        System.out.println("Initiating UPI refund for txnId=" + txnId + " to " + upiId);
        return true;
    }

    protected PaymentResponse buildResponse(String gateway, double amount) {
        String txnId = gateway.toUpperCase() + "_TXN_" + System.currentTimeMillis();
        System.out.println("₹" + amount + " paid via " + gateway + " [" + upiId + "] | txnId=" + txnId);
        return new PaymentResponse(txnId, true, "Payment successful via " + gateway);
    }
}
