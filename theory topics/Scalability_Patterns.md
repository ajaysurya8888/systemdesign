# Scalability Patterns

Scalability is a system's ability to handle increased load by adding resources. Understanding when and how to scale is fundamental to system design.

---

## Horizontal vs Vertical Scaling

### Vertical Scaling (Scale Up)

Add more resources to the **same machine** — more CPU, more RAM, faster disk.

```
[Server 2 CPU, 8GB] → [Server 16 CPU, 64GB]
```

**Pros:**
- Simple — no application changes needed
- No distributed systems complexity
- Low latency (no network hops between components)

**Cons:**
- Hard upper limit — there's a biggest machine you can buy
- Single point of failure — one machine goes down, everything goes down
- Downtime required for hardware upgrades (usually)
- Expensive at high specs

**Use for:** Databases (initially), stateful components hard to distribute.

---

### Horizontal Scaling (Scale Out)

Add **more machines** of the same size.

```
[Server] → [Server] + [Server] + [Server]
```

**Pros:**
- Theoretically unlimited scale
- High availability — losing one server doesn't kill the system
- Use commodity hardware (cheaper per unit)

**Cons:**
- Requires stateless services (state must live in a shared layer)
- More operational complexity
- Network calls between services add latency
- Distributed systems problems (CAP, consistency, coordination)

**Use for:** Stateless services, web servers, API servers, worker processes.

---

## Load Balancing

Distribute requests across multiple servers to prevent any single server from becoming a bottleneck.

### Algorithms

| Algorithm | Description | Use Case |
|---|---|---|
| Round Robin | Each server gets requests in turn | Equal-capacity servers |
| Weighted Round Robin | Servers get requests proportional to weight | Mixed-capacity servers |
| Least Connections | Send to server with fewest active connections | Varying request durations |
| IP Hash | Same client IP → same server | Sticky sessions |
| Random | Pick server randomly | Simple, works well in practice |
| Resource-based | Send to server with most available CPU/memory | Resource-intensive workloads |

### Layer 4 vs Layer 7 Load Balancing

| | L4 (Transport) | L7 (Application) |
|---|---|---|
| Operates on | TCP/UDP packets | HTTP headers, URLs, cookies |
| Speed | Faster | Slower (but still fast) |
| Routing logic | IP/port only | URL path, host, headers |
| SSL termination | No | Yes |
| Examples | AWS NLB, HAProxy (TCP) | AWS ALB, Nginx, Envoy |

---

## Database Scaling

### Read Replicas

Add replicas that serve read traffic. The primary handles all writes; replicas stay in sync asynchronously.

```
        Writes
          ↓
       [Primary]
      /    |    \
[Replica] [Replica] [Replica]
    ↑
  Reads
```

**Good for:** Read-heavy workloads (most web applications are 80-90% reads).

**Limitation:** Replicas may lag slightly behind primary (replication delay).

---

### Sharding (Horizontal Partitioning)

Split data across multiple databases based on a shard key. Each shard owns a subset of the data.

```
user_id 1-1M   → Shard 1
user_id 1M-2M  → Shard 2
user_id 2M-3M  → Shard 3
```

**Sharding strategies:**
- **Range-based:** Shard by value range (can cause hotspots)
- **Hash-based:** Shard by `hash(key) % N` (even distribution, hard to rebalance)
- **Directory-based:** Lookup table maps keys to shards (flexible, single point of failure)
- **Consistent hashing:** Best of hash-based with easier rebalancing

**Challenges:**
- Cross-shard queries are expensive or impossible
- Transactions spanning multiple shards are complex
- Rebalancing shards as data grows is painful

---

### Vertical Partitioning

Split different columns of a table into different databases.

```
User table → [id, name, email] in DB1
             [id, profile_photo, preferences, bio] in DB2
```

**Good for:** When some columns are accessed much more than others (hot vs cold data).

---

## Stateless vs Stateful Architecture

Stateless services can scale horizontally trivially — any instance can handle any request.

**Making stateless:**
- Move sessions to Redis/Memcached
- Move uploads to S3
- Move computed state to the database
- Pass state in the request (JWT, signed cookies)

---

## Microservices Scaling

Each service scales independently based on its own load profile.

```
API Gateway     → 5 instances
User Service    → 3 instances
Payment Service → 10 instances (high load)
Email Service   → 1 instance
```

**Benefit:** Scale expensive/high-traffic services without scaling everything.

---

## Summary

| Technique | Solves | Complexity |
|---|---|---|
| Vertical scaling | Immediate capacity | Low |
| Horizontal scaling | Long-term capacity | Medium |
| Load balancing | Traffic distribution | Low |
| Read replicas | Read scalability | Medium |
| Sharding | Write scalability, data volume | High |
| Caching | Reduce load on downstream | Medium |
| CDN | Static asset delivery, edge caching | Low |
| Async processing | Decouple and buffer spiky load | Medium |

Scale incrementally — don't over-engineer. Start simple (single server, single DB), measure, then scale the bottleneck.