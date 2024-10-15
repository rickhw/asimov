
## 建立一個 Tasks (Test Only)

request message:

```json
POST /api/tasks
Content-Type: application/json

{
    "data": {
      "message": "Hello"
    }
}
```

response:

```json
HTTP/1.1 200

{
    "id": "uuid-1234-5678-91011",
    "apiVersion": "v1alpha",           
    "metadata": {  
        "_kind": "platform.Task",
        "_state": "pending",  
        "_type": "processing",
        "_creationTime": "2024-10-10T13:41:58+00:00"  
    },
    "data": {
      "message": "Hello"
    }
}
```


get the stats from task api.

```bash
GET /api/tasks/uuid-1234-5678-91011
X-Message-Struct: fully
```

response:

```json
HTTP/1.1 200

{
    "id": "uuid-1234-5678-91011",
    "apiVersion": "v1alpha",           
    "metadata": {  
        "_kind": "platform.Task",
        "_state": "running",  
        "_type": "processing",
        "_creationTime": "2024-10-10T13:41:58+00:00"  
    },
    "data": {
      "message": "Hello"
    }
}
```

again:

```json
HTTP/1.1 200

{
    "id": "uuid-1234-5678-91011",
    "apiVersion": "v1alpha",           
    "metadata": {  
        "_kind": "platform.Task",
        "_state": "completed",  
        "_type": "processing",
        "_creationTime": "2024-10-10T13:41:58+00:00",
        "_finishedTime": "2024-10-10T13:42:58+00:00"
    },
    "data": {
      "message": "Hello"
    }
}
```