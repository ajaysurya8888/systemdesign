# Strategy Design Pattern — Payment System

## Class Diagram

```
┌══════════════════════════════════════════════════════════════════════════════════════════════╗
║                        STRATEGY PATTERN — Payment System (Full)                              ║
╚══════════════════════════════════════════════════════════════════════════════════════════════╝

                              ┌──────────────────────────┐
                              │      PaymentContext        │
                              │──────────────────────────│
                              │ - strategy: PayStrategy   │
                              │ - orderId: String         │
                              │ - userId: String          │
                              │──────────────────────────│
                              │ + setStrategy(strategy)   │
                              │ + executePayment(amount)  │
                              │ + getPaymentStatus()      │
                              └──────────────────────────┘
                                           │
                                           │ delegates to
                                           ▼
                              ┌──────────────────────────┐
                              │   <<interface>>           │
                              │   PaymentStrategy         │
                              │──────────────────────────│
                              │ + pay(amount): Response   │
                              │ + validate(): bool        │
                              │ + refund(txnId): bool     │
                              └──────────────────────────┘
                                           △
                  ┌────────────────────────┼────────────────────────┐
                  │                        │                        │
                  ▼                        ▼                        ▼
     ┌─────────────────────┐  ┌─────────────────────┐  ┌─────────────────────┐
     │  <<abstract>>       │  │  <<abstract>>        │  │  BankTransfer       │
     │  UPIStrategy        │  │  CardStrategy        │  │  Strategy           │
     │─────────────────────│  │─────────────────────│  │─────────────────────│
     │ # upiId: String     │  │ # cardData: CardInfo │  │ - accountNo: String │
     │ # verified: bool    │  │ # billingAddr: Addr  │  │ - ifscCode: String  │
     │─────────────────────│  │─────────────────────│  │ - bankName: String  │
     │ + validate()        │  │ + validate()         │  │ - accountName:String│
     │ + pay(amount)       │  │ + pay(amount)         │  │─────────────────────│
     └─────────────────────┘  └─────────────────────┘  │ + validate()        │
               △                         △              │ + pay(amount)       │
        ┌──────┴──────┐           ┌──────┴──────┐       │ + refund(txnId)     │
        │             │           │             │       └─────────────────────┘
        ▼             ▼           ▼             ▼
┌─────────────┐ ┌───────────┐ ┌──────────────┐ ┌──────────────┐
│ GooglePay   │ │ PhonePe   │ │ CreditCard   │ │ DebitCard    │
│ Strategy    │ │ Strategy  │ │ Strategy     │ │ Strategy     │
│─────────────│ │───────────│ │──────────────│ │──────────────│
│- gpayToken  │ │- ppToken  │ │- creditLimit │ │- linkedAcct  │
│- linkedUPI  │ │- linkedUPI│ │- emiOptions[]│ │- dailyLimit  │
│─────────────│ │───────────│ │──────────────│ │──────────────│
│+ pay()      │ │+ pay()    │ │+ pay()       │ │+ pay()       │
│+ validate() │ │+validate()│ │+ validate()  │ │+ validate()  │
└─────────────┘ └───────────┘ └──────────────┘ └──────────────┘
```

---

## CardInfo — How Card Data is Stored

```
┌══════════════════════════════════════════════════════════════════════════════╗
║                     CardInfo — How Card Data is Stored                       ║
╚══════════════════════════════════════════════════════════════════════════════╝

  ┌──────────────────────────────────────────────────────────────────────┐
  │                         CardInfo  (Value Object)                      │
  │──────────────────────────────────────────────────────────────────────│
  │                                                                        │
  │   STORED IN DB (tokenized / encrypted)        NEVER STORED            │
  │   ─────────────────────────────────────        ───────────────────── │
  │   cardToken      : "tok_visa_4242..."    ✓     fullCardNumber   ✗     │
  │   last4Digits    : "4242"               ✓     CVV / CVC        ✗     │
  │   cardNetwork    : VISA | MASTER | AMEX ✓     PIN              ✗     │
  │   cardType       : CREDIT | DEBIT       ✓                            │
  │   cardHolderName : "John Doe"           ✓                            │
  │   expiryMonth    : "12"                 ✓  ◄─ display only           │
  │   expiryYear     : "2028"               ✓  ◄─ display only           │
  │   issuingBank    : "HDFC"               ✓                            │
  │   billingZip     : "400001"             ✓                            │
  │   fingerprint    : "fp_abc123..."       ✓  ◄─ detect duplicate cards │
  │                                                                        │
  └──────────────────────────────────────────────────────────────────────┘
                              │
                ┌─────────────┴─────────────┐
                ▼                           ▼
  ┌─────────────────────────┐   ┌────────────────────────────┐
  │    cards  (DB Table)    │   │  Payment Gateway / Vault   │
  │─────────────────────────│   │────────────────────────────│
  │ id          UUID  PK    │   │  Stores actual PAN         │
  │ user_id     UUID  FK    │   │  Returns: cardToken        │
  │ card_token  VARCHAR      │   │  (Stripe / Razorpay etc.)  │
  │ last4       CHAR(4)      │   │                            │
  │ network     ENUM         │   │  Your server NEVER sees    │
  │ card_type   ENUM         │   │  the raw card number after │
  │ holder_name VARCHAR      │   │  initial tokenization      │
  │ expiry_mm   CHAR(2)      │   └────────────────────────────┘
  │ expiry_yy   CHAR(4)      │
  │ bank_name   VARCHAR      │
  │ fingerprint VARCHAR      │
  │ is_default  BOOL         │
  │ created_at  TIMESTAMP    │
  └─────────────────────────┘
```

---

## Runtime Flow — Credit Card Payment

```
  User selects saved card ──► DB lookup by user_id ──► loads CardInfo (token)
            │
            ▼
  PaymentContext.setStrategy(CreditCardStrategy(cardInfo))
            │
            ▼
  PaymentContext.executePayment(₹1500)
            │
            ├──► CreditCardStrategy.validate()
            │         checks: expiry not past, credit limit ok
            │
            ├──► CreditCardStrategy.pay(1500)
            │         sends: { cardToken, amount, currency }
            │         to:    Payment Gateway API
            │
            └──► Gateway returns: { txnId, status: SUCCESS }
                       │
                       ▼
               Store in transactions table
               { txnId, userId, orderId, amount,
                 method: CREDIT_CARD, last4: "4242" }
```

---

## Runtime Flow — UPI (GooglePay vs PhonePe)

```
  GooglePayStrategy                     PhonePayStrategy
  ─────────────────────────             ─────────────────────────
  - gpayToken (OAuth token)             - ppToken (OAuth token)
  - linkedUPI: "user@okaxis"            - linkedUPI: "user@ybl"
  - pay() ──► Google Pay SDK            - pay() ──► PhonePe SDK
              collect request                       collect request
              UPI PIN entered                       UPI PIN entered
              on user's device                      on user's device
                    │                                     │
                    └──────────────┬──────────────────────┘
                                   ▼
                            NPCI / UPI Rails
                                   │
                            Bank account debited
                                   │
                            txnId returned to app
```

---

## Folder Structure

```
src/strategy/payments/
├── PaymentStrategy.java          ← interface
├── PaymentContext.java           ← context (holds & delegates to strategy)
├── PaymentResponse.java          ← response value object
├── model/
│   └── CardInfo.java             ← card data (tokenized, PCI-DSS safe)
├── upi/
│   ├── UPIStrategy.java          ← abstract base for UPI methods
│   ├── GooglePayStrategy.java    ← Google Pay concrete strategy
│   └── PhonePayStrategy.java     ← PhonePe concrete strategy
├── card/
│   ├── CardStrategy.java         ← abstract base for card methods
│   ├── CreditCardStrategy.java   ← credit card concrete strategy
│   └── DebitCardStrategy.java    ← debit card concrete strategy
└── bank/
    └── BankTransferStrategy.java ← NEFT/IMPS bank transfer strategy
```

---

## Key Design Decisions

| Decision | Reason |
|---|---|
| `CardInfo` is a separate value object | Reused by both `CreditCardStrategy` and `DebitCardStrategy` |
| Raw card number never stored | PCI-DSS compliance — gateway vaults it, you store only the token |
| `fingerprint` column on cards table | Detect if user adds same card twice across networks |
| UPI strategies share `UPIStrategy` base | Both use UPI rails; only the SDK and token differ |
| `validate()` on every strategy | Each method has different pre-payment checks (limit, expiry, UPI linkage) |
| Builder pattern on `CardInfo` | Prevents raw card fields from leaking as mutable state |
