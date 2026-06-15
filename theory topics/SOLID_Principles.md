# SOLID Principles

SOLID is a set of five object-oriented design principles introduced by Robert C. Martin (Uncle Bob). They guide you toward code that is easy to maintain, extend, and understand.

---

## S — Single Responsibility Principle (SRP)

> A class should have **only one reason to change**.

Each class/module should do one thing and do it well. If a class handles both business logic and database persistence, it has two reasons to change — split it.

**Bad:**
```java
class UserService {
    void createUser(User u) { /* business logic */ }
    void saveToDatabase(User u) { /* DB logic */ }
    void sendWelcomeEmail(User u) { /* email logic */ }
}
```

**Good:**
```java
class UserService    { void createUser(User u) { ... } }
class UserRepository { void save(User u) { ... } }
class EmailService   { void sendWelcome(User u) { ... } }
```

**Why it matters:** When requirements change (e.g., switch from MySQL to MongoDB), only `UserRepository` changes — not `UserService`.

---

## O — Open/Closed Principle (OCP)

> Classes should be **open for extension, closed for modification**.

Add new behavior by extending, not by editing existing code. Editing existing code risks breaking things that already work.

**Bad:** Adding a new payment type requires modifying `PaymentProcessor`.

**Good:**
```java
interface PaymentStrategy { void pay(int amount); }

class CreditCardPayment implements PaymentStrategy { ... }
class UPIPayment implements PaymentStrategy { ... }
class CryptoPayment implements PaymentStrategy { ... }  // new type, no existing code changed

class PaymentProcessor {
    void process(PaymentStrategy strategy, int amount) {
        strategy.pay(amount);
    }
}
```

---

## L — Liskov Substitution Principle (LSP)

> Subtypes must be **substitutable** for their base types without altering correctness.

If `S` is a subtype of `T`, you should be able to replace `T` with `S` everywhere without breaking the program.

**Classic violation — Square extends Rectangle:**
```java
class Rectangle {
    void setWidth(int w)  { this.width = w; }
    void setHeight(int h) { this.height = h; }
    int area() { return width * height; }
}

class Square extends Rectangle {
    void setWidth(int w)  { this.width = w; this.height = w; }  // breaks LSP
    void setHeight(int h) { this.width = h; this.height = h; }  // breaks LSP
}
```

A `Square` is not substitutable for `Rectangle` — setting width and height independently breaks a square. LSP says this inheritance is wrong; use composition instead.

---

## I — Interface Segregation Principle (ISP)

> Clients should not be forced to **depend on interfaces they don't use**.

Prefer many small, specific interfaces over one large general-purpose interface.

**Bad:**
```java
interface Animal {
    void eat();
    void fly();   // not all animals fly
    void swim();  // not all animals swim
}
```

**Good:**
```java
interface Eatable { void eat(); }
interface Flyable { void fly(); }
interface Swimmable { void swim(); }

class Duck implements Eatable, Flyable, Swimmable { ... }
class Dog  implements Eatable, Swimmable { ... }
```

---

## D — Dependency Inversion Principle (DIP)

> High-level modules should not depend on low-level modules. Both should depend on **abstractions**.

Depend on interfaces, not concrete implementations. This makes swapping implementations easy.

**Bad:**
```java
class OrderService {
    MySQLDatabase db = new MySQLDatabase();  // tightly coupled
    void placeOrder(Order o) { db.save(o); }
}
```

**Good:**
```java
interface Database { void save(Order o); }

class OrderService {
    Database db;
    OrderService(Database db) { this.db = db; }  // injected dependency
    void placeOrder(Order o) { db.save(o); }
}

// Can now swap MySQL for MongoDB, in-memory, or mock — no OrderService changes
```

---

## Summary

| Principle | Core Idea | Violation Symptom |
|---|---|---|
| SRP | One reason to change | God classes, bloated services |
| OCP | Extend, don't modify | if/switch chains for new types |
| LSP | Subtypes are substitutable | Overriding methods that break parent behavior |
| ISP | Small, focused interfaces | Classes implementing methods they don't need |
| DIP | Depend on abstractions | `new ConcreteClass()` inside business logic |

SOLID principles are guidelines, not laws. Apply them where the complexity justifies it — over-engineering simple code is also a problem.