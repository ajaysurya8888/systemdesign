# ACID Properties

ACID is a set of properties that guarantee database transactions are processed reliably. Every relational database (MySQL, PostgreSQL, Oracle) enforces these.

---

## A — Atomicity

A transaction is treated as a single unit. Either **all operations succeed** or **none of them are applied**.

**Example:** Transferring ₹500 from Account A to Account B involves two steps:
1. Deduct ₹500 from A
2. Add ₹500 to B

If step 2 fails after step 1, atomicity ensures step 1 is also rolled back. Money is never lost in the middle.

**Mechanism:** Implemented via transaction logs and rollback.

---

## C — Consistency

A transaction brings the database from one **valid state to another valid state**. All defined rules, constraints, and triggers must hold before and after the transaction.

**Example:** A bank account balance cannot go below zero (constraint). If a withdrawal would violate this, the transaction is rejected — the database stays consistent.

**Note:** Consistency here is defined by the application's rules, not the database engine alone.

---

## I — Isolation

Concurrent transactions execute **as if they were serial** (one after another). Intermediate states of a transaction are not visible to other transactions.

**Example:** Two users booking the last seat on a flight simultaneously. Isolation ensures only one succeeds — the other sees the updated state after the first commits.

**Isolation Levels (weakest to strongest):**
| Level | Dirty Read | Non-repeatable Read | Phantom Read |
|---|---|---|---|
| Read Uncommitted | Yes | Yes | Yes |
| Read Committed | No | Yes | Yes |
| Repeatable Read | No | No | Yes |
| Serializable | No | No | No |

Higher isolation = more correctness, less concurrency.

---

## D — Durability

Once a transaction is **committed**, it remains committed even in the event of a crash, power failure, or error.

**Example:** After you receive a "Payment successful" confirmation, that record persists even if the server crashes a second later.

**Mechanism:** Write-ahead logging (WAL), replication, and disk flushing.

---

## When ACID Matters

- Financial systems (payments, banking)
- Order management systems
- Any system where partial writes are catastrophic

---

## ACID vs BASE

| | ACID | BASE |
|---|---|---|
| Used in | Relational DBs | NoSQL DBs |
| Consistency | Strong | Eventual |
| Availability | May sacrifice | Prioritized |
| Example | PostgreSQL | DynamoDB, Cassandra |

ACID trades availability for correctness. BASE trades correctness for availability and scale.