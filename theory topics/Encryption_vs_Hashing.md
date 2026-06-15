# Encryption vs Hashing

Both transform data into an unreadable form, but they serve fundamentally different purposes and have different properties.

---

## Core Difference

| | Hashing | Encryption |
|---|---|---|
| Reversible? | No (one-way) | Yes (two-way) |
| Key required? | No | Yes |
| Output size | Fixed | Variable (≈ input size) |
| Purpose | Integrity / fingerprinting | Confidentiality |
| Same input → same output? | Always | Yes (same key) |

---

## Hashing

A hash function takes input of any size and produces a **fixed-size digest**. There is no way to reverse it — you cannot get the original input back from the hash.

```
"password123"  → SHA-256 → ef92b778bafe771e...  (256 bits)
"password124"  → SHA-256 → a3f39ba1c4d2e876...  (256 bits, completely different)
```

**Use cases:**
- Storing passwords (store the hash, never the plain text)
- Verifying file integrity (check hash before and after download)
- Data deduplication (same hash = same content)
- Digital signatures (hash the document, sign the hash)
- Checksums in databases and caches (ETags)

**Properties of a good hash function:**
- **Deterministic** — same input always gives same output
- **Fast to compute**
- **Avalanche effect** — small input change → completely different hash
- **Pre-image resistant** — cannot reverse hash to get input
- **Collision resistant** — hard to find two inputs with same hash

---

## Encryption

Encryption transforms data using a **key** so that it can be **decrypted back** to the original using the correct key.

```
"secret message" + key → AES → Xk39Bq... (ciphertext)
Xk39Bq...         + key → AES → "secret message" (plaintext)
```

**Two types:**
- **Symmetric:** Same key encrypts and decrypts (AES, DES)
- **Asymmetric:** Public key encrypts, private key decrypts (RSA)

**Use cases:**
- Securing data in transit (HTTPS/TLS)
- Storing sensitive data at rest (database encryption)
- Secure communication between services
- End-to-end encrypted messaging

---

## Encoding (Not the Same as Either)

Encoding (e.g., Base64) is often confused with encryption/hashing. It is **neither** — it's just a format transformation with no security.

```
"Hello" → Base64 → SGVsbG8=  (easily reversed, no key needed)
```

Base64 is used to represent binary data as ASCII text (email attachments, JWTs, image data in HTML). It provides **zero security**.

---

## Passwords: Why Hashing and Not Encryption?

**Wrong approach — store encrypted password:**
- If the encryption key is stolen, all passwords are recoverable
- The key must be stored somewhere — it's a liability

**Right approach — store hashed password:**
- Store `hash(password + salt)`
- At login, hash the input and compare — if equal, password is correct
- Even if the database is stolen, attacker has hashes, not passwords
- Cannot recover original password — only brute-force/rainbow table attacks possible

**Salting:** A random value added to the password before hashing, unique per user. Defeats rainbow table attacks.

```
salt = random_bytes(16)
stored = bcrypt(password + salt)
```

---

## Choosing the Right Tool

| Scenario | Use |
|---|---|
| Storing passwords | Hashing (bcrypt, Argon2) |
| Verifying file integrity | Hashing (SHA-256) |
| Sending sensitive data over the network | Encryption (TLS/AES) |
| Storing a credit card number | Encryption (AES — must retrieve original) |
| Deduplication / content addressing | Hashing (SHA-256) |
| Sending binary data over email/HTTP | Encoding (Base64) |
| Digital signatures | Both — hash the data, encrypt the hash with private key |

---

## Summary

- **Hashing** = one-way fingerprint. Use to verify, deduplicate, store passwords.
- **Encryption** = two-way lock. Use to protect data that must be retrieved later.
- **Encoding** = format change. Use to represent data in a different format. No security.