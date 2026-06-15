# Caching Strategies

Caching stores copies of data in a faster layer to reduce latency and load on the origin. The choice of write strategy, read strategy, and eviction policy determines correctness, consistency, and performance.

---

## Write Strategies

### Write-Through

Data is written to the **cache and the database simultaneously**.

```
Client → Write → Cache → Write → Database
                  ↑
             synchronous
```

**Pros:**
- Cache is always consistent with the database
- No data loss on cache failure (data already in DB)

**Cons:**
- Higher write latency (must wait for both cache and DB)
- Cache may fill up with infrequently read data (written but never read)

**Use when:** Read-heavy workloads where data freshness is critical.

---

### Write-Back (Write-Behind)

Data is written to the **cache only**. The database write happens asynchronously later.

```
Client → Write → Cache → (async, batched) → Database
```

**Pros:**
- Low write latency — client doesn't wait for DB
- Batching reduces DB write load

**Cons:**
- Risk of data loss if cache crashes before flushing to DB
- More complex to implement (need durable queue or WAL)

**Use when:** Write-heavy workloads where some data loss is acceptable (analytics counters, non-critical writes).

---

### Write-Around

Data is written **directly to the database, bypassing the cache**. Cache is only populated on reads.

```
Client → Write → Database (cache bypassed)
Client → Read  → Cache miss → Database → Cache populated
```

**Pros:**
- Avoids polluting cache with write-once data never read again
- Simpler than write-through for write-heavy, read-rare data

**Cons:**
- First read after write is always a cache miss (higher latency)

**Use when:** Data written rarely and read rarely — log files, batch job outputs.

---

## Read Strategies

### Cache-Aside (Lazy Loading)

The application manages the cache directly. On a miss, it loads from DB and populates the cache.

```
Read request:
  → Check cache
  → Hit: return from cache
  → Miss: read from DB → write to cache → return

Write request:
  → Write to DB
  → Invalidate or update cache entry
```

**Pros:**
- Cache only contains data that was actually requested
- Resilient — if cache fails, reads fall through to DB

**Cons:**
- First request for any key is always a miss
- Potential for stale data if DB updates don't invalidate cache
- Cache stampede: many concurrent misses for the same key all hit the DB

**Used by:** Most application-level caches (Redis + application code).

---

### Read-Through

Cache sits in front of DB. On a miss, the **cache itself** fetches from DB and stores it.

```
Client → Cache (miss) → Cache fetches from DB → returns to client
Client → Cache (hit)  → returns directly
```

**Pros:**
- Application doesn't need to know about DB — cleaner separation
- Same as cache-aside in behavior, different in who manages the fill

**Cons:**
- Cache stampede still possible
- First access always misses

**Used by:** Managed caching services like ElastiCache with read-through plugins.

---

## Eviction Policies

When the cache is full, something must be removed to make room.

### LRU — Least Recently Used
Evict the item that was **accessed longest ago**.

- Best general-purpose policy
- Assumes recent access predicts future access
- Used by: Redis (default), Memcached

### LFU — Least Frequently Used
Evict the item accessed the **fewest times**.

- Better for workloads with stable hot/cold data
- More expensive to implement (must track access counts)
- Problem: recently added items are unfairly evicted before building up counts

### FIFO — First In, First Out
Evict the **oldest inserted** item regardless of access pattern.

- Simple to implement
- Ignores access patterns entirely
- Rarely optimal

### TTL — Time to Live
Items expire after a set duration regardless of access.

- Used to ensure freshness (cache invalidation by time)
- Combined with LRU/LFU for hybrid approaches
- Critical for session tokens, rate limiting, temporary data

### Random
Evict a **random** item.

- Surprisingly competitive in some workloads
- Simple to implement
- Used as a fallback or in specialized hardware caches

---

## Cache Invalidation Strategies

> "There are only two hard things in Computer Science: cache invalidation and naming things." — Phil Karlton

| Strategy | Description | Risk |
|---|---|---|
| TTL expiry | Item expires after N seconds | Stale data until TTL expires |
| Event-driven | DB write triggers cache delete | Complexity, dual-write failures |
| Write-through | Cache updated on every write | Consistency at cost of write latency |
| Version keys | `user:123:v2` — new version = new key | Cache never cleaned up (storage grows) |

---

## Cache Stampede (Thundering Herd)

When a popular cached item expires, many concurrent requests all miss and hit the database simultaneously.

**Solutions:**
- **Mutex/lock:** First miss acquires a lock, others wait for the result
- **Probabilistic early expiration:** Randomly refresh before TTL expires
- **Background refresh:** A separate job refreshes cache before expiry
- **Stale-while-revalidate:** Serve stale data while refreshing in the background

---

## Where to Cache

| Layer | Examples | Best for |
|---|---|---|
| Client-side | Browser cache, HTTP cache | Static assets, API responses |
| CDN | Cloudflare, Akamai | Static files, edge caching |
| Application | In-process (Caffeine, Guava) | Session data, computed values |
| Distributed cache | Redis, Memcached | Shared state across instances |
| Database | Query cache, buffer pool | Repeated identical queries |