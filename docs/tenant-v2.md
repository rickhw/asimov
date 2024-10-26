

## State Machine

Source \ Target | START | PENDING | PROCESSING | ACTIVE | INACTIVE | TERMINATED
----------------|-------|---------|------------|--------|----------|-----------
START           |   x   |  v (1)  |     x      |    x   |   v (2)  |    v (3)
PENDING         |   x   |    x    |     p (4)  |  v (4) |   v (5)  |    v (6)
PROCESSING      |   x   |  p (7)  |     x      |  p (8) |   p (9)  |   p (10)
ACTIVE          |   x   |    x    |    p (11)  |    x   |   v (11)    |      x
INACTIVE        |   x   |    x    |    p (12)  |    v   |    x     |      v
TERMINATED      |   x   |    x    |    v (13)  |    x   |    x     |      x

- begin state: START
- end state: TERMINATED
- passing state: PENDING, PROCESSING

## Transition

每個轉換過程需要做什麼事情。

- START
    - (1) START->PENDING
    - (2) START->INACTIVE
    - (3) START->TERMINATED
- PENDING
    - (4) PENDING->PROCESSING->ACTIVE
        - send confirm email
        - waiting confirm email
        - resend confrm email
        - accept confirm email
        - reject confirm email
    - (5) PENDING->PROCESSING->INACTIVE
    - (6) PENDING->TERMINATED (Exception)
- PROCESSING (p)
    - (7) PROCESSING->PENDING
    - (8) PROCESSING->ACTIVE
    - (9) PROCESSING->INACTIVE
    - (10) PROCESSING->TERMINATED
- ACTIVE
    - (11) ACTIVE->PROCESSING->INACTIVE
- INACTIVE
    - (12) INACTIVE->PROCESSING->ACTIVE
    - (13) INACTIVE->PROCESSING->TERMINATED


## Register a tenant (Async)

request message:

```json
POST /api/tenants
Content-Type: application/json

{
  "tenantKey": "Tears-of-The-Kingdom",
  "displayName": "Tear of the Kingdom",
  "description": "王國之淚",
  "rootAccount": "rick@abc.com"
}
```

response:

```json
HTTP/1.1 202 Accepted

{
    "apiVersion": "v1alpha",
    "kind": "platform.Tenant",
    "metadata": {
        "_id": "uuid-1234-5678-91011",   // write once, read many
        "key": "Tears-of-The-Kingdom",
        "_state": "PENDING",        // state machine
        "_type": "record",      // resource, record, processing (cpu-bound)
        "_creationTime": "2024-10-10T13:41:58+00:00",
        "_lastModified": "2024-10-10T13:41:58+00:00"
    },
    "data": {
        "displayName": "Tear of the Kingdom",
        "description": "王國之淚",
        "rootAccount": "rick@abc.com",
    }
}
```

### get tenant status

```bash
GET {{hostname}}/tenants/Tears-of-The-Kingdom
```

response

```json
{
    "apiVersion": "v1alpha",
    "kind": "platform.Tenant",
    "metadata": {
        "_id": "uuid-1234-5678-91011",   // write once, read many
        "key": "Tears-of-The-Kingdom",
        "_state": "RUNNING",        // state machine
        "_type": "record",      // resource, record, processing (cpu-bound)
        "_creationTime": "2024-10-10T13:41:58+00:00",
        "_lastModified": "2024-10-10T13:41:58+00:00"
    },
    "data": {
        "displayName": "Tear of the Kingdom",
        "description": "王國之淚",
        "rootAccount": "rick@abc.com",
    }
}
```

