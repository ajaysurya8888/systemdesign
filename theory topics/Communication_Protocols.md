# Communication Protocols

A communication protocol defines the rules for how two systems exchange data. Choosing the right protocol impacts performance, real-time capability, coupling, and developer experience.

---

## REST — Representational State Transfer

**Transport:** HTTP/1.1 or HTTP/2  
**Format:** JSON (most common), XML  
**Pattern:** Request / Response  
**Connection:** Stateless, short-lived

### How it works
Client sends an HTTP request to a URL endpoint. Server processes and responds. Connection closes.

```
GET  /users/123          → returns user data
POST /orders             → creates a new order
PUT  /orders/42          → replaces order 42
DELETE /orders/42        → deletes order 42
```

### Pros
- Universal — every language, browser, and tool speaks HTTP
- Human-readable (JSON)
- Stateless — easy to scale horizontally
- Works through firewalls and proxies
- Great tooling (Postman, curl, Swagger)
- HTTP caching works natively (GET responses cacheable)

### Cons
- Over-fetching: endpoint returns more fields than you need
- Under-fetching: one page may need 5+ separate API calls
- No real-time — polling required for live updates
- Versioning gets messy (`/v1`, `/v2`, breaking changes)

### Use when
- Public APIs
- CRUD operations
- Browser-to-server communication
- Interoperability with external systems

---

## GraphQL

**Transport:** HTTP (usually POST to a single `/graphql` endpoint)  
**Format:** JSON  
**Pattern:** Query / Mutation / Subscription  
**Connection:** Stateless (queries/mutations), long-lived (subscriptions over WebSocket)

### How it works
Client sends a typed query declaring exactly what data it needs. Server returns exactly that — no more, no less.

```graphql
# Client defines the shape of the response
query {
  user(id: "123") {
    name
    email
    orders {
      id
      total
    }
  }
}
```

Response:
```json
{
  "data": {
    "user": {
      "name": "Alice",
      "email": "alice@example.com",
      "orders": [{ "id": "42", "total": 500 }]
    }
  }
}
```

### Pros
- No over-fetching or under-fetching — client gets exactly what it asks for
- Single endpoint — no versioning problem
- Strongly typed schema — acts as contract between frontend and backend
- Introspection — auto-generated documentation
- Subscriptions for real-time data

### Cons
- Complexity: schema design, resolver architecture, N+1 query problem
- HTTP caching broken by default (all POSTs to one URL)
- Overkill for simple CRUD APIs
- Query complexity attacks (client sends deeply nested expensive queries)
- Slower to set up initially than REST

### N+1 Problem
Fetching a list of users and their orders naively runs:
- 1 query for users
- N queries for each user's orders

Fixed with DataLoader (batching and caching per request).

### Use when
- Complex, nested data requirements (dashboards, mobile apps)
- Multiple client types (web, mobile, TV) needing different data shapes
- Rapid frontend iteration without backend changes
- Aggregating data from multiple microservices (GraphQL federation)

---

## gRPC — Google Remote Procedure Call

**Transport:** HTTP/2  
**Format:** Protocol Buffers (Protobuf — binary)  
**Pattern:** Unary, Server Streaming, Client Streaming, Bidirectional Streaming  
**Connection:** Long-lived multiplexed HTTP/2 connection

### How it works
Define a service and message types in a `.proto` file. Generate client and server code in any language. Call remote methods like local function calls.

```proto
// user.proto
service UserService {
  rpc GetUser (UserRequest) returns (UserResponse);
  rpc ListUsers (ListRequest) returns (stream UserResponse);
}

message UserRequest { string id = 1; }
message UserResponse { string id = 1; string name = 2; string email = 3; }
```

Generated client usage (Go):
```go
client.GetUser(ctx, &UserRequest{Id: "123"})
```

### Streaming modes
| Mode | Description | Use Case |
|---|---|---|
| Unary | Single request, single response | Standard API call |
| Server streaming | One request, stream of responses | Live feeds, large datasets |
| Client streaming | Stream of requests, one response | File upload, batch input |
| Bidirectional | Both sides stream simultaneously | Chat, real-time collaboration |

### Pros
- Very fast: binary Protobuf serialization (5-10× smaller than JSON, faster to parse)
- HTTP/2: multiplexing, header compression, server push
- Strongly typed: `.proto` schema enforced at compile time
- Multi-language code generation (Go, Java, Python, C++, Node.js, etc.)
- Native streaming support
- Built-in deadline/timeout and cancellation propagation

### Cons
- Not human-readable (binary format — debugging requires tooling)
- Browser support limited (gRPC-Web required for browsers, not full gRPC)
- `.proto` schema changes require coordination and versioning
- More setup than REST (protoc compiler, generated code)
- Poor support for traditional HTTP caching

### Use when
- Internal microservice-to-microservice communication
- High-performance, low-latency internal APIs
- Streaming data (log ingestion, real-time feeds)
- Polyglot environments where strong typing across languages matters
- Mobile apps communicating with backend (efficient bandwidth usage)

---

## WebSockets

**Transport:** TCP (upgraded from HTTP)  
**Format:** Any (text or binary frames)  
**Pattern:** Full-duplex, persistent, bidirectional  
**Connection:** Long-lived, stateful

### How it works
Connection starts as an HTTP request, then upgrades to a WebSocket connection. After the handshake, both client and server can send messages to each other at any time without the overhead of HTTP headers.

```
HTTP Upgrade Handshake:
GET /ws HTTP/1.1
Connection: Upgrade
Upgrade: websocket

After handshake:
Client → "{"type":"subscribe","channel":"prices"}"
Server → "{"type":"price","symbol":"BTC","price":65432}"
Server → "{"type":"price","symbol":"ETH","price":3211}"  ← pushed without request
```

### Pros
- True real-time — server pushes data the moment it's available
- Low overhead after handshake — no HTTP headers per message
- Bidirectional — both sides initiate messages
- Low latency — no connection setup per message

### Cons
- Stateful and long-lived — harder to scale horizontally (sticky sessions or pub/sub layer needed)
- Load balancers need WebSocket support
- No built-in request/response correlation — must implement manually
- Reconnection logic must be handled by the client
- Proxies and firewalls sometimes block WebSocket connections

### Scaling WebSockets
Each WebSocket is a persistent TCP connection. One server can hold ~65k connections. Scaling requires:
1. Multiple servers with a shared pub/sub layer (Redis Pub/Sub, Kafka)
2. Clients connect to any server; messages broadcast through the shared layer

```
Client A → Server 1 ─┐
Client B → Server 2 ─┼→ Redis Pub/Sub ← message fan-out
Client C → Server 2 ─┘
```

### Use when
- Live chat and messaging
- Real-time collaboration (Google Docs-style)
- Live sports scores, stock prices, auction bidding
- Multiplayer games
- Live dashboards and monitoring
- Notifications (as a long-lived push channel)

---

## SSE — Server-Sent Events

**Transport:** HTTP  
**Format:** Text (UTF-8)  
**Pattern:** Server → Client only (one-directional push)  
**Connection:** Long-lived HTTP response

A simpler alternative to WebSockets when you only need server-to-client streaming.

```http
GET /events HTTP/1.1
Accept: text/event-stream

← Server streams:
data: {"type":"update","value":42}

data: {"type":"update","value":43}
```

**Pros:** Works over plain HTTP, native browser support, auto-reconnects  
**Cons:** One-directional only, max ~6 connections per browser (HTTP/1.1 limit)

**Use when:** Server pushes events to browser (notifications, live feeds) and the client doesn't need to send data back in the same channel.

---

## Protocol Comparison

| | REST | GraphQL | gRPC | WebSocket | SSE |
|---|---|---|---|---|---|
| Transport | HTTP | HTTP | HTTP/2 | TCP | HTTP |
| Format | JSON | JSON | Protobuf (binary) | Any | Text |
| Direction | Client→Server | Client→Server | Both | Both | Server→Client |
| Real-time | No (polling) | Subscriptions | Streaming | Yes | Yes |
| Browser support | Full | Full | Partial (gRPC-Web) | Full | Full |
| Performance | Good | Good | Excellent | Good | Good |
| Type safety | None (runtime) | Schema | Compile-time | None | None |
| Caching | Native HTTP | Manual | Not applicable | Not applicable | HTTP |
| Complexity | Low | Medium | High | Medium | Low |

---

## Decision Guide

| Scenario | Protocol |
|---|---|
| Public API, external developers | REST |
| Complex frontend with varied data needs | GraphQL |
| Internal microservice RPC | gRPC |
| High-throughput internal streaming | gRPC bidirectional |
| Live chat, multiplayer game | WebSocket |
| Live feed / notifications (server-only push) | SSE or WebSocket |
| Mobile app, bandwidth-constrained | gRPC |
| Simple CRUD app | REST |
| Aggregating multiple microservices for a frontend | GraphQL (BFF pattern) |