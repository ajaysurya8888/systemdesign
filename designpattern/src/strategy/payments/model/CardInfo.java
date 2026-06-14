package strategy.payments.model;

public class CardInfo {

    public enum CardNetwork { VISA, MASTERCARD, AMEX, RUPAY }
    public enum CardType    { CREDIT, DEBIT }

    // Stored in DB — tokenized, never raw PAN
    private final String cardToken;      // gateway token e.g. "tok_visa_4242"
    private final String last4Digits;
    private final CardNetwork cardNetwork;
    private final CardType cardType;
    private final String cardHolderName;
    private final String expiryMonth;    // "MM"
    private final String expiryYear;     // "YYYY"
    private final String issuingBank;
    private final String billingZip;
    private final String fingerprint;    // detect duplicate cards

    // CVV / full PAN are NEVER stored — validated once by gateway, discarded
    private CardInfo(Builder builder) {
        this.cardToken      = builder.cardToken;
        this.last4Digits    = builder.last4Digits;
        this.cardNetwork    = builder.cardNetwork;
        this.cardType       = builder.cardType;
        this.cardHolderName = builder.cardHolderName;
        this.expiryMonth    = builder.expiryMonth;
        this.expiryYear     = builder.expiryYear;
        this.issuingBank    = builder.issuingBank;
        this.billingZip     = builder.billingZip;
        this.fingerprint    = builder.fingerprint;
    }

    public boolean isExpired() {
        int currentYear  = java.time.Year.now().getValue();
        int currentMonth = java.time.MonthDay.now().getMonthValue();
        int expYear  = Integer.parseInt(expiryYear);
        int expMonth = Integer.parseInt(expiryMonth);
        return (expYear < currentYear) || (expYear == currentYear && expMonth < currentMonth);
    }

    public String getCardToken()      { return cardToken; }
    public String getLast4Digits()    { return last4Digits; }
    public CardNetwork getCardNetwork(){ return cardNetwork; }
    public CardType getCardType()     { return cardType; }
    public String getCardHolderName() { return cardHolderName; }
    public String getExpiryMonth()    { return expiryMonth; }
    public String getExpiryYear()     { return expiryYear; }
    public String getIssuingBank()    { return issuingBank; }
    public String getBillingZip()     { return billingZip; }
    public String getFingerprint()    { return fingerprint; }

    @Override
    public String toString() {
        return cardNetwork + " " + cardType + " ending " + last4Digits + " (" + issuingBank + ")";
    }

    // Builder — enforces that raw card fields are never held as state
    public static class Builder {
        private String cardToken, last4Digits, cardHolderName;
        private String expiryMonth, expiryYear, issuingBank, billingZip, fingerprint;
        private CardNetwork cardNetwork;
        private CardType cardType;

        public Builder cardToken(String v)      { this.cardToken = v;      return this; }
        public Builder last4Digits(String v)    { this.last4Digits = v;    return this; }
        public Builder cardNetwork(CardNetwork v){ this.cardNetwork = v;   return this; }
        public Builder cardType(CardType v)     { this.cardType = v;       return this; }
        public Builder cardHolderName(String v) { this.cardHolderName = v; return this; }
        public Builder expiryMonth(String v)    { this.expiryMonth = v;    return this; }
        public Builder expiryYear(String v)     { this.expiryYear = v;     return this; }
        public Builder issuingBank(String v)    { this.issuingBank = v;    return this; }
        public Builder billingZip(String v)     { this.billingZip = v;     return this; }
        public Builder fingerprint(String v)    { this.fingerprint = v;    return this; }
        public CardInfo build()                 { return new CardInfo(this); }
    }
}
