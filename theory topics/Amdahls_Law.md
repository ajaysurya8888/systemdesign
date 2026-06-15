# Amdahl's Law

Amdahl's Law quantifies the theoretical maximum speedup achievable when parallelizing a task. Formulated by computer architect Gene Amdahl in 1967.

---

## The Formula

```
Speedup = 1 / (S + (1 - S) / N)
```

Where:
- `S` = fraction of the task that is **sequential** (cannot be parallelized)
- `1 - S` = fraction that **can** be parallelized
- `N` = number of processors/threads

---

## Intuition

Even if you parallelize everything you can, the sequential portion becomes the bottleneck. Adding more processors gives diminishing returns.

**Example:** If 20% of a task is sequential (S = 0.2):

| Processors (N) | Max Speedup |
|---|---|
| 1 | 1× |
| 2 | 1.67× |
| 4 | 2.5× |
| 8 | 3.33× |
| 16 | 4× |
| ∞ | 5× |

No matter how many processors you add, you can **never exceed 5×** speedup. The sequential 20% is the hard ceiling.

---

## Key Insight

The maximum speedup with infinite processors is:

```
Max Speedup = 1 / S
```

If 10% is sequential → max 10× speedup  
If 50% is sequential → max 2× speedup  
If 1% is sequential → max 100× speedup

This is why reducing the sequential portion is more impactful than adding processors.

---

## Amdahl's Law in System Design

### Database queries
A report that requires aggregating data across 100 shards in parallel, then combining results (sequential step), is bounded by the combine step — not the shard reads.

### Microservices
A request that fans out to 5 services in parallel, then waits for all 5 to respond before responding, is bounded by the slowest service (the sequential "wait for all" step).

### Build systems
A build pipeline where compilation is parallelized but unit tests must run sequentially is bounded by the test phase.

### MapReduce
- Map phase: parallelizable across all nodes
- Reduce phase: depends on intermediate results, partially sequential
- The reduce barrier limits total speedup

---

## Amdahl vs Gustafson's Law

Amdahl's Law assumes **fixed problem size** — you're trying to solve the same problem faster.

Gustafson's Law argues that in practice, we use extra processors to **solve bigger problems** in the same time. The parallel fraction grows with scale.

| | Amdahl | Gustafson |
|---|---|---|
| Problem size | Fixed | Scales with processors |
| Conclusion | Speedup is bounded | Speedup scales linearly |
| Use case | Latency optimization | Throughput/capacity optimization |

**Amdahl:** "How much faster can I make this one request?"  
**Gustafson:** "How much more work can I do per second?"

---

## Practical Takeaway

1. **Profile before parallelizing** — find the sequential bottleneck first
2. **Reduce S aggressively** — even small reductions in the sequential fraction give large gains
3. **Don't just add workers** — adding 10× servers won't help if 30% of work is serial
4. **Applies to more than CPUs** — the same math governs distributed services, pipelines, and build systems

The law is a reminder that **parallelism has diminishing returns** and the sequential portion of any system is the true constraint.