# REST Constraints

REST (Representational State Transfer) is an architectural style for designing networked applications, defined by Roy Fielding in his 2000 PhD dissertation. An API is truly RESTful only if it follows all six constraints.

---

## 1. Client-Server

The **client and server are separate** and communicate only through a defined interface (the API).

- Client handles the UI and user experience
- Server handles data storage and business logic
- Neither knows the internal implementation of the other

**Benefit:** Independent evolution. You can rewrite the frontend or backend without affecting the other, as long as the API contract stays the same.

---

## 2. Stateless

Every request from the client to the server must contain **all information needed** to process that request. The server stores no session state between requests.

**Example:** Instead of the server remembering "this user is logged in," the client sends an auth token with every request.

**Benefit:** Scalability — any server can handle any request. No sticky sessions needed. Easier horizontal scaling.

**Tradeoff:** Larger request payloads; client must manage state.

---

## 3. Cacheable

Server responses must explicitly mark themselves as **cacheable or non-cacheable** using HTTP headers (`Cache-Control`, `ETag`, `Last-Modified`).

**Example:** A product listing response can be cached for 60 seconds. A bank balance response should never be cached.

**Benefit:** Reduced load on servers, faster responses for clients, reduced bandwidth.

---

## 4. Uniform Interface

The API has a **consistent, standardized interface** across all resources. This is the core of REST and has four sub-constraints:

| Sub-constraint | Meaning |
|---|---|
| Resource identification | Resources are identified by URIs (`/users/123`) |
| Resource manipulation via representations | Client manipulates resources using JSON/XML representations |
| Self-descriptive messages | Each message includes enough info to describe how to process it (Content-Type header, HTTP method) |
| HATEOAS | Responses include links to related actions (Hypermedia as the Engine of Application State) |

**HATEOAS example:**
```json
{
  "orderId": 42,
  "status": "pending",
  "_links": {
    "cancel": { "href": "/orders/42/cancel" },
    "payment": { "href": "/orders/42/payment" }
  }
}
```

Most APIs implement the first three but skip HATEOAS.

---

## 5. Layered System

The client doesn't need to know if it's talking directly to the server or through **intermediaries** (load balancers, API gateways, caches, security layers).

**Example:** A request might pass through a CDN → API gateway → auth service → backend. The client sees none of this.

**Benefit:** Security, scalability, and flexibility to add/remove layers without changing the client.

---

## 6. Code on Demand (Optional)

Servers can optionally send **executable code** to clients (e.g., JavaScript). This is the only optional constraint.

**Example:** A server returning a JavaScript widget that the browser runs.

**Rarely used** in modern API design because it introduces coupling between client and server.

---

## HTTP Methods in REST

| Method | Action | Idempotent | Safe |
|---|---|---|---|
| GET | Read | Yes | Yes |
| POST | Create | No | No |
| PUT | Replace | Yes | No |
| PATCH | Partial update | No | No |
| DELETE | Delete | Yes | No |

**Idempotent** — calling it multiple times has the same effect as calling it once.  
**Safe** — has no side effects (read-only).

---

## REST vs RPC vs GraphQL

| | REST | gRPC | GraphQL |
|---|---|---|---|
| Protocol | HTTP/1.1 | HTTP/2 | HTTP |
| Format | JSON/XML | Protobuf (binary) | JSON |
| Flexibility | Fixed endpoints | Fixed methods | Client defines query |
| Performance | Good | Excellent | Good |
| Best for | Public APIs | Internal microservices | Complex, flexible queries |