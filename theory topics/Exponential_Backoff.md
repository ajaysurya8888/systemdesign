# Exponential Backoff & Retry Strategies

Backoff is a strategy for spacing out retry attempts after a failure. Instead of hammering a struggling service with immediate retries, the client waits progressively longer between each attempt.

---

## Why Naive Retries Are Dangerous

Imagine a service goes down. 10,000 clients all fail simultaneously and immediately retry.

```
T=0ms  → 10,000 requests fail
T=1ms  → 10,000 retries → service still down, now also overwhelmed
T=2ms  → 10,000 retries → worse
...
```

This is a **retry storm** — retries amplify the load on an already stressed system and prevent recovery.

---

## Linear Backoff

Wait a fixed amount of time between each retry.

```
Attempt 1 → fail → wait 1s
Attempt 2 → fail → wait 1s
Attempt 3 → fail → wait 1s
Attempt 4 → success
```

**Better than immediate retry, but:**
- All clients still retry at the same intervals — synchronized thundering herd
- Doesn't back off fast enough for serious outages

---

## Exponential Backoff

Wait time **doubles** with each failed attempt.

```
wait = base_delay × 2^attempt
```

```
Attempt 1 → fail → wait 1s   (2^0)
Attempt 2 → fail → wait 2s   (2^1)
Attempt 3 → fail → wait 4s   (2^2)
Attempt 4 → fail → wait 8s   (2^3)
Attempt 5 → fail → wait 16s  (2^4)
```

**Pros:**
- Pressure on the recovering service drops off quickly
- Clients self-regulate — heavy retriers back off fastest

**Problem remaining:** All clients still synchronize. If 10,000 clients all start retrying at T=0, they all retry again at T=1s, T=2s, T=4s together — still a thundering herd, just slower.

---

## Exponential Backoff with Jitter

Add **randomness** to desynchronize clients.

```
wait = random(0, base_delay × 2^attempt)
```

```
Client A: attempt 3 → waits 3.2s
Client B: attempt 3 → waits 1.7s
Client C: attempt 3 → waits 4.9s
```

Now clients spread their retries across the window instead of hitting simultaneously.

### Jitter Variants

**Full Jitter** (AWS recommended for most cases):
```
wait = random(0, min(cap, base × 2^attempt))
```
- Maximum randomness, best spreading
- Can have very short waits (client may retry too soon)

**Equal Jitter**:
```
half = min(cap, base × 2^attempt) / 2
wait = half + random(0, half)
```
- Guarantees minimum wait time (at least half the computed delay)
- Less spread than full jitter

**Decorrelated Jitter** (often best empirically):
```
wait = min(cap, random(base, previous_wait × 3))
```
- Each client's timing is independent of others
- Tends to produce the best overall throughput in simulations

**Fixed Jitter** (worst — avoid):
```
wait = base × 2^attempt + fixed_random_offset
```
- Clients still partially synchronized — offset is computed once

---

## Max Delay Cap

Without a cap, exponential backoff grows unboundedly:

```
Attempt 10 → wait = 1 × 2^10 = 1024 seconds (~17 minutes)
```

Always cap the maximum wait:

```
wait = min(MAX_DELAY, base × 2^attempt)
```

Common caps: 30s, 60s, 5 minutes depending on the use case.

---

## Max Retry Count

Always set a maximum number of attempts. Without it, a client retries forever.

```python
MAX_RETRIES = 5
BASE_DELAY  = 0.5   # seconds
MAX_DELAY   = 30    # seconds

for attempt in range(MAX_RETRIES):
    try:
        response = call_service()
        break
    except TransientError:
        if attempt == MAX_RETRIES - 1:
            raise  # give up after last attempt
        wait = min(MAX_DELAY, BASE_DELAY * (2 ** attempt))
        wait = wait / 2 + random.uniform(0, wait / 2)  # equal jitter
        time.sleep(wait)
```

---

## Retry-able vs Non-Retry-able Errors

Not all errors should be retried. Retrying a non-transient error wastes time.

| Error | Retry? | Reason |
|---|---|---|
| 500 Internal Server Error | Yes (usually) | Server-side transient failure |
| 503 Service Unavailable | Yes | Server overloaded, temporary |
| 429 Too Many Requests | Yes, with backoff | Rate limited — back off and slow down |
| 408 Request Timeout | Yes | Network/timeout issue |
| 400 Bad Request | No | Client bug — retrying sends same bad data |
| 401 Unauthorized | No | Auth failure — retrying without fixing auth won't help |
| 403 Forbidden | No | Permissions issue |
| 404 Not Found | No | Resource doesn't exist |

**Rule:** Only retry on errors that are **transient and idempotent-safe**.

---

## Retry Budgets

Instead of per-request retry counts, a **retry budget** limits the total fraction of requests that can be retries across the service.

```
retry_budget = 10%
→ if 100 requests/s arrive, max 10 retries/s are allowed
→ excess retries are dropped (fail fast)
```

Used by Google and Netflix to prevent retry amplification across microservice chains.

**Without a retry budget:**
```
Service A retries 3× → Service B retries 3× → Service C
→ 1 original request → up to 9 requests at Service C
```

**With a retry budget:** Service B only retries if its retry budget allows.

---

## Circuit Breaker + Backoff

Backoff handles individual request failures. A **circuit breaker** handles systemic service failure.

```
States:
  CLOSED   → requests flow normally
  OPEN     → requests fail immediately (no retry) for a cooldown period
  HALF-OPEN → one test request allowed through to check recovery
```

**Combined pattern:**
1. Requests fail → exponential backoff with jitter kicks in
2. Failure rate exceeds threshold → circuit breaker opens
3. During open state → fail immediately (no retries, no waiting)
4. After cooldown → circuit goes half-open, test request allowed
5. Test succeeds → circuit closes, normal operation resumes

This prevents clients from wasting time retrying when the service is clearly down.

---

## Backoff in Practice

### AWS SDK
```python
# boto3 uses exponential backoff with jitter automatically
# Configurable via botocore config
from botocore.config import Config

config = Config(retries={'max_attempts': 5, 'mode': 'adaptive'})
```

### HTTP Retry-After Header
Server can tell the client exactly how long to wait:
```http
HTTP/1.1 429 Too Many Requests
Retry-After: 30
```
Client should respect this value instead of its own backoff calculation.

### gRPC
Built-in retry policy configurable in the service config with exponential backoff parameters: `initial_backoff`, `max_backoff`, `backoff_multiplier`.

---

## Summary

| Strategy | Thundering Herd Risk | Complexity | Recommended |
|---|---|---|---|
| No retry | — | None | No |
| Immediate retry | Very High | None | No |
| Linear backoff | High | Low | No |
| Exponential backoff | Medium | Low | Baseline |
| Expo backoff + full jitter | Low | Low | Yes |
| Expo backoff + decorrelated jitter | Very Low | Medium | Best |
| Circuit breaker + backoff | Minimal | High | Production systems |

**Golden rule:** Always use exponential backoff + jitter + max cap + max retries. Never retry non-idempotent operations without idempotency keys.