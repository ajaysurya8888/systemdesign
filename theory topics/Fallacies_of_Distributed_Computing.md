# Fallacies of Distributed Computing

Eight incorrect assumptions that developers new to distributed systems commonly make. Originally identified at Sun Microsystems by L Peter Deutsch and others. Each fallacy, when assumed true, leads to fragile, poorly-performing, or incorrect systems.

---

## Fallacy 1 — The Network is Reliable

**The assumption:** Network calls succeed like local function calls.

**The reality:** Networks drop packets, connections time out, routers fail, cables are cut.

**What goes wrong:** Code that doesn't handle network failures will silently lose data, hang forever, or crash.

**Fix:**
- Always set timeouts on network calls
- Implement retries with exponential backoff
- Design for idempotency so retries are safe
- Use circuit breakers to fail fast

---

## Fallacy 2 — Latency is Zero

**The assumption:** A remote call is as fast as a local call.

**The reality:** A local function call takes nanoseconds. A cross-datacenter HTTP call takes 50-150ms. Satellite links can be 600ms+.

**What goes wrong:** Chatty APIs (10 calls to build one page), no caching, synchronous call chains — all compound latency and make systems feel slow.

**Fix:**
- Batch multiple operations into fewer calls
- Cache aggressively
- Use async/parallel calls where possible
- Minimize cross-datacenter traffic for latency-sensitive paths

---

## Fallacy 3 — Bandwidth is Infinite

**The assumption:** You can send as much data as you want over the network.

**The reality:** Bandwidth has limits and costs money. Sending large payloads or high volumes causes congestion, throttling, and cost overruns.

**What goes wrong:** Sending entire objects when only one field changed, no pagination, returning megabytes when kilobytes suffice.

**Fix:**
- Use pagination for large datasets
- Return only requested fields (GraphQL, sparse fieldsets)
- Compress data (gzip, protobuf instead of JSON)
- Cache at CDN edges to avoid repeated large transfers

---

## Fallacy 4 — The Network is Secure

**The assumption:** Data in transit is safe from interception or tampering.

**The reality:** Networks are shared infrastructure. Packets can be intercepted, man-in-the-middle attacks are real, internal networks are not immune.

**What goes wrong:** Sending credentials or sensitive data over plain HTTP, trusting internal network traffic without validation.

**Fix:**
- Always use TLS/HTTPS, even internally
- Validate and authenticate all requests regardless of origin
- Apply zero-trust networking principles — verify everything, trust nothing by default
- Rotate secrets and certificates regularly

---

## Fallacy 5 — Topology Doesn't Change

**The assumption:** The network topology (which servers exist, where they are) is static.

**The reality:** Servers are added, removed, moved, and replaced constantly. Cloud environments are especially dynamic — autoscaling, spot instances, rolling deploys.

**What goes wrong:** Hardcoded IP addresses, no service discovery, clients caching server lists that go stale.

**Fix:**
- Use service discovery (Consul, Kubernetes DNS, AWS Service Discovery)
- Never hardcode IP addresses — use DNS names
- Implement health checks so dead nodes are removed from rotation
- Design clients to handle endpoint changes gracefully

---

## Fallacy 6 — There is One Administrator

**The assumption:** One person or team controls the entire system.

**The reality:** Distributed systems span teams, organizations, cloud providers, and regulatory jurisdictions. Each piece has different owners with different priorities.

**What goes wrong:** Assuming you can coordinate changes across all components simultaneously. Assuming everyone will upgrade at the same time. Assuming unified logging and monitoring.

**Fix:**
- Design for backward compatibility — old and new versions coexist during deploys
- Version your APIs
- Build observability into every component independently
- Assume you cannot coordinate with all owners simultaneously

---

## Fallacy 7 — Transport Cost is Zero

**The assumption:** Moving data over the network is free.

**The reality:** There are two costs:
1. **Financial cost:** Cloud egress fees, inter-AZ transfer costs, CDN costs
2. **Time cost:** Serialization and deserialization of data (JSON parsing, protobuf encoding) takes CPU time

**What goes wrong:** Chatty services with tiny payloads, no batching, no compression, moving data unnecessarily between regions.

**Fix:**
- Colocate data and compute that communicate frequently
- Use binary serialization (protobuf) for high-throughput internal services
- Batch small messages
- Be aware of cloud provider egress pricing

---

## Fallacy 8 — The Network is Homogeneous

**The assumption:** All nodes in the network use the same technology, protocol, and data format.

**The reality:** Systems are polyglot — different teams use different languages, frameworks, and serialization formats. External partners have their own stacks. Legacy systems don't speak modern protocols.

**What goes wrong:** Assuming all services understand the same encoding, locale, character set, or time zone. Assuming all clocks are synchronized.

**Fix:**
- Use standard, interoperable protocols (HTTP, JSON, gRPC) for cross-system communication
- Be explicit about encodings (UTF-8), time zones (UTC), and date formats (ISO 8601)
- Build adapters and anti-corruption layers when integrating with external systems
- Never assume NTP-synchronized clocks — use logical clocks or vector clocks for ordering events

---

## Summary

| # | Fallacy | Real-World Fix |
|---|---|---|
| 1 | Network is reliable | Timeouts, retries, circuit breakers |
| 2 | Latency is zero | Batching, caching, async calls |
| 3 | Bandwidth is infinite | Pagination, compression, sparse responses |
| 4 | Network is secure | TLS everywhere, zero-trust |
| 5 | Topology doesn't change | Service discovery, dynamic clients |
| 6 | One administrator | API versioning, backward compatibility |
| 7 | Transport cost is zero | Colocation, batching, binary protocols |
| 8 | Network is homogeneous | Standard protocols, explicit encodings |

These fallacies explain why distributed systems are fundamentally harder than single-node systems. Acknowledging them upfront leads to more resilient designs.