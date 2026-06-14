package strategy.payments.bank;

import strategy.payments.PaymentStrategy;
import strategy.payments.PaymentResponse;

public class BankTransferStrategy implements PaymentStrategy {

    private final String accountNo;
    private final String ifscCode;
    private final String bankName;
    private final String accountHolderName;

    public BankTransferStrategy(String accountNo, String ifscCode,
                                 String bankName, String accountHolderName) {
        this.accountNo         = accountNo;
        this.ifscCode          = ifscCode;
        this.bankName          = bankName;
        this.accountHolderName = accountHolderName;
    }

    @Override
    public boolean validate() {
        if (accountNo == null || accountNo.isBlank()) {
            System.out.println("Invalid account number.");
            return false;
        }
        if (ifscCode == null || ifscCode.length() != 11) {
            System.out.println("Invalid IFSC code: " + ifscCode);
            return false;
        }
        return true;
    }

    @Override
    public PaymentResponse pay(double amount) {
        String txnId = "NEFT_TXN_" + System.currentTimeMillis();
        System.out.println("Transferring ₹" + amount + " via NEFT/IMPS");
        System.out.println("To: " + accountHolderName + " | Acc: " + accountNo + " | IFSC: " + ifscCode + " | Bank: " + bankName);
        return new PaymentResponse(txnId, true, "Bank transfer initiated via " + bankName);
    }

    @Override
    public boolean refund(String txnId) {
        System.out.println("Initiating bank transfer refund for txnId=" + txnId + " to account " + accountNo);
        return true;
    }
}
