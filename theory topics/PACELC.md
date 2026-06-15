# PACELC Theorem

PACELC extends CAP Theorem by addressing what happens in **normal operation** (when there is no partition). Proposed by Daniel Abadi in 2012.

---

## The Problem with CAP

CAP only tells you what to do **when a network partition occurs**. But partitions are rare — what tradeoffs does a distributed system make **the rest of the time?**

Answer: **Latency vs Consistency**.

---

## PACELC Breakdown

> **P**artition → **A**vailability or **C**onsistency  
> **E**lse → **L**atency or **C**onsistency

| Scenario | Choice |
|---|---|
| During a **P**artition | **A**vailability or **C**onsistency |
| **E**lse (normal operation) | **L**atency or **C**onsistency |

---

## Why Latency vs Consistency?

To guarantee strong consistency in normal operation, a system must:
- Wait for **all replicas to acknowledge** a write before confirming
- Use **quorum reads** to ensure the latest value

Both of these **increase latency**. Relaxing consistency allows faster responses.

---

## System Classifications

| System | Partition behavior | Normal behavior | Label |
|---|---|---|---|
| DynamoDB | Availability | Latency | PA/EL |
| Cassandra | Availability | Latency | PA/EL |
| CouchDB | Availability | Latency | PA/EL |
| MongoDB | Consistency | Consistency | PC/EC |
| HBase | Consistency | Consistency | PC/EC |
| MySQL Cluster | Consistency | Latency | PC/EL |
| Spanner (Google) | Consistency | Consistency | PC/EC |

**PA/EL** — Optimizes for availability and speed (NoSQL, high-scale)  
**PC/EC** — Optimizes for correctness (financial, critical systems)  
**PC/EL** — Strict during partitions, fast in normal operation

---

## Cassandra Tunable Consistency

Cassandra is interesting because it's tunable per-query:

- `ONE` — fastest, least consistent (PA/EL)
- `QUORUM` — majority of nodes must agree (middle ground)
- `ALL` — all nodes must agree (PC/EC)

This means Cassandra can shift its PACELC classification based on your consistency level setting.

---

## Key Insight

Most distributed systems spend 99.9% of their time in the "else" case (no partition). PACELC is more practically useful than CAP because it captures the **everyday** tradeoff system designers face.

When choosing a database, ask:
1. Can I tolerate stale reads? → Choose EL (low latency)
2. Do I need always-accurate reads? → Choose EC (strong consistency, higher latency)