package strategy.payments.card;

import strategy.payments.PaymentResponse;
import strategy.payments.model.CardInfo;

public class DebitCardStrategy extends CardStrategy {

    private final String linkedAccountNo;
    private final double dailyLimit;

    public DebitCardStrategy(CardInfo cardData, String billingAddress,
                              String linkedAccountNo, double dailyLimit) {
        super(cardData, billingAddress);
        this.linkedAccountNo = linkedAccountNo;
        this.dailyLimit      = dailyLimit;
    }

    @Override
    public boolean validate() {
        if (!super.validate()) return false;
        if (linkedAccountNo == null || linkedAccountNo.isBlank()) {
            System.out.println("No linked bank account found for debit card.");
            return false;
        }
        return true;
    }

    @Override
    public PaymentResponse pay(double amount) {
        if (amount > dailyLimit) {
            return new PaymentResponse(null, false, "Amount ₹" + amount + " exceeds daily limit of ₹" + dailyLimit);
        }
        System.out.println("Debiting from account: " + linkedAccountNo);
        return chargeCard("DebitCard", amount);
    }
}
