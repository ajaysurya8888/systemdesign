# CAP Theorem

CAP Theorem (also called Brewer's Theorem, proposed by Eric Brewer in 2000) states that a distributed system can guarantee **at most 2 out of 3** properties simultaneously.

---

## The Three Properties

### C — Consistency
Every read receives the **most recent write** or an error. All nodes see the same data at the same time.

### A — Availability
Every request receives a **response** (not necessarily the most recent data). The system never refuses a request.

### P — Partition Tolerance
The system **continues to operate** even when network partitions occur (messages between nodes are lost or delayed).

---

## The Core Insight

**Network partitions are unavoidable** in any real distributed system. Servers go down, cables are cut, packets are dropped. This means **P is not optional** — you must tolerate partitions.

Therefore, the real choice is between:
- **CP** — Consistency + Partition Tolerance (sacrifice Availability)
- **AP** — Availability + Partition Tolerance (sacrifice Consistency)

"CA" without partition tolerance only works on a single-node system — not truly distributed.

---

## CP Systems (Consistency over Availability)

When a partition happens, the system **refuses to respond** rather than return stale data.

**Examples:** HBase, Zookeeper, etcd, MongoDB (default config)

**Use when:** Correctness is critical — financial transactions, inventory management, config management.

**Behavior during partition:** Returns errors or timeouts instead of stale data.

---

## AP Systems (Availability over Consistency)

When a partition happens, the system **still responds** but may return stale data. Nodes sync after the partition heals.

**Examples:** Cassandra, DynamoDB, CouchDB, DNS

**Use when:** Always-on is critical — social feeds, product catalogs, shopping carts.

**Behavior during partition:** Returns possibly stale data, reconciles later (eventual consistency).

---

## Real-World Examples

| System | Type | Reason |
|---|---|---|
| Cassandra | AP | Tunable consistency, availability-first |
| HBase | CP | Strong consistency for analytics |
| Zookeeper | CP | Leader election needs consistency |
| DynamoDB | AP (tunable) | High availability, eventual by default |
| PostgreSQL (single node) | CA | Not truly distributed |
| MongoDB | CP | Primary always has latest data |

---

## Limitations of CAP

CAP is a simplification. Real systems are nuanced:

1. **Partitions are rare** — most of the time you're choosing between latency and consistency (see PACELC).
2. **"All or nothing" is too binary** — systems can be tuned (e.g., Cassandra's quorum reads).
3. **Consistency has many levels** — strong, causal, eventual are all different.

---

## CAP → PACELC

PACELC extends CAP to address the normal (non-partition) case:

> **If Partition:** choose **A** or **C**  
> **Else (normal operation):** choose **L**atency or **C**onsistency

Most real systems optimize for low latency in normal operation, and that choice also has a consistency tradeoff.