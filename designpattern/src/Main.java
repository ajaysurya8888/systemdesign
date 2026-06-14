import strategy.payments.*;
import strategy.payments.upi.*;
import strategy.payments.card.*;
import strategy.payments.bank.*;
import strategy.payments.model.CardInfo;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        PaymentContext context = new PaymentContext("ORDER_001", "USER_42");

        // ── Google Pay ────────────────────────────────────────────────
        System.out.println("\n=== Google Pay ===");
        context.setStrategy(new GooglePayStrategy("user@okaxis", "gpay_token_xyz"));
        System.out.println(context.executePayment(500));

        // ── PhonePe ───────────────────────────────────────────────────
        System.out.println("\n=== PhonePe ===");
        context.setStrategy(new PhonePayStrategy("user@ybl", "pp_token_abc"));
        System.out.println(context.executePayment(750));

        // ── Credit Card ───────────────────────────────────────────────
        System.out.println("\n=== Credit Card ===");
        CardInfo creditCard = new CardInfo.Builder()
                .cardToken("tok_visa_4242")
                .last4Digits("4242")
                .cardNetwork(CardInfo.CardNetwork.VISA)
                .cardType(CardInfo.CardType.CREDIT)
                .cardHolderName("John Doe")
                .expiryMonth("12")
                .expiryYear("2028")
                .issuingBank("HDFC")
                .billingZip("400001")
                .fingerprint("fp_visa_hdfc_001")
                .build();

        context.setStrategy(new CreditCardStrategy(creditCard, "Mumbai, MH", 50000, List.of(3, 6, 12)));
        System.out.println(context.executePayment(1500));

        // ── Debit Card ────────────────────────────────────────────────
        System.out.println("\n=== Debit Card ===");
        CardInfo debitCard = new CardInfo.Builder()
                .cardToken("tok_rupay_5678")
                .last4Digits("5678")
                .cardNetwork(CardInfo.CardNetwork.RUPAY)
                .cardType(CardInfo.CardType.DEBIT)
                .cardHolderName("Jane Doe")
                .expiryMonth("08")
                .expiryYear("2027")
                .issuingBank("SBI")
                .billingZip("110001")
                .fingerprint("fp_rupay_sbi_002")
                .build();

        context.setStrategy(new DebitCardStrategy(debitCard, "Delhi, DL", "SBI_ACC_9876543210", 25000));
        System.out.println(context.executePayment(2000));

        // ── Bank Transfer ─────────────────────────────────────────────
        System.out.println("\n=== Bank Transfer ===");
        context.setStrategy(new BankTransferStrategy("123456789012", "SBIN0001234", "SBI", "Jane Doe"));
        System.out.println(context.executePayment(5000));
    }
}
