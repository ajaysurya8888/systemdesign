# Idempotency

An operation is **idempotent** if performing it multiple times produces the same result as performing it once.

> f(f(x)) = f(x)

---

## Why It Matters

In distributed systems, **failures are normal**. A network request might:
- Time out before the response arrives
- Fail mid-processing
- Be received twice due to retries

Without idempotency, retrying a failed request can cause duplicate actions — charging a customer twice, sending two emails, creating two orders.

---

## HTTP Methods and Idempotency

| Method | Idempotent | Safe | Notes |
|---|---|---|---|
| GET | Yes | Yes | Read-only, no side effects |
| PUT | Yes | No | Replaces the resource completely |
| DELETE | Yes | No | Deleting twice has same result as once |
| POST | No | No | Creates new resource each time |
| PATCH | No | No | Partial updates can compound |

**GET /users/123** — Safe to retry. Returns same user.  
**PUT /users/123** `{"name": "Alice"}` — Safe to retry. Result is always the same.  
**POST /orders** — Not safe. Each retry creates a new order.

---

## Making POST Idempotent with Idempotency Keys

Pass a unique key with the request. The server stores processed keys and returns the cached response on duplicate.

**Client sends:**
```http
POST /payments
Idempotency-Key: uuid-abc-123

{ "amount": 500, "to": "account-456" }
```

**Server logic:**
```
if key "uuid-abc-123" already processed:
    return stored result
else:
    process payment
    store result with key "uuid-abc-123"
    return result
```

The client can safely retry on timeout — the payment only happens once.

**Used by:** Stripe, Razorpay, Braintree, Twilio.

---

## Database-Level Idempotency

**Upsert (INSERT ... ON CONFLICT DO UPDATE):**
```sql
INSERT INTO users (id, email) VALUES (1, 'a@b.com')
ON CONFLICT (id) DO UPDATE SET email = EXCLUDED.email;
```
Safe to run multiple times — always ends in the same state.

**Conditional updates:**
```sql
UPDATE orders SET status = 'shipped'
WHERE id = 42 AND status = 'pending';
```
Running twice has no extra effect — second run finds no matching row.

---

## Message Queue Idempotency

Queues like Kafka and SQS guarantee **at-least-once delivery** — a message may be delivered more than once. Your consumer must be idempotent.

**Pattern:** Track processed message IDs in a database. Skip if already seen.

```python
def process_message(msg):
    if db.exists("processed", msg.id):
        return  # already handled
    db.insert("processed", msg.id)
    # do actual work
```

---

## Idempotency vs At-Most-Once vs At-Least-Once

| Guarantee | Behavior | Risk |
|---|---|---|
| At-most-once | Message delivered 0 or 1 times | May lose messages |
| At-least-once | Message delivered 1 or more times | May process duplicates |
| Exactly-once | Delivered exactly once | Hard to achieve, expensive |

Idempotent consumers + at-least-once delivery = effectively exactly-once behavior.

---

## Key Rule

**Any operation that can be retried must be idempotent.** Design APIs and consumers with this assumption from the start — retrofitting is painful.