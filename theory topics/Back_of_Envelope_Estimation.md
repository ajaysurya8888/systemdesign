# Back-of-the-Envelope Estimation

Back-of-the-envelope (BOE) estimation is the skill of producing rough but useful numerical estimates during system design. The goal is not precision — it's to detect order-of-magnitude problems and inform architectural decisions before writing a single line of code.

---

## Why It Matters

Estimation answers questions like:
- Can one database handle this load, or do we need sharding?
- How much storage do we need for 5 years of user data?
- Will this fit in RAM, or must we go to disk?
- How many servers do we need?

A 10× error in an estimate still tells you whether you need 1 server or 10, or 1 TB or 10 TB — and that's enough to make the right architectural call.

---

## The Number Toolkit

Memorize these. Everything else is derived.

### Powers of 2

| Power | Value | Common name |
|---|---|---|
| 2^10 | 1,024 | ~1 thousand (KB) |
| 2^20 | 1,048,576 | ~1 million (MB) |
| 2^30 | ~1 billion | ~1 billion (GB) |
| 2^40 | ~1 trillion | ~1 trillion (TB) |

### Time Conversions

| Period | Seconds |
|---|---|
| 1 minute | 60 |
| 1 hour | 3,600 |
| 1 day | 86,400 ≈ 10^5 |
| 1 month | ~2.6 × 10^6 |
| 1 year | ~3.15 × 10^7 ≈ 3 × 10^7 |

### Latency Numbers (Jeff Dean's Numbers — memorize these)

| Operation | Latency |
|---|---|
| L1 cache reference | 1 ns |
| L2 cache reference | 4 ns |
| Main memory (RAM) reference | 100 ns |
| Read 1 MB from RAM | 250 µs |
| SSD random read | 150 µs |
| Read 1 MB from SSD | 1 ms |
| HDD seek | 10 ms |
| Read 1 MB from HDD | 20 ms |
| Round trip within same datacenter | 500 µs |
| Round trip US → Europe | 150 ms |

**Key insight:** RAM is 1000× faster than SSD, SSD is 10× faster than HDD. If something fits in RAM, keep it there.

### Data Size Reference

| Item | Size |
|---|---|
| ASCII character | 1 byte |
| Integer (int32) | 4 bytes |
| Long (int64) | 8 bytes |
| UUID | 16 bytes |
| IPv4 address | 4 bytes |
| IPv6 address | 16 bytes |
| Timestamp (unix) | 8 bytes |
| Average tweet | ~300 bytes |
| Average web page | ~2 MB |
| Photo (compressed) | ~300 KB |
| 1-minute video (720p) | ~50–100 MB |
| 1-minute audio (MP3) | ~1 MB |

---

## Estimation Framework

Use this 4-step process:

### Step 1 — Assumptions
State your assumptions explicitly. If they're wrong, the estimate fails — so make them visible.

### Step 2 — Scale (DAU, QPS)
- How many daily active users (DAU)?
- What fraction perform the action in question?
- How many times per day?
- → **Requests per second (QPS)**

```
QPS = (DAU × actions_per_day) / seconds_per_day
```

### Step 3 — Storage
- What data is generated per action?
- What is the retention period?
- Add replication factor and overhead.

```
storage = QPS × data_per_request × retention_seconds × replication_factor
```

### Step 4 — Resources
- How many servers/cores needed?
- Can it fit in RAM?
- What is the bandwidth requirement?

---

## Worked Example 1 — Twitter-Scale Feed

**Given:** 300M DAU, each user reads feed 5 times/day, each read fetches 20 tweets.

**QPS (read):**
```
300M users × 5 reads/day = 1.5B reads/day
1.5B / 86,400s = ~17,000 reads/sec ≈ 17K QPS (read)
```

**QPS (write — tweets):**
```
Assume 1% of users tweet once/day
300M × 1% × 1 = 3M tweets/day
3M / 86,400 = ~35 QPS (write)
```
Write QPS is tiny. This is a read-heavy system — design for read scale.

**Tweet storage (10 years):**
```
35 tweets/sec × 300 bytes/tweet = 10,500 bytes/sec ≈ 10 KB/s
10 KB/s × 3 × 10^7 s/year × 10 years = 3 TB / year × 10 = 30 TB raw
× 3 replication = ~90 TB
```
90 TB is very manageable — fits in a moderately sized distributed DB cluster.

---

## Worked Example 2 — YouTube-Scale Video Storage

**Given:** 500 hours of video uploaded every minute.

**Storage per minute of ingestion:**
```
500 hours × 60 min/hour = 30,000 minutes of video per minute
Average video: 500 MB / hour = ~8 MB/min

30,000 min × 8 MB/min = 240,000 MB/min = 240 GB/min
240 GB/min × 60 = 14,400 GB/hour = ~14 TB/hour
14 TB/hour × 24 × 365 = ~123 PB/year
```

Multiple resolutions (360p, 720p, 1080p, 4K) → multiply by ~4:
```
~500 PB/year of raw video storage
```

**Takeaway:** This requires object storage at massive scale (S3-class), not a traditional filesystem. CDN is essential — serving from origin directly would be impossible.

---

## Worked Example 3 — WhatsApp Message Storage

**Given:** 65B messages/day, average message 100 bytes.

**Daily storage:**
```
65 × 10^9 × 100 bytes = 6.5 × 10^12 bytes = 6.5 TB/day
```

**Per year:**
```
6.5 TB/day × 365 = ~2.4 PB/year
× 3 (replication) = ~7 PB/year
```

**QPS:**
```
65B / 86,400 = ~750,000 messages/sec ≈ 750K QPS
```

This needs massive horizontal scaling — no single database handles 750K writes/sec. Sharding by user or conversation ID is required.

---

## Worked Example 4 — Server Count

**Given:** 1M QPS, each request takes 10ms, each server has 8 CPU cores.

**Concurrent requests a server handles:**
```
Using Little's Law: L = λ × W
L per server = (QPS / server_count) × response_time
```

Flip it: how many concurrent requests per server can we support?
```
cores × (1s / 10ms) = 8 × 100 = 800 concurrent req/s per server
```

Servers needed:
```
1,000,000 / 800 = 1,250 servers
```

Add 30% headroom for spikes:
```
~1,600 servers
```

---

## Common Estimation Pitfalls

| Mistake | Fix |
|---|---|
| Forgetting replication (usually 3×) | Always multiply storage by replication factor |
| Ignoring peak vs average (peak ≈ 3–5× average) | Design for peak, not average |
| Mixing up KB/MB/GB | Keep units consistent throughout |
| Forgetting compression (often 3–10× reduction) | Note when compression applies |
| Not including metadata overhead | Typically +20–30% to raw data size |
| Ignoring index storage | DB indexes are often 10–30% of data size |

---

## Quick Reference Card

```
1 day        = 86,400 sec    ≈ 10^5 sec
1 year       ≈ 3 × 10^7 sec
1 million    = 10^6
1 billion    = 10^9

1 KB = 10^3 bytes
1 MB = 10^6 bytes
1 GB = 10^9 bytes
1 TB = 10^12 bytes
1 PB = 10^15 bytes

QPS          = (DAU × actions/day) / 86,400
Storage/day  = QPS × bytes/request × 86,400
Servers      = total_QPS / QPS_per_server

Peak traffic ≈ 3–5× average
Replication  = 3× (typical)
Read : Write ≈ 80:20 (typical web app)
```

---

## BOE in System Design Interviews

1. **State assumptions out loud** — interviewers want to see your reasoning, not the final number
2. **Round aggressively** — 86,400 → 10^5, 300M × 5 → 1.5B is fine
3. **Identify the bottleneck** — after estimating, say what the numbers imply ("this is read-heavy, so we need caching")
4. **Sanity check against known systems** — YouTube, WhatsApp, Twitter scales are public knowledge; use them as anchors
5. **Don't get lost in arithmetic** — the insight matters more than the exact number