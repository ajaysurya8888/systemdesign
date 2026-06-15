# Hashing Methods

A hash function maps input data of arbitrary size to a fixed-size output (digest). Each algorithm differs in output size, speed, collision resistance, and intended use case.

---

## MD5 — Message Digest 5

- **Output size:** 128 bits (32 hex characters)
- **Speed:** Very fast
- **Security:** Cryptographically broken — collisions can be generated in seconds
- **Status:** Do NOT use for security purposes

```
MD5("hello") → 5d41402abc4b2a76b9719d911017c592
MD5("hello") → 5d41402abc4b2a76b9719d911017c592  (same)
MD5("helo")  → b1946ac92492d2347c6235b4d2611184  (completely different)
```

**Still used for:**
- Non-cryptographic checksums (verifying file transfers on trusted networks)
- Database indexing / deduplication where security is not a concern
- Legacy systems that haven't migrated

**Never use for:** Passwords, digital signatures, certificates, any security-sensitive purpose.

---

## SHA Family — Secure Hash Algorithm

Designed by NIST. SHA-1 is deprecated; SHA-2 and SHA-3 are the current standards.

### SHA-1
- **Output:** 160 bits (40 hex chars)
- **Status:** Deprecated — collision attacks demonstrated (SHAttered attack, 2017)
- **Legacy use:** Old TLS certificates, old Git commit hashes

### SHA-256 (SHA-2 family)
- **Output:** 256 bits (64 hex chars)
- **Status:** Current standard — widely used, no known practical attacks
- **Speed:** Moderate

```
SHA-256("hello") → 2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824
```

**Used for:**
- TLS certificates (HTTPS)
- Code signing
- Blockchain (Bitcoin uses SHA-256 for proof-of-work)
- File integrity verification (`sha256sum`)
- HMAC signatures (JWT, API request signing)

### SHA-512 (SHA-2 family)
- **Output:** 512 bits (128 hex chars)
- **Slower** than SHA-256 on 32-bit systems, **faster** on 64-bit systems
- Higher security margin, used when extra strength is required

### SHA-3 (Keccak)
- **Output:** 224, 256, 384, or 512 bits (configurable)
- **Completely different design** from SHA-2 (sponge construction)
- Designed as a backup if SHA-2 is ever broken
- Slower than SHA-2 in software but hardware-friendly
- Not yet widely deployed despite being standardized in 2015

---

## bcrypt

- **Output:** 60-character string (includes salt and cost factor)
- **Designed specifically for password hashing**
- **Intentionally slow** — adjustable cost factor makes brute-force expensive

```
bcrypt("password", cost=12) → $2b$12$eKxLjkQ8dq3sT7...  (includes salt + cost)
```

**Key features:**
- Built-in salting (prevents rainbow table attacks)
- Cost factor: increasing it doubles computation time (future-proof)
- Max 72 bytes input — longer passwords are truncated

**Use for:** Password storage. Do not use for file checksums or signatures.

**Cost recommendations:**
- Interactive login: cost 10-12 (≈100-300ms)
- High-security: cost 13-14 (≈500ms-1s)

---

## Argon2

- **Winner of the Password Hashing Competition (2015)**
- **Output:** Variable length
- Three variants: Argon2d (GPU-resistant), Argon2i (side-channel-resistant), Argon2id (recommended — both)
- Configurable: time cost, memory cost, parallelism

```
Argon2id(password, salt, time=3, memory=64MB, threads=4) → $argon2id$v=19$m=65536...
```

**Why it beats bcrypt:**
- Memory-hard: requires large RAM, not just CPU time — defeats GPU/ASIC attacks
- Modern, actively maintained standard

**Use for:** New systems requiring password hashing. Prefer over bcrypt for new projects.

---

## HMAC — Hash-based Message Authentication Code

Not a hash function itself — a construction that combines a hash function with a secret key to produce a **keyed hash** (MAC).

```
HMAC-SHA256(key, message) → signature
```

**Properties:**
- Only someone with the key can produce or verify the MAC
- Verifies both integrity and authenticity

**Used for:**
- JWT (JSON Web Token) signatures — `HMAC-SHA256(header.payload, secret)`
- API request signing (AWS Signature v4, Stripe webhook validation)
- Cookie integrity (signed cookies in Rails, Django, etc.)

---

## CRC32 — Cyclic Redundancy Check

- **Output:** 32 bits (8 hex chars)
- **Not a cryptographic hash** — fast, minimal collision resistance
- Detects accidental corruption, not malicious tampering

**Used for:**
- Network packet checksums (Ethernet frame CRC)
- ZIP/gzip file integrity
- Database page checksums

---

## Base64 — Not a Hash, Not Encryption

Base64 is **encoding**, not hashing. It converts binary data to ASCII-safe text. Fully reversible, no key.

```
"Hello, World!" → Base64 → SGVsbG8sIFdvcmxkIQ==
SGVsbG8sIFdvcmxkIQ== → decode → "Hello, World!"
```

**Output size:** ~33% larger than input (every 3 bytes → 4 characters).

**Variants:**
- Standard Base64: uses `+` and `/`, padded with `=`
- URL-safe Base64: uses `-` and `_` (safe for URLs and JWTs)
- Base64url: no padding (used in JWTs)

**Used for:**
- Encoding binary data in JSON, HTML, email (images, attachments)
- JWT structure (header and payload are Base64url encoded — not encrypted)
- Encoding credentials in HTTP Basic Auth (`Authorization: Basic base64(user:pass)`)

---

## Comparison Table

| Algorithm | Output Size | Speed | Cryptographic | Use Case |
|---|---|---|---|---|
| MD5 | 128 bit | Very fast | Broken | Legacy checksums only |
| SHA-1 | 160 bit | Fast | Deprecated | Legacy only |
| SHA-256 | 256 bit | Moderate | Strong | TLS, signing, integrity |
| SHA-512 | 512 bit | Fast (64-bit) | Strong | High-security hashing |
| SHA-3 | Variable | Moderate | Strong | Backup to SHA-2 |
| bcrypt | 60 char | Slow (tunable) | Strong | Password storage |
| Argon2id | Variable | Slow (tunable) | Strong | Password storage (preferred) |
| HMAC-SHA256 | 256 bit | Moderate | Strong | API signing, JWT |
| CRC32 | 32 bit | Very fast | No | Error detection only |
| Base64 | +33% input | Very fast | No (encoding) | Binary-to-text encoding |

---

## Quick Decision Guide

- **Store a password** → bcrypt or Argon2id
- **Verify a file download** → SHA-256
- **Sign an API request / JWT** → HMAC-SHA256
- **HTTPS certificate** → SHA-256
- **Represent binary as text** → Base64
- **Non-security checksum** → CRC32 or MD5
- **Never use MD5 or SHA-1 for anything security-related**