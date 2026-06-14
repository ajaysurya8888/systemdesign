package strategy.payments.card;

import strategy.payments.PaymentStrategy;
import strategy.payments.PaymentResponse;
import strategy.payments.model.CardInfo;

public abstract class CardStrategy implements PaymentStrategy {

    protected final CardInfo cardData;
    protected final String billingAddress;

    public CardStrategy(CardInfo cardData, String billingAddress) {
        this.cardData = cardData;
        this.billingAddress = billingAddress;
    }

    @Override
    public boolean validate() {
        if (cardData == null) {
            System.out.println("No card data provided.");
            return false;
        }
        if (cardData.isExpired()) {
            System.out.println("Card expired: " + cardData);
            return false;
        }
        return true;
    }

    @Override
    public boolean refund(String txnId) {
        System.out.println("Initiating refund to " + cardData + " for txnId=" + txnId);
        return true;
    }

    protected PaymentResponse chargeCard(String type, double amount) {
        String txnId = type.toUpperCase() + "_TXN_" + System.currentTimeMillis();
        System.out.println("Charging ₹" + amount + " to " + cardData + " via " + type);
        System.out.println("Token sent to gateway: " + cardData.getCardToken());
        return new PaymentResponse(txnId, true, "Payment successful via " + type);
    }
}
