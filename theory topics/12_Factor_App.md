# The 12-Factor App

The 12-Factor App is a methodology for building software-as-a-service applications that are scalable, maintainable, and portable. Developed by engineers at Heroku based on patterns observed across thousands of deployments.

---

## Factor 1 — Codebase

> One codebase tracked in version control, many deploys.

- One app = one repository
- Multiple deploys (staging, production) from the same codebase, different configs
- Multiple apps sharing code → extract shared code into libraries (dependencies)

---

## Factor 2 — Dependencies

> Explicitly declare and isolate dependencies.

Never rely on system-wide installed packages. Declare all dependencies in a manifest file and use isolation to ensure no implicit dependencies leak in.

**Examples:**
- Python: `requirements.txt` + `venv`
- Node.js: `package.json` + `node_modules`
- Java: `pom.xml` (Maven) or `build.gradle`

---

## Factor 3 — Config

> Store config in the environment, not in code.

Config = anything that varies between deploys (database URLs, API keys, port numbers). This should **never be in source code**.

**Wrong:** `db_url = "mysql://prod-server/mydb"` hardcoded in code  
**Right:** `db_url = os.environ["DATABASE_URL"]`

Litmus test: Could you open-source the codebase right now without exposing credentials? If yes, config is properly externalized.

---

## Factor 4 — Backing Services

> Treat backing services as attached resources.

A backing service is anything the app consumes over the network: databases, caches, message queues, email services, payment APIs.

Treat local and third-party services identically — both accessed via URL/config. You should be able to swap a local MySQL for AWS RDS by changing a config value, with no code change.

---

## Factor 5 — Build, Release, Run

> Strictly separate build and run stages.

| Stage | Description |
|---|---|
| Build | Convert code + dependencies into an executable bundle |
| Release | Combine the build with environment config |
| Run | Execute the release in the runtime environment |

Every release should have a unique ID (timestamp or version). Releases are immutable — if something breaks, you roll back to a previous release, not patch the current one.

---

## Factor 6 — Processes

> Execute the app as one or more stateless processes.

Processes are stateless and share-nothing. Any data that needs to persist goes into a backing service (database, cache).

**Never store in local memory or filesystem between requests:**
- User sessions (use Redis/Memcached)
- Uploaded files (use S3/GCS)
- Compiled assets (serve from CDN)

This is the foundation of horizontal scalability — any process can handle any request.

---

## Factor 7 — Port Binding

> Export services via port binding.

The app is self-contained and exports HTTP as a service by binding to a port. It does not rely on a web server (Apache, Nginx) being injected at runtime.

**Example:** A Python app runs its own HTTP server (`gunicorn`) and listens on `$PORT`. The routing layer sends traffic to it.

---

## Factor 8 — Concurrency

> Scale out via the process model.

Scale by adding more processes (horizontal scaling), not by making processes larger (vertical scaling).

Different types of work can run in different process types:
- Web processes handle HTTP requests
- Worker processes handle background jobs
- Clock processes run scheduled tasks

Each process type can scale independently.

---

## Factor 9 — Disposability

> Maximize robustness with fast startup and graceful shutdown.

- **Fast startup:** Processes should start within seconds. Enables rapid scaling and deploys.
- **Graceful shutdown:** On SIGTERM, stop accepting new requests, finish in-flight requests, then exit cleanly.
- **Crash-safe:** Sudden crashes should be safe. Use crash-only design — return jobs to the queue, use transactions.

---

## Factor 10 — Dev/Prod Parity

> Keep development, staging, and production as similar as possible.

**Three gaps to close:**

| Gap | Problem | Solution |
|---|---|---|
| Time gap | Code sits in dev for weeks before deploying | Continuous deployment |
| Personnel gap | Devs write code, ops deploys it | Dev deploys their own code (DevOps) |
| Tools gap | SQLite in dev, PostgreSQL in prod | Same backing services everywhere |

The "it works on my machine" problem is a dev/prod parity failure.

---

## Factor 11 — Logs

> Treat logs as event streams.

An app should never write to log files or manage log routing. Instead, write all log output to `stdout` as an unbuffered event stream.

The execution environment captures that stream and routes it to wherever makes sense:
- Terminal output in development
- Log aggregation services in production (Datadog, Splunk, CloudWatch)

The app has no knowledge of or concern with log storage.

---

## Factor 12 — Admin Processes

> Run admin/management tasks as one-off processes.

Database migrations, console sessions, one-time scripts — these should run in an identical environment to the regular app processes, against the same release.

```bash
# One-off admin process
heroku run python manage.py migrate
```

These run in the same codebase and config as the app — not ad-hoc scripts on production servers.

---

## Summary

| # | Factor | One-Line Rule |
|---|---|---|
| 1 | Codebase | One repo, many deploys |
| 2 | Dependencies | Declare everything explicitly |
| 3 | Config | Config in environment, not code |
| 4 | Backing Services | Treat all external services as attached resources |
| 5 | Build/Release/Run | Separate and immutable stages |
| 6 | Processes | Stateless, share-nothing |
| 7 | Port Binding | App serves itself, no web server required |
| 8 | Concurrency | Scale horizontally with processes |
| 9 | Disposability | Fast start, graceful stop |
| 10 | Dev/Prod Parity | Eliminate environment differences |
| 11 | Logs | stdout event stream, no file management |
| 12 | Admin Processes | Run as one-off processes in same environment |