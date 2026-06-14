package strategy.payments.card;

import strategy.payments.PaymentResponse;
import strategy.payments.model.CardInfo;

import java.util.List;

public class CreditCardStrategy extends CardStrategy {

    private final double creditLimit;
    private final List<Integer> emiOptions;   // available EMI tenures in months

    public CreditCardStrategy(CardInfo cardData, String billingAddress,
                               double creditLimit, List<Integer> emiOptions) {
        super(cardData, billingAddress);
        this.creditLimit = creditLimit;
        this.emiOptions  = emiOptions;
    }

    @Override
    public boolean validate() {
        if (!super.validate()) return false;
        if (creditLimit <= 0) {
            System.out.println("Insufficient credit limit.");
            return false;
        }
        return true;
    }

    @Override
    public PaymentResponse pay(double amount) {
        if (amount > creditLimit) {
            return new PaymentResponse(null, false, "Amount exceeds credit limit of ₹" + creditLimit);
        }
        if (!emiOptions.isEmpty()) {
            System.out.println("EMI options available: " + emiOptions + " months");
        }
        return chargeCard("CreditCard", amount);
    }
}
