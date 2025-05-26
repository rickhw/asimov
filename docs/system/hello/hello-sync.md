

## Async Message

request message:

```json
POST /api/hello
Content-Type: application/json
X-Request-Mode: sync

{
  "message": "Hello, Asimov"
}
```

response:

```json
HTTP/1.1 200 
Vary: Origin, Access-Control-Request-Method, Access-Control-Request-Headers
X-Request-Id: 85bccf78-c5fd-4966-ae86-2fb8a1fe23da
X-Content-Type-Options: nosniff
X-XSS-Protection: 0
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Mon, 26 May 2025 11:25:20 GMT
Connection: close

{
  "message": {
    "message": "Hello, World!"
  },
  "launchTime": "2025-05-26T11:25:20.849Z"
}
```
