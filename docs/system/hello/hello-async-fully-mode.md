

## Fully mode

Retrive the by task with fully mode

```bash
GET /api/tasks/88386aab-11a5-4d24-9a4b-13679087734f
X-Message-Struct: fully
```

response:

```json
HTTP/1.1 200

{
    "apiVersion": "v1alpha",
    "kind": "platform.Task",
    "id": "uuid-1234-5678-91011",   // write once, read many
    "metadata": {        
        "_state": "RUNNING",        // state machine
        "_type": "processing",      // resource, record, processing (cpu-bound)
        "_creationTime": "2024-10-10T13:41:58+00:00",
        "_lastModified": "2024-10-10T13:41:58+00:00"  
    },
    "data": {
        {
            "apiVersion": "v1alpha",
            "kind": "platform.Hello",
            "id": "uuid-1234-5678-91011",   // write once, read many
            //"key": "system-item",           // write once, read many (optional)
            "metadata": {        
                "_state": "RUNNING",        // state machine
                "_type": "processing",      // resource, record, processing (cpu-bound)
                "_creationTime": "2024-10-10T13:41:58+00:00",
                "_lastModified": "2024-10-10T13:41:58+00:00"  
            },
            "data": {
                "message": "Hello, Master Asimov",
            }
        }
    }
}
```

again:

```json
HTTP/1.1 200

{
    "apiVersion": "v1alpha",
    "kind": "platform.Task",
    "id": "uuid-1234-5678-91011",   // write once, read many
    "metadata": {        
        "_state": "COMPLETED",        // state machine
        "_type": "processing",      // resource, record, processing (cpu-bound)
        "_creationTime": "2024-10-10T13:41:58+00:00",
        "_lastModified": "2024-10-10T13:41:58+00:00"  
    },
    "data": {
        {
            "apiVersion": "v1alpha",
            "kind": "platform.Hello",
            "id": "uuid-1234-5678-91011",   // write once, read many
            "metadata": {        
                "_state": "COMPLETED",        // state machine
                "_type": "processing",      // resource, record, processing (cpu-bound)
                "_creationTime": "2024-10-10T13:41:58+00:00",
                "_lastModified": "2024-10-10T13:41:58+00:00"  
            },
            "data": {
                "message": "Hello, Master Asimov",
            }
        }
    }
}
```