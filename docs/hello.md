
request message:

```json
POST /api/hello
Content-Type: application/json
X-Request-Mode: async

{
  "message": "Hello, Asimov"
}
```

response:

```json
HTTP/1.1 202

{
  "id": "88386aab-11a5-4d24-9a4b-13679087734f",
  "metadata": {
    "_kind": "platform.SayHello",
    "_state": "pending",
    "_type": "processing",
    "_creationTime": "2024-10-10T13:41:58+00:00"
  },
  "data": {
    "message": "Hello, Master Asimov"
  }
}
```


get the stats from task api.

```bash
GET /api/tasks/88386aab-11a5-4d24-9a4b-13679087734f
X-Message-Struct: fully
```

response:

```json
HTTP/1.1 200

{
  "id": "88386aab-11a5-4d24-9a4b-13679087734f",
  "metadata": {
    "_kind": "platform.SayHello",
    "_state": "running",
    "_type": "processing",
    "_creationTime": "2024-10-10T13:41:58+00:00"
  },
  "data": {
    "message": "Hello, Master Asimov"
  }
}
```

```bash
GET /api/tasks/88386aab-11a5-4d24-9a4b-13679087734f
X-Message-Struct: simple
```

response:

```json
HTTP/1.1 200

{
  "id": "88386aab-11a5-4d24-9a4b-13679087734f",
  "_state": "running",
  "_creationTime": "2024-10-10T13:41:58+00:00",
  "message": "Hello, Master Asimov"
}
```

again:

```json
HTTP/1.1 200

{
  "id": "88386aab-11a5-4d24-9a4b-13679087734f",
  "metadata": {
    "_kind": "platform.SayHello",
    "_state": "completed",
    "_type": "processing",
    "_creationTime": "2024-10-10T13:41:58+00:00",
    "_finishedTime": "2024-10-10T13:42:58+00:00"
  },
  "data": {
    "message": "Hello, Master Asimov"
  }
}
```