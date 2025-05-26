

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
    "id": "88386aab-11a5-4d24-9a4b-13679087734f",
    "apiVersion": "v1alpha",  
    "kind": "platform.Task",
    "metadata": {  
        "_state": "PENDING",  
        "_type": "processing",
        "_creationTime": "2024-10-10T13:41:58+00:00"  
    },
    "data": {
        "tenantKey": "Tears-of-The-Kingdom",
        "displayName": "Tear of the Kingdom",
        "description": "王國之淚",
        "rootAccount": "rick@abc.com"
    }
}
```

get the stats from task api.

```bash
GET {{hostname}}/tasks/1e37839e-f81c-44e6-acbf-1efc8633acdf
```

response:

```json
HTTP/1.1 200

{
    "id": "88386aab-11a5-4d24-9a4b-13679087734f",
    "apiVersion": "v1alpha",  
    "kind": "platform.Task",
    "metadata": {  
        "_state": "RUNNING",  
        "_type": "processing",
        "_creationTime": "2024-10-10T13:41:58+00:00"  
    },
    "data": {
        {
            "apiVersion": "v1alpha",
            "kind": "platform.Tenant",
            "id": "uuid-1234-5678-91011",   // write once, read many
            "key": "Tears-of-The-Kingdom",
            "metadata": {        
                "_state": "RUNNING",        // state machine
                "_type": "record",      // resource, record, processing (cpu-bound)
                "_creationTime": "2024-10-10T13:41:58+00:00",
                "_lastModified": "2024-10-10T13:41:58+00:00" 
            },
            "data": {
                "message": "Hello, Master Asimov",
                "displayName": "Tear of the Kingdom",
                "description": "王國之淚",
                "rootAccount": "rick@abc.com",
            }
        }
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
    "id": "uuid-1234-5678-91011",   // write once, read many
    //"key": "system-item",           // write once, read many (optional)
    "key": "Tears-of-The-Kingdom",
    "metadata": {        
        "_state": "RUNNING",        // state machine
        "_type": "record",      // resource, record, processing (cpu-bound)
        "_creationTime": "2024-10-10T13:41:58+00:00",
        "_lastModified": "2024-10-10T13:41:58+00:00" 
    },
    "data": {
        "message": "Hello, Master Asimov",
        "displayName": "Tear of the Kingdom",
        "description": "王國之淚",
        "rootAccount": "rick@abc.com",
    }
}
```


