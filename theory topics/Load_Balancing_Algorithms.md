# Load Balancing Algorithms

A load balancer distributes incoming requests across a pool of servers. The algorithm determines which server handles each request. Each algorithm has different trade-offs around fairness, stickiness, and server heterogeneity.

---

## 1. Round Robin

Requests are distributed to servers **in order**, cycling back to the first after reaching the last.

```
Request 1 → Server A
Request 2 → Server B
Request 3 → Server C
Request 4 → Server A  ← wraps around
Request 5 → Server B
```

**Pros:**
- Simple to implement
- Even distribution when all requests have equal cost

**Cons:**
- Ignores server load — a slow request on Server A still means Server B gets the next request
- Ignores server capacity — all servers treated equally regardless of spec

**Best for:** Stateless services with homogeneous servers and uniform request cost.

---

## 2. Weighted Round Robin

Like Round Robin, but servers receive requests proportional to their assigned weight.

```
Server A (weight 3) → gets 3 requests per cycle
Server B (weight 1) → gets 1 request per cycle
Server C (weight 2) → gets 2 requests per cycle

Cycle: A, A, A, B, C, C → repeat
```

**Pros:**
- Handles servers with different capacities
- Still simple, no real-time metrics needed

**Cons:**
- Weights are static — doesn't adapt to actual runtime load
- If a server gets stuck on expensive requests, it still receives its full share

**Best for:** Mixed-capacity server pools where relative capacities are known in advance.

---

## 3. Least Connections

Each new request is sent to the server with the **fewest active connections** at that moment.

```
Server A: 10 active connections
Server B: 3 active connections  ← next request goes here
Server C: 7 active connections
```

**Pros:**
- Adapts to varying request durations automatically
- Naturally handles slow requests — a server bogged down with long-running requests gets fewer new ones

**Cons:**
- Requires tracking active connection count per server
- Slightly more complex than Round Robin
- Doesn't account for server capacity (still treats all servers equally)

**Best for:** Workloads with highly variable request durations (long-polling, file uploads, streaming, WebSockets).

---

## 4. Weighted Least Connections

Combines Least Connections with server weights. The server with the lowest **effective load** (connections / weight) gets the next request.

```
effective_load = active_connections / weight

Server A: 10 connections, weight 5 → effective load = 2.0
Server B: 3 connections,  weight 1 → effective load = 3.0
Server C: 6 connections,  weight 3 → effective load = 2.0

Next request → Server A or C (tied lowest effective load)
```

**Best for:** Heterogeneous server pools with variable request durations.

---

## 5. IP Hash (Sticky Sessions)

The client's IP address is hashed, and the result determines which server handles the request. The same IP always goes to the same server.

```
hash(client_IP) % number_of_servers = server_index
```

**Pros:**
- Session persistence — useful when servers hold session state
- Deterministic — no coordination needed between load balancers

**Cons:**
- Uneven distribution if many clients share an IP (corporate NAT, proxy)
- Adding/removing servers causes most IPs to remap (use consistent hashing to fix this)
- Ties availability to the specific server — if the server dies, the session is lost anyway

**Best for:** Applications with server-side session state that haven't yet moved sessions to a shared store (Redis).

**Note:** Prefer moving sessions to Redis and using a stateless algorithm — it's more resilient.

---

## 6. Least Response Time

Combines active connections with server response time. Requests go to the server with the best combination of fewest connections and fastest response time.

```
score = active_connections × avg_response_time
```

Lowest score wins the next request.

**Pros:**
- Most adaptive to real server performance
- Handles both load and latency differences

**Cons:**
- Requires active health check probes to measure response time
- More complex state to maintain

**Used in:** HAProxy, Nginx Plus, AWS ALB (with target group metrics).

---

## 7. Random

Pick a server at random from the pool.

```
server = random.choice(server_pool)
```

**Pros:**
- Stateless — no tracking needed
- Surprisingly good distribution with large request volumes (law of large numbers)
- Simple to implement, no synchronization needed across LB instances

**Cons:**
- No guarantee of evenness at low traffic volumes
- Doesn't account for server load or capacity

**Used in:** Netflix's Ribbon (with "power of two choices" — pick 2 random servers, route to the less loaded one).

---

## 8. Power of Two Random Choices

A clever improvement on random: pick **2 random servers**, route to the **less loaded** of the two.

```
candidates = random.sample(server_pool, 2)
route to: min(candidates, key=lambda s: s.connections)
```

**Why it works:** Mathematically proven to approach optimal distribution with O(log log N) max load, vs O(log N / log log N) for pure random. Used at scale by Netflix, NGINX, and others.

---

## 9. Resource-Based / Adaptive

The load balancer queries each server's actual resource utilization (CPU %, memory %) via an agent or API, then routes to the most available server.

**Pros:**
- Most accurate real-world load distribution

**Cons:**
- Requires health agents on each server
- Higher overhead from constant polling
- Latency between measurement and routing decision

**Used in:** AWS ALB target groups with custom health metrics, Nginx Plus.

---

## Comparison Table

| Algorithm | State Needed | Handles Variable Load | Handles Unequal Servers | Sticky |
|---|---|---|---|---|
| Round Robin | None | No | No | No |
| Weighted Round Robin | Weights only | No | Yes | No |
| Least Connections | Connection count | Yes | No | No |
| Weighted Least Connections | Count + weights | Yes | Yes | No |
| IP Hash | None | No | No | Yes |
| Least Response Time | Count + latency | Yes | Yes | No |
| Random | None | No | No | No |
| Power of Two Choices | Connection count | Yes | No | No |

---

## Layer 4 vs Layer 7 Load Balancing

| | L4 (Transport Layer) | L7 (Application Layer) |
|---|---|---|
| Routing basis | IP address + TCP port | HTTP headers, URL path, cookies, body |
| SSL termination | No | Yes |
| Content-based routing | No | Yes (`/api` → service A, `/static` → CDN) |
| Speed | Faster | Slightly slower (parses HTTP) |
| Health checks | TCP connection | HTTP endpoint (`/health`) |
| Examples | AWS NLB, HAProxy TCP | AWS ALB, Nginx, Envoy, Traefik |

**L7 enables:**
- Route `/api/v1` to old service, `/api/v2` to new service
- Blue/green deployments (route % of traffic to new version)
- A/B testing (route users in group A to variant, group B to control)
- Rate limiting per user/endpoint

---

## Which Algorithm to Choose

| Workload | Recommended Algorithm |
|---|---|
| Uniform request cost, equal servers | Round Robin |
| Mixed server capacities | Weighted Round Robin |
| Variable request durations (uploads, WebSockets) | Least Connections |
| Large-scale stateless services | Random or Power of Two Choices |
| Legacy stateful app (can't use shared sessions) | IP Hash (short-term) |
| Precise load awareness needed | Least Response Time or Resource-Based |