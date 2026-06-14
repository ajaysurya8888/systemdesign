package strategy.payments;

public interface PaymentStrategy {
    boolean validate();
    PaymentResponse pay(double amount);
    boolean refund(String txnId);
}
