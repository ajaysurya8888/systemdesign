# B-Tree Indexing

A B-Tree (Balanced Tree) is the data structure behind most database indexes. Understanding it explains why indexed lookups are fast, when indexes help, and how a database finds a username/password among millions of records almost instantly.

---

## The Problem Without an Index

You have 10 million user records in a table. A login query:

```sql
SELECT * FROM users WHERE username = 'alice';
```

Without an index, the database does a **full table scan** — it reads every row until it finds 'alice'.

```
Row 1: bob      ← not alice, skip
Row 2: charlie  ← not alice, skip
Row 3: dave     ← not alice, skip
...
Row 4,782,341: alice  ← found!
```

At 10M rows, this is O(N) — millions of disk reads. Even with fast SSDs this is unacceptably slow for an online system.

---

## What a B-Tree Index Is

A B-Tree index is a separate data structure maintained by the database alongside your table. It stores the indexed column values in **sorted order** in a balanced tree, with each leaf node holding a pointer to the actual row on disk.

```
                    [M]
                 /       \
            [D, H]        [R, V]
           /  |   \      /  |   \
         [A-C][E-G][I-L][N-Q][S-U][W-Z]
           ↓    ↓    ↓    ↓    ↓    ↓
         rows  rows rows rows rows rows
```

**Key properties:**
- **Balanced:** Every leaf is the same depth — guaranteed O(log N) lookup
- **Sorted:** Values are in order — range queries are efficient
- **Fan-out:** Each node holds many keys (hundreds), so the tree is very shallow
- **Self-balancing:** Inserts and deletes automatically rebalance the tree

---

## B-Tree Structure

### Node anatomy

Each internal node holds `n` keys and `n+1` child pointers:

```
[ ptr | key1 | ptr | key2 | ptr | key3 | ptr ]
  ↓             ↓             ↓             ↓
values       values        values        values
< key1      key1–key2     key2–key3      > key3
```

### Order / Degree

A B-Tree of order `t` means:
- Every node holds between `t-1` and `2t-1` keys
- Every internal node has between `t` and `2t` children

With order 500 (common in databases), each node holds up to 999 keys. A tree 4 levels deep can index 500^4 ≈ 62.5 billion records.

---

## How a Lookup Works — Username Example

**Table:** 10 million users  
**Index:** B-Tree on `username`  
**Query:** `WHERE username = 'alice'`

**Step 1 — Start at root node (1 disk read)**
```
Root: [kara, mike, sam, zara]
'alice' < 'kara' → go left child
```

**Step 2 — Read internal node (1 disk read)**
```
Node: [alice, bob, charlie, dan]
'alice' == 'alice' → found in index
```

**Step 3 — Follow row pointer (1 disk read)**
```
Pointer → page 4,782,341 → read row → return user record
```

**Total: 3–4 disk reads** regardless of table size.

Compare to full scan: up to **10 million disk reads**.

---

## B-Tree vs B+ Tree

Most databases (PostgreSQL, MySQL InnoDB) actually use **B+ Trees**, a variant:

| | B-Tree | B+ Tree |
|---|---|---|
| Data storage | In every node | Only in leaf nodes |
| Internal nodes | Hold keys + data | Hold keys only (more keys per node) |
| Leaf nodes | Linked? | Doubly linked list |
| Range queries | Slower (traverse up/down) | Fast (scan leaves left to right) |
| Lookup | Slightly faster (may find in internal node) | Slightly slower (always reaches leaf) |

**B+ Tree leaf nodes form a linked list:**

```
[A-D] ↔ [E-H] ↔ [I-L] ↔ [M-P] ↔ [Q-T] ↔ [U-Z]
```

This makes range queries like `WHERE username BETWEEN 'alice' AND 'dave'` extremely efficient — find 'alice', then scan right along the linked list.

PostgreSQL, MySQL InnoDB, Oracle, and SQL Server all use B+ Trees internally.

---

## The Login Flow: Username + Password with 10 Million Records

### Schema

```sql
CREATE TABLE users (
    id       BIGINT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash CHAR(60) NOT NULL,   -- bcrypt hash
    email    VARCHAR(100)
);

-- Index is created automatically on UNIQUE columns
-- Explicit index: CREATE INDEX idx_username ON users(username);
```

### Query

```sql
SELECT id, password_hash FROM users WHERE username = 'alice';
```

### What happens internally

```
1. Query parser receives: WHERE username = 'alice'

2. Query planner checks: is there an index on username?
   → Yes: idx_username (B+ Tree)

3. B+ Tree traversal (3–4 node reads):
   Root → Internal node → Internal node → Leaf node
   Leaf node contains: ('alice', row_pointer → page 482, slot 7)

4. Fetch the row from data page 482, slot 7 (1 disk read)
   Returns: { id: 10023, password_hash: '$2b$12$eKx...' }

5. Application compares:
   bcrypt.verify('inputPassword', '$2b$12$eKx...') → true/false

6. Login succeeds or fails
```

**Total disk reads: 4–5** regardless of whether there are 10K or 100M users.

---

## Why Passwords Are Not Indexed

You should **never** index the `password_hash` column, and **never** query `WHERE password = ?`.

**Correct flow:**
1. `SELECT password_hash WHERE username = ?` — use the username index to find the row
2. `bcrypt.verify(inputPassword, storedHash)` — compare in application memory

**Why not `WHERE password_hash = ?`:**
- Would require storing plaintext or a fast (weak) hash — a security disaster
- bcrypt is intentionally slow and non-deterministic (salted) — the same password produces different hashes, so equality lookup is impossible
- Even if it were possible, it would expose a timing oracle

---

## Composite Index for Login

If you always query by username, a single-column index on `username` is optimal. But for a composite query:

```sql
SELECT * FROM users WHERE username = 'alice' AND active = true;
```

A **composite index** on `(username, active)` lets the B-Tree filter both conditions in one traversal:

```sql
CREATE INDEX idx_username_active ON users(username, active);
```

**Column order matters:** The index is sorted by `username` first, then `active`. It can be used for:
- `WHERE username = ?` — yes (leftmost prefix rule)
- `WHERE username = ? AND active = ?` — yes
- `WHERE active = ?` alone — no (cannot use index without leftmost column)

---

## Index Selectivity

An index is most useful when it has **high selectivity** — most values are unique.

| Column | Selectivity | Index useful? |
|---|---|---|
| `username` (unique) | 100% | Excellent |
| `email` (unique) | 100% | Excellent |
| `country` (200 countries, 10M rows) | Low | Weak — 50K rows per value on average |
| `active` (true/false) | Very low | Rarely useful |
| `created_at` (range queries) | High | Good for range scans |

For `WHERE active = true` on 9 million active users out of 10 million total, the database ignores the index and does a full scan — it's cheaper.

---

## When Indexes Slow Things Down

Indexes are not free:

| Operation | Cost |
|---|---|
| SELECT (indexed column) | Fast — B-Tree lookup |
| INSERT | Slow — must update index tree + rebalance |
| UPDATE (indexed column) | Slow — must delete old index entry, insert new one |
| DELETE | Slow — must remove index entry |
| Storage | Each index is ~10–30% of table size |

For a table with heavy writes and rare reads, indexes hurt more than they help.

---

## Other Index Types

| Type | Structure | Best for |
|---|---|---|
| B+ Tree | Sorted balanced tree | Equality, range, prefix queries |
| Hash Index | Hash table | Equality only (`=`), not ranges |
| GiST / R-Tree | Spatial tree | Geographic queries (`ST_Within`) |
| Full-Text | Inverted index | `LIKE '%word%'`, text search |
| Bitmap | Bit array per value | Low-cardinality columns (status, gender) |

PostgreSQL and MySQL default to B+ Tree for all `CREATE INDEX` statements unless you specify otherwise.

---

## Summary

| | Without Index | With B-Tree Index |
|---|---|---|
| Lookup complexity | O(N) | O(log N) |
| Disk reads (10M rows) | Up to 10 million | 4–5 |
| Range query | Full scan | Leaf list scan |
| Write overhead | None | Rebalance on insert/update/delete |
| Storage overhead | None | ~10–30% of table size |

For a login system with millions of users: always index `username` (or make it `UNIQUE`, which creates an index automatically). The query goes from scanning millions of rows to 4 disk reads — the difference between a 10-second login and a 1-millisecond login.