package strategy.payments.upi;

import strategy.payments.PaymentResponse;

public class GooglePayStrategy extends UPIStrategy {

    private final String gpayToken;   // OAuth token from Google Pay SDK

    public GooglePayStrategy(String upiId, String gpayToken) {
        super(upiId);
        this.gpayToken = gpayToken;
    }

    @Override
    public PaymentResponse pay(double amount) {
        System.out.println("Initiating Google Pay collect request via SDK...");
        System.out.println("GPay token: " + gpayToken);
        // In real flow: invoke Google Pay SDK → user enters UPI PIN on device → NPCI settles
        return buildResponse("GooglePay", amount);
    }
}
