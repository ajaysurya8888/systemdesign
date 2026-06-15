# Encryption Methods

Encryption transforms plaintext into ciphertext using a key, so that only authorized parties with the correct key can recover the original data. There are two main categories: symmetric (one key) and asymmetric (key pair).

---

## Symmetric Encryption

The **same key** is used to both encrypt and decrypt. Fast, efficient for bulk data.

**Challenge:** How do you securely share the key with the other party?

---

### AES — Advanced Encryption Standard

- **Type:** Symmetric block cipher
- **Key sizes:** 128, 192, or 256 bits
- **Block size:** 128 bits (16 bytes)
- **Status:** Current gold standard — used everywhere
- **Speed:** Very fast, hardware-accelerated on modern CPUs (AES-NI instruction)

```
Plaintext + Key (128/192/256 bit) → AES → Ciphertext
Ciphertext + Same Key             → AES → Plaintext
```

**AES Modes of Operation** (how blocks are chained):

| Mode | Full Name | Properties |
|---|---|---|
| ECB | Electronic Codebook | DO NOT USE — identical plaintext blocks → identical ciphertext |
| CBC | Cipher Block Chaining | XORs each block with previous; needs IV; not parallelizable |
| CTR | Counter | Converts block cipher to stream cipher; parallelizable |
| GCM | Galois/Counter Mode | CTR + authentication tag (AEAD); most recommended |

**AES-GCM** is the recommended mode — it provides both encryption and integrity verification (authenticated encryption).

**Used in:**
- HTTPS/TLS (AES-128-GCM, AES-256-GCM)
- File encryption (VeraCrypt, BitLocker)
- Database encryption at rest
- AWS, GCP encryption of stored data
- WPA2/WPA3 Wi-Fi encryption

---

### DES — Data Encryption Standard

- **Type:** Symmetric block cipher
- **Key size:** 56 bits (effectively)
- **Block size:** 64 bits
- **Status:** Completely broken — 56-bit key brute-forceable in hours

DES was the US federal standard from 1977 to the late 1990s. A 56-bit key space has only 2^56 ≈ 72 quadrillion possible keys — brute-forced by specialized hardware in 1998 (22 hours).

**Do not use DES for anything.**

### 3DES (Triple DES)

DES applied three times with different keys — effectively 112-bit security.

- **Status:** Deprecated (NIST deprecated in 2017, disallowed from 2024)
- **Speed:** 3× slower than DES, far slower than AES
- **Vulnerable to** Sweet32 birthday attack on 64-bit block size

Legacy systems (old payment terminals, mainframes) may still use 3DES. Migrate to AES.

---

## Asymmetric Encryption

Uses a **key pair**: a public key (shareable) and a private key (secret). What one encrypts, only the other can decrypt.

**Solves the key distribution problem** — you can share your public key openly.

```
Alice's public key  → encrypts message  → ciphertext
Alice's private key → decrypts ciphertext → plaintext
```

**Tradeoff:** 100-1000× slower than symmetric encryption. Used for small data or key exchange, not bulk data.

---

### RSA — Rivest–Shamir–Adleman

- **Type:** Asymmetric
- **Key sizes:** 1024 (broken), 2048 (minimum), 3072, 4096 bits
- **Security based on:** Difficulty of factoring large prime numbers
- **Status:** Still widely used, though being displaced by ECC for new systems

**Two uses:**

1. **Encryption:** Encrypt with public key, decrypt with private key
   - Used to encrypt small data (session keys, symmetric keys)
   - Max data size ≈ key size − padding overhead

2. **Digital Signatures:** Sign with private key, verify with public key
   - Proves authenticity and integrity
   - Used in TLS certificates, code signing, SSH

```
# Encrypt a message for Alice
ciphertext = RSA_encrypt(message, alice_public_key)

# Alice decrypts it
plaintext = RSA_decrypt(ciphertext, alice_private_key)

# Sign a document (proving it came from Alice)
signature = RSA_sign(hash(document), alice_private_key)
verify     = RSA_verify(signature, alice_public_key)  → true/false
```

**Used in:**
- TLS/HTTPS handshake (key exchange)
- SSH authentication
- SSL certificates
- PGP/GPG email encryption

**Key size recommendations:**
- 2048 bit: minimum acceptable today
- 3072 bit: recommended for new systems
- 4096 bit: high security, slower
- RSA-1024: broken, do not use

---

### ECC — Elliptic Curve Cryptography

- **Type:** Asymmetric
- **Key sizes:** 256 bits provides ~equivalent security to RSA-3072
- **Security based on:** Difficulty of the elliptic curve discrete logarithm problem
- **Speed:** Significantly faster than RSA at equivalent security levels
- **Status:** Preferred for new systems

**Common curves:**
| Curve | Bits | Used in |
|---|---|---|
| P-256 (secp256r1) | 256 | TLS, HTTPS, most modern systems |
| P-384 | 384 | High-security government use |
| Curve25519 | 255 | Modern TLS, Signal, WireGuard |
| secp256k1 | 256 | Bitcoin |

**ECDH (Elliptic Curve Diffie-Hellman):** Used for key exchange in TLS.  
**ECDSA (Elliptic Curve Digital Signature Algorithm):** Used for digital signatures.

**Why ECC over RSA:**
- Shorter keys (256-bit ECC ≈ 3072-bit RSA security)
- Faster operations — critical for mobile devices and embedded systems
- Less CPU/battery usage

---

## Hybrid Encryption (How TLS Actually Works)

In practice, asymmetric encryption is used to exchange a symmetric key, then symmetric encryption handles the bulk data.

```
1. Client gets server's public key (from TLS certificate)
2. Client generates a random session key
3. Client encrypts session key with server's public key → sends it
4. Server decrypts session key with its private key
5. Both parties now share the session key
6. All further communication uses AES with that session key
```

This combines:
- RSA/ECC: secure key exchange (slow, but only for small data)
- AES: fast bulk data encryption

---

## SHA in the Context of Encryption

SHA (Secure Hash Algorithm) is a **hashing function**, not encryption. However it plays a critical role in encryption systems:

- **Digital signatures:** `sign(SHA-256(document), private_key)` — you sign the hash, not the full document
- **TLS:** Certificate fingerprints are SHA-256 hashes of the certificate
- **HMAC:** `HMAC-SHA256(key, message)` — keyed hash for message authentication
- **Key derivation:** PBKDF2, HKDF use SHA internally to derive keys from passwords

SHA does not encrypt data — it fingerprints it. The confusion arises because SHA appears alongside encryption in security protocols.

---

## Comparison Table

| Algorithm | Type | Key Size | Speed | Status | Primary Use |
|---|---|---|---|---|---|
| AES-128-GCM | Symmetric | 128 bit | Very fast | Current standard | Bulk encryption, TLS |
| AES-256-GCM | Symmetric | 256 bit | Very fast | Current standard | High-security encryption |
| DES | Symmetric | 56 bit | Moderate | Broken | Do not use |
| 3DES | Symmetric | 112 bit | Slow | Deprecated | Legacy only |
| RSA-2048 | Asymmetric | 2048 bit | Slow | Acceptable | Key exchange, signatures |
| RSA-4096 | Asymmetric | 4096 bit | Slower | Strong | High-security signatures |
| ECC P-256 | Asymmetric | 256 bit | Fast | Current standard | TLS, modern key exchange |
| Curve25519 | Asymmetric | 255 bit | Very fast | Preferred | Modern TLS, Signal, WireGuard |

---

## Quick Decision Guide

| Scenario | Algorithm |
|---|---|
| Encrypt data at rest (files, DB) | AES-256-GCM |
| Encrypt data in transit | TLS 1.3 (uses AES-GCM + ECDH) |
| Key exchange / public-key encryption | ECC (Curve25519 or P-256) |
| Digital signatures | ECDSA or RSA-3072+ |
| API / JWT authentication | HMAC-SHA256 (symmetric) or RS256/ES256 (asymmetric) |
| Password storage | bcrypt / Argon2id (not encryption — use hashing) |
| Legacy system integration | 3DES (only if forced; migrate ASAP) |
| Never use | DES, RSA-1024, ECB mode |