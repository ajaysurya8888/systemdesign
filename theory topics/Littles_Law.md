# Little's Law

Little's Law is a fundamental theorem in queueing theory that relates throughput, concurrency, and latency. It applies to any stable system — web servers, databases, call centers, traffic lanes.

---

## The Formula

```
L = λ × W
```

Where:
- `L` = average number of items **in the system** (concurrency / queue depth)
- `λ` (lambda) = average **arrival rate** (items per second)
- `W` = average **time** each item spends in the system (latency)

All three are averages over the same time window. The relationship is exact for any stable system regardless of distribution.

---

## Intuition

If a coffee shop serves 10 customers per minute (λ = 10/min) and each customer spends 5 minutes (W = 5 min), there are always 50 customers in the shop (L = 50).

The law doesn't care about the distribution of arrival times or service times — it holds universally.

---

## Rearranging for System Design

The power of Little's Law is that knowing any two values lets you calculate the third.

**Calculate concurrency from throughput and latency:**
```
L = λ × W
concurrent_requests = RPS × avg_latency_seconds
```

**Calculate throughput from concurrency and latency:**
```
λ = L / W
RPS = concurrent_requests / avg_latency_seconds
```

**Calculate latency from concurrency and throughput:**
```
W = L / λ
avg_latency = concurrent_requests / RPS
```

---

## Practical Examples

### How many threads does my service need?

Your service handles 500 RPS and each request takes 100ms on average.

```
L = λ × W
L = 500 req/s × 0.1 s = 50 concurrent requests
```

You need a thread pool (or connection pool) of at least 50 to avoid queuing.

---

### What happens to latency when load increases?

You have 50 threads and load increases from 500 RPS to 1000 RPS:

```
W = L / λ = 50 / 1000 = 0.05s = 50ms
```

But if the service is CPU-bound and can't process faster, queuing begins. Latency rises as requests wait for a free thread. At saturation, latency grows unboundedly.

---

### Database connection pool sizing

Your app makes 200 database queries per second, and each query takes 10ms:

```
L = 200 × 0.01 = 2 concurrent DB connections needed
```

A pool of 5-10 is more than sufficient. 200 connections would be wasteful.

---

### Queue depth monitoring

If your message queue has 10,000 messages and you're processing 100 messages/second:

```
W = L / λ = 10,000 / 100 = 100 seconds of backlog
```

This tells you how long a new message will wait before being processed.

---

## When Little's Law Breaks Down

Little's Law assumes a **stable system** — arrival rate ≤ service rate. When the system is overloaded:

- The queue grows unboundedly (unstable)
- Average wait time goes to infinity
- The law still holds mathematically, but the values are useless for planning

The fix is to ensure `λ < μ` (arrival rate < service rate), or apply backpressure and load shedding.

---

## Connection to Other Concepts

- **Amdahl's Law** — limits speedup from parallelism
- **Little's Law** — tells you the concurrency needed to sustain a throughput target at a given latency
- Together they guide capacity planning: how many workers you need, and what the ceiling on parallelism is

---

## Key Formulas at a Glance

```
L = λ × W           (concurrency = throughput × latency)
λ = L / W           (throughput = concurrency / latency)
W = L / λ           (latency = concurrency / throughput)

Thread pool size  = target_RPS × avg_latency_seconds
DB pool size      = query_RPS × avg_query_seconds
Queue backlog age = queue_depth / consumer_rate
```

Little's Law is the most useful back-of-napkin tool for capacity planning in any queuing system.