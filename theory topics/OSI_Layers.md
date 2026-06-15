# OSI Layers — Open Systems Interconnection Model

The OSI model is a conceptual framework that standardizes how different network systems communicate. It divides network communication into 7 layers, each with a specific responsibility. Data flows **down** the layers on the sender side and **up** the layers on the receiver side.

---

## The 7 Layers

```
Sender                          Receiver
─────────────────────────────────────────
7. Application  ──────────────► Application
6. Presentation ──────────────► Presentation
5. Session      ──────────────► Session
4. Transport    ──────────────► Transport
3. Network      ──────────────► Network
2. Data Link    ──────────────► Data Link
1. Physical     ══════════════► Physical
                  (actual wire)
```

Each layer adds a **header** (encapsulation) going down, and strips it (decapsulation) going up.

---

## Layer 7 — Application

**What it does:** The layer closest to the user. Provides the interface for applications to access network services.

**Not the app itself** — it's the protocol the app uses to communicate.

**Protocols:** HTTP, HTTPS, FTP, SMTP, DNS, SSH, WebSocket, gRPC

**Real-world:** When your browser fetches `https://example.com`, it uses HTTP at Layer 7.

**Data unit:** Message / Data

---

## Layer 6 — Presentation

**What it does:** Translates data between the application layer and the network. Handles encoding, encryption, and compression.

**Responsibilities:**
- Character encoding: ASCII ↔ UTF-8 ↔ Unicode
- Encryption/Decryption: TLS/SSL operates here
- Compression: gzip, deflate

**Real-world:** When HTTPS encrypts your data before sending, that's Layer 6. When the receiver decrypts it, also Layer 6.

**Data unit:** Data

**Note:** In practice, Layer 6 and 7 are often merged in modern protocol stacks. TLS is sometimes placed here, sometimes at Layer 7.

---

## Layer 5 — Session

**What it does:** Manages sessions (connections) between applications. Establishes, maintains, and terminates communication sessions.

**Responsibilities:**
- Session establishment, maintenance, termination
- Synchronization (checkpoints for long transfers — resume after failure)
- Authentication and authorization at the session level

**Protocols:** NetBIOS, RPC, PPTP, SIP (VoIP session management)

**Real-world:** When you log into a website, the session layer manages that conversation state. A video call uses SIP to set up the session.

**Data unit:** Data

**Note:** Like Layer 6, this is often handled by application-layer protocols in practice.

---

## Layer 4 — Transport

**What it does:** Responsible for **end-to-end communication** between processes. Handles segmentation, flow control, error detection, and reassembly.

**Two main protocols:**

### TCP — Transmission Control Protocol
- **Connection-oriented:** 3-way handshake before data flows
- **Reliable:** Guarantees delivery, ordering, error checking
- **Flow control:** Receiver controls how fast sender sends (sliding window)
- **Congestion control:** Backs off when network is congested
- **Use when:** Data must arrive complete and in order (HTTP, file transfer, email)

### UDP — User Datagram Protocol
- **Connectionless:** No handshake, fire-and-forget
- **Unreliable:** No guarantee of delivery or order
- **Fast:** No overhead of acknowledgment
- **Use when:** Speed matters more than reliability (video streaming, gaming, DNS, VoIP)

**Ports live here:** Port 80 (HTTP), 443 (HTTPS), 22 (SSH), 3306 (MySQL)

**Data unit:** Segment (TCP) / Datagram (UDP)

---

## Layer 3 — Network

**What it does:** Handles **logical addressing** and **routing** — finding the best path to send data across multiple networks.

**Responsibilities:**
- Logical addressing: IP addresses (IPv4, IPv6)
- Routing: Deciding the path from source to destination across routers
- Fragmentation: Breaking large packets into smaller ones if needed

**Protocols:** IP (IPv4, IPv6), ICMP (ping), OSPF, BGP, ARP

**Devices:** Routers operate at Layer 3

**Real-world:** When you send a packet from Mumbai to New York, routers at Layer 3 make hop-by-hop forwarding decisions using the IP address.

**Data unit:** Packet

---

## Layer 2 — Data Link

**What it does:** Handles **physical addressing** (MAC addresses) and reliable transfer between **directly connected** nodes (hop to hop, not end to end).

**Responsibilities:**
- Framing: Encapsulates packets into frames
- MAC (Media Access Control) addressing: Identifies devices on a local network
- Error detection: CRC checksums per frame
- Flow control between adjacent nodes

**Sub-layers:**
- **LLC (Logical Link Control):** Error checking, flow control
- **MAC (Media Access Control):** Physical addressing, access to medium

**Protocols:** Ethernet, Wi-Fi (802.11), PPP, ARP (sometimes placed here)

**Devices:** Switches, Network Interface Cards (NICs) operate at Layer 2

**Data unit:** Frame

---

## Layer 1 — Physical

**What it does:** Transmits raw **bits** over a physical medium. Defines electrical, optical, or radio signals.

**Responsibilities:**
- Bit representation: How 0s and 1s are encoded as voltage, light, or radio waves
- Physical topology: Bus, star, ring
- Transmission medium: Copper wire, fiber optic, wireless

**Standards:** Ethernet cables (Cat5e, Cat6), fiber optic (802.3), Bluetooth, USB

**Devices:** Hubs, repeaters, cables, network adapters (physical layer)

**Data unit:** Bit

---

## Data Encapsulation Flow

When you send an HTTP request, data travels down the OSI stack with headers added at each layer:

```
Application   →  [HTTP Data]
Transport     →  [TCP Header | HTTP Data]
Network       →  [IP Header  | TCP Header | HTTP Data]
Data Link     →  [Frame Header | IP Header | TCP Header | HTTP Data | Frame Trailer]
Physical      →  101010110001...  (raw bits on wire)
```

On the receiving end, each layer strips its header and passes the payload up.

---

## TCP/IP Model vs OSI Model

In practice, the TCP/IP model (4 layers) is what the internet actually uses. OSI is a reference model for understanding.

| OSI Layer | TCP/IP Layer | Protocols |
|---|---|---|
| 7. Application | Application | HTTP, DNS, FTP, SMTP, SSH |
| 6. Presentation | Application | TLS, encoding |
| 5. Session | Application | TLS handshake, RPC |
| 4. Transport | Transport | TCP, UDP |
| 3. Network | Internet | IP, ICMP, BGP |
| 2. Data Link | Network Access | Ethernet, Wi-Fi |
| 1. Physical | Network Access | Cables, radio |

---

## Where Common Technologies Live

| Technology | OSI Layer | Why |
|---|---|---|
| HTTP/HTTPS | 7 | Application protocol |
| TLS/SSL | 6 (or 7) | Encryption layer |
| TCP | 4 | Transport, reliability |
| UDP | 4 | Transport, fast |
| IP | 3 | Logical addressing, routing |
| DNS | 7 (uses UDP at 4) | Application-level name resolution |
| Ping (ICMP) | 3 | Network-layer diagnostic |
| Ethernet | 2 | Local network framing |
| Wi-Fi (802.11) | 1 & 2 | Physical + data link |
| Switch | 2 | Forwards frames by MAC |
| Router | 3 | Forwards packets by IP |
| Load balancer (L4) | 4 | Routes by TCP port |
| Load balancer (L7) | 7 | Routes by HTTP headers/URL |
| Firewall | 3–7 | Filters at various layers |
| VPN | 3 | Tunnels IP packets |
| WebSocket | 7 | Application over TCP |

---

## Memory Aid

**"Please Do Not Throw Sausage Pizza Away"** (bottom to top: Physical, Data Link, Network, Transport, Session, Presentation, Application)

Or top to bottom: **"All People Seem To Need Data Processing"**