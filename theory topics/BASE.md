# BASE Properties

BASE is the alternative consistency model to ACID, adopted by most NoSQL and distributed databases. It prioritizes availability and performance over strict consistency.

Coined by Eric Brewer (who also gave us CAP theorem).

---

## B — Basically Available

The system **guarantees availability** — it will always respond to a request, even if the response is stale or partial.

**Example:** Amazon's product page always loads, even during a partial outage. You might see an old price or "unavailable" stock count, but the page is never down.

The system may return degraded responses rather than refusing requests.

---

## S — Soft State

The state of the system **may change over time**, even without new input. This happens because of eventual consistency — background processes are continuously syncing data.

**Example:** After you post a tweet, your follower count or like count might differ across data centers for a few seconds. The state is "soft" — it's in flux until all nodes converge.

This is the opposite of ACID's hard, consistent state.

---

## E — Eventually Consistent

Given enough time and no new updates, **all replicas will converge** to the same value.

**Example:** After updating your Facebook profile photo, some users may see the old photo for a few seconds. Eventually, all servers sync and everyone sees the new one.

**This is not:**
- No consistency (data is never lost)
- Random consistency (there is a guaranteed convergence)

---

## Eventual Consistency Models

| Model | Description |
|---|---|
| Read-your-writes | You always see your own updates immediately |
| Monotonic reads | You never see older data after seeing newer data |
| Causal consistency | Operations that are causally related are seen in order |
| Strong eventual | Once converged, all nodes agree (CRDTs use this) |

---

## When to Use BASE

- Social media feeds
- Shopping carts (Amazon famously uses eventual consistency for carts)
- DNS propagation
- Caching layers
- Analytics and logging systems

---

## BASE vs ACID

| | ACID | BASE |
|---|---|---|
| Consistency | Immediate, strong | Eventual, weak |
| Availability | May block or reject | Always responds |
| Partition handling | May refuse writes | Accepts writes, syncs later |
| Complexity | Database handles it | Application must handle conflicts |
| Examples | MySQL, PostgreSQL | DynamoDB, Cassandra, CouchDB |

---

## Key Tradeoff

BASE gives you scale and availability at the cost of complexity — your application code must now handle conflicts, stale reads, and convergence logic that ACID databases handle automatically.