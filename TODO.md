
# User Story

## Hello API

- [x] sync
- [ ] async
  - [ ] producer
    - [x] rabbitMQ multiple config
    - [v] common message producer
    - [ ] common constants and model
    - [ ] task object
    - [ ] store to cache
  - [ ] concumser
    - [ ] common consummer
  - [ ] log

## Tenant Users

- [ ] Apply an new tenant
  - model: TenantProfile
- [ ] Apply an API key for a tenant


## SaaS Platform Admin



## Service Providers




---

## Tech Issue

- core
    - common librares for web server
    - TenantContext
    - ApiMetadataContext
- async
    - message dispatch, type
    - annotation: @DryRun, @Async