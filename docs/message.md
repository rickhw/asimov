
## Message Structure

```json
{
    "apiVersion": "v1alpha",
    "kind": "platform.Task",
    "id": "uuid-1234-5678-91011",   // write once, read many
    "key": "system-item",           // write once, read many (optional)
    "metadata": {        
        "_state": "running",        // state machine
        "_type": "processing",      // resource, record, processing (cpu-bound)
        "_creationTime": "2024-10-10T13:41:58+00:00",
        "_lastModified": "2024-10-10T13:41:58+00:00"  
    },
    "data": {
    }
}
```


## Task Message

```json
{
    "apiVersion": "v1alpha",
    "kind": "platform.Task",
    "id": "uuid-1234-5678-91011",   // write once, read many
    //"key": "system-item",           // write once, read many (optional)
    "metadata": {        
        "_state": "RUNNING",        // state machine
        "_type": "processing",      // resource, record, processing (cpu-bound)
        "_creationTime": "2024-10-10T13:41:58+00:00",
        "_lastModified": "2024-10-10T13:41:58+00:00"  
    },
    "data": {
    }
}
```

simple:

```json
{
    "id": "uuid-1234-5678-91011",   // write once, read many
    "_state": "RUNNING",        // state machine
    "_creationTime": "2024-10-10T13:41:58+00:00",
    "_lastModified": "2024-10-10T13:41:58+00:00",
    "data": {
    }
}```

## Hello Message

```json
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
        "message": "Hi Asimov"
    }
}
```

simple:

```json
{
    "message": "Hi Asimov"
}
```



## Async Hello Message

```json
{
    "apiVersion": "v1alpha",
    "kind": "platform.Task",
    "id": "uuid-1234-5678-91011",   // write once, read many
    //"key": "system-item",           // write once, read many (optional)
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
                "message": "Hi Asimov"
            }
        }
    }
}
```