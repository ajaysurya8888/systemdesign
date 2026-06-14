package strategy.payments.upi;

import strategy.payments.PaymentResponse;

public class PhonePayStrategy extends UPIStrategy {

    private final String ppToken;   // OAuth token from PhonePe SDK

    public PhonePayStrategy(String upiId, String ppToken) {
        super(upiId);
        this.ppToken = ppToken;
    }

    @Override
    public PaymentResponse pay(double amount) {
        System.out.println("Initiating PhonePe collect request via SDK...");
        System.out.println("PhonePe token: " + ppToken);
        // In real flow: invoke PhonePe SDK → user enters UPI PIN on device → NPCI settles
        return buildResponse("PhonePe", amount);
    }
}
