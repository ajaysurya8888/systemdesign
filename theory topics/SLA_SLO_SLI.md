# SLA, SLO, and SLI

These three terms form the reliability measurement framework used by SRE (Site Reliability Engineering) teams, popularized by Google's SRE book.

---

## SLI — Service Level Indicator

An **SLI is a metric** — a specific, measurable property of the service's behavior.

It answers: *"What are we measuring?"*

**Common SLIs:**
| Category | SLI Example |
|---|---|
| Availability | % of requests that returned a non-5xx response |
| Latency | % of requests completed in under 200ms |
| Throughput | Requests per second processed |
| Error rate | % of requests that returned an error |
| Durability | % of stored data that can be retrieved |
| Saturation | CPU/memory utilization % |

**Example SLI:**  
`(successful_requests / total_requests) * 100` = 99.95%

---

## SLO — Service Level Objective

An **SLO is an internal target** — the threshold you aim to meet for your SLI.

It answers: *"What's our goal?"*

SLOs are set internally by the engineering team. They are not promises to customers — they're internal targets to ensure the service is reliable enough.

**Example SLOs:**
- 99.9% of requests return a 2xx response over a rolling 30-day window
- 95% of requests complete in under 300ms
- Monthly availability ≥ 99.95%

**Error Budget:**  
`Error Budget = 1 - SLO`

If SLO = 99.9%, error budget = 0.1% of requests can fail. Once the budget is exhausted, reliability work takes priority over feature work.

```
Monthly error budget for 99.9% SLO:
  30 days × 24h × 60min = 43,200 minutes
  0.1% of 43,200 = 43.2 minutes of downtime allowed
```

---

## SLA — Service Level Agreement

An **SLA is a contractual promise** to customers — a legal/business commitment with consequences (refunds, credits, penalties) if violated.

It answers: *"What do we promise customers?"*

SLAs are always set **looser than SLOs** — if your SLO is 99.9%, your SLA might be 99.5%. This buffer protects you from SLA violations during unexpected incidents.

**Example SLAs:**
- AWS EC2: 99.99% monthly uptime (violation = service credits)
- Google Cloud: 99.9% availability (violation = up to 50% credit)
- Stripe: SLA not public, but service credits offered for downtime

---

## The Relationship

```
SLI (what we measure)
  ↓
SLO (internal target we set for the SLI)
  ↓
SLA (external promise, looser than SLO, with business consequences)
```

| | SLI | SLO | SLA |
|---|---|---|---|
| What it is | Metric | Target | Contract |
| Who sets it | Engineering | Engineering | Business/Legal |
| Audience | Internal | Internal | Customers |
| Consequence of breach | Alert/incident | Reliability work | Refunds/penalties |

---

## Error Budgets and Engineering Culture

Error budgets create a healthy tension:
- **Within budget** → team can ship features, take risks
- **Budget exhausted** → team must focus on reliability, no new features

This shifts reliability from "ops problem" to shared engineering responsibility.

**Example workflow:**
1. SLO: 99.9% availability (error budget: 43 min/month)
2. Incident consumes 30 minutes of budget
3. Team has 13 minutes remaining
4. Decision: delay risky deployment until next month's budget resets

---

## Choosing Good SLOs

- Set SLOs based on **user happiness**, not server metrics
- Avoid 100% SLOs — perfection is impossible and kills innovation
- Start with observed baselines, then tighten over time
- Review SLOs quarterly — requirements change

**Good:** "99% of checkout requests succeed within 1 second"  
**Bad:** "CPU utilization < 80%" (doesn't map to user experience)