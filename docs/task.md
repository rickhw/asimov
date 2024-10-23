

NOTES: 簡化設計，這一層可以拿掉。

## 建立一個 Tasks (Test Only)

request message:

```json
POST /api/tasks
Content-Type: application/json

{
    "apiVersion": "v1alpha",
    "kind": "platform.Hello",
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
    "kind": "platform.Hello",
    "metadata": {  
        "_state": "PENDING",  
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
    "kind": "platform.Hello",     
    "metadata": {  
        "_state": "RUNNING",  
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
    "kind": "platform.Hello",     
    "metadata": {  
        "_state": "COMPLETED",  
        "_type": "processing",
        "_creationTime": "2024-10-10T13:41:48+00:00",
        "_lastModified": "2024-10-10T13:41:58+00:00"  
    },
    "data": {
        "message": "Hello 2024-10-10T13:41:58+00:00"
    }
}
```