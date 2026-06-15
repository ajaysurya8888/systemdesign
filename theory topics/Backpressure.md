# Backpressure

Backpressure is a mechanism where a **consumer signals a producer to slow down** when it cannot keep up with the rate of incoming data. It prevents system overload by propagating the signal upstream rather than letting buffers overflow or data get dropped silently.

---

## The Problem Without Backpressure

```
Producer → [unbounded queue] → Consumer
  1000 msg/s       overflowing    100 msg/s
```

- Queue grows unboundedly → memory exhaustion
- System crashes or starts dropping messages
- No signal to the producer — it keeps sending at full speed

---

## With Backpressure

```
Producer ← "slow down" ← Consumer
  100 msg/s                100 msg/s
```

The consumer signals the producer to match its processing speed. The system stays stable.

---

## Backpressure Strategies

### 1. Blocking (Synchronous)
Producer blocks (waits) when the consumer's buffer is full.

```
Producer → [bounded queue, size=10] → Consumer
           ← blocks when full
```

**Used in:** Java's `BlockingQueue`, Go channels with bounded capacity.

**Pros:** Simple, no data loss.  
**Cons:** Can cause cascading slowdowns; producer threads are held.

---

### 2. Drop / Shedding
When the consumer is overwhelmed, new messages are **dropped**.

**Variants:**
- Drop newest (ignore incoming)
- Drop oldest (evict from queue head)
- Drop random

**Used in:** UDP, real-time telemetry, non-critical event streams.

**Pros:** System never crashes from overload.  
**Cons:** Data loss; must be acceptable for the use case.

---

### 3. Rate Limiting / Throttling
Producer is explicitly told to slow down (e.g., 429 Too Many Requests, flow control signals).

**Used in:** HTTP APIs, TCP flow control (receive window), gRPC flow control.

---

### 4. Reactive Streams / Pull-based
Consumer pulls data from the producer at its own pace, requesting only what it can handle.

```
Consumer: "Give me 10 items"
Producer: sends 10 items
Consumer: "Give me 10 more" (only after processing the first 10)
```

**Used in:** Reactive Streams (RxJava, Project Reactor), Akka Streams, Kafka consumers (poll-based).

---

## Real-World Examples

### Kafka
Kafka consumers pull messages at their own rate. If a consumer is slow, it simply doesn't request more — no signal needed to the producer. The producer (Kafka broker) holds messages in a log.

### TCP
TCP has a built-in receive window. The receiver advertises how much buffer space it has. The sender stops when the window is zero — this is network-layer backpressure.

### gRPC Streaming
gRPC uses HTTP/2 flow control. Each stream has a flow control window. When the window is full, the sender is blocked.

### Node.js Streams
```javascript
const readable = fs.createReadStream('large-file.txt');
const writable = fs.createWriteStream('output.txt');

// pipe() automatically handles backpressure
readable.pipe(writable);
```

Without `pipe()`, if you write faster than the writable can flush, the internal buffer grows unboundedly. `pipe()` pauses the readable when the writable buffer is full.

---

## Backpressure in Microservices

```
Client → API Gateway → Service A → Service B → Database
```

If the database is slow, Service B slows down, which queues requests in Service A, which causes the API Gateway to timeout, which causes the client to retry — making it worse.

**Solutions:**
- Circuit breakers (fail fast instead of queuing)
- Bounded queues with explicit rejection
- Async processing with observable queue depth metrics

---

## Key Principle

Backpressure moves the **problem signal upstream** rather than letting it silently accumulate. A system with backpressure degrades gracefully under load; a system without it crashes suddenly.