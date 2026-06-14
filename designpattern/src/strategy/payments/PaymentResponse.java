package strategy.payments;

public class PaymentResponse {
    private final String txnId;
    private final boolean success;
    private final String message;

    public PaymentResponse(String txnId, boolean success, String message) {
        this.txnId = txnId;
        this.success = success;
        this.message = message;
    }

    public String getTxnId()    { return txnId; }
    public boolean isSuccess()  { return success; }
    public String getMessage()  { return message; }

    @Override
    public String toString() {
        return "PaymentResponse{txnId='" + txnId + "', success=" + success + ", message='" + message + "'}";
    }
}
