# Consistent Hashing

Consistent hashing is a technique for distributing data across nodes in a way that minimizes redistribution when nodes are added or removed. It is foundational to scalable distributed storage systems.

---

## The Problem with Naive Hashing

Simple modulo-based hashing:
```
node = hash(key) % number_of_nodes
```

When you add or remove a node, `number_of_nodes` changes, and **almost every key remaps to a different node**.

**Example:** 3 nodes → 4 nodes. A key that mapped to node 2 might now map to node 1. Nearly all data must be moved.

For a cache, this means a near-total cache miss storm. For a database, it means massive data migration.

---

## How Consistent Hashing Works

1. Arrange all possible hash values in a **ring** (0 to 2^32 - 1, wrapping around)
2. Place each **node** on the ring by hashing its name/IP
3. Place each **key** on the ring by hashing it
4. A key is assigned to the **first node clockwise** from its position on the ring

```
        0
       / \
   N3       N1
      \   /
        N2
      360°
```

Key K lands at position X → walk clockwise → find first node → that's K's node.

---

## Adding a Node

When a new node N4 is added, only keys between N3 and N4 (counterclockwise) need to move to N4. All other keys are unaffected.

**Impact:** Only `1/N` of keys need to move (where N is the new total number of nodes).

---

## Removing a Node

When N2 is removed, its keys move to the next clockwise node (N1). All other keys stay put.

**Impact:** Only the removed node's keys need to redistribute.

---

## The Problem: Uneven Distribution

With few nodes, the ring may have large gaps — one node handles much more than its fair share.

**Example:** 3 nodes placed at positions 10, 11, and 12 on a 0-360 ring. Node at position 12 handles positions 13-360 (348° of the ring). Extremely uneven.

---

## Virtual Nodes (VNodes)

Each physical node is assigned **multiple positions** on the ring (virtual nodes). This smooths out the distribution.

```
Physical Node N1 → Virtual nodes at positions: 45, 130, 290
Physical Node N2 → Virtual nodes at positions: 80, 200, 350
```

More virtual nodes = more even distribution. Cassandra uses 256 vnodes per physical node by default.

**Benefits:**
- More even load distribution
- When a node is added/removed, load is spread across all remaining nodes (not just neighbors)
- Nodes with more capacity can get more virtual nodes

---

## Real-World Uses

| System | Usage |
|---|---|
| Cassandra | Data partitioning across nodes |
| DynamoDB | Internal partitioning |
| Redis Cluster | Key distribution across shards |
| Memcached | Client-side consistent hashing for cache sharding |
| Chord DHT | Peer-to-peer distributed hash table |
| Nginx (load balancer) | Consistent hashing by URI for upstream selection |
| CDN edge routing | Route users to the closest/least-loaded edge node |

---

## Consistent Hashing for Caching

A CDN or caching tier uses consistent hashing to route the same key to the same cache server. This maximizes cache hit rates.

Without consistent hashing: adding a cache server causes widespread cache misses as keys remap.

With consistent hashing: adding a server causes only `1/N` of keys to miss and warm up on the new server.

---

## Summary

| | Naive Hashing | Consistent Hashing |
|---|---|---|
| Node addition | ~100% keys remapped | ~1/N keys remapped |
| Node removal | ~100% keys remapped | ~1/N keys remapped |
| Distribution | Even (with good hash) | Uneven (fix with vnodes) |
| Implementation | Simple | More complex |
| Use case | Single-server | Distributed, elastic clusters |

The core insight: design the mapping so that adding or removing a node only affects its immediate neighbors, not the entire key space.