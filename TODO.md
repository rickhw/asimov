
# User Story

## Hello API

- [x] sync
- [ ] async
  - [ ] producer
    - [x] rabbitMQ multiple config
    - [v] common message producer
    - [v] common constants and model
    - [v] task object
    - [v] store to cache
  - [ ] log

## Tenant Users

- [ ] Apply an new tenant
  - model: TenantProfile
- [ ] Apply an API key for a tenant


## SaaS Platform Admin (Backstage Web UI)



## Service Providers




---

## Tech Issue

- core
    - [ ] common librares for web server
    - [ ] TenantContext
    - [ ] ApiMetadataContext
- async
    - message dispatch, type
    - annotation: @DryRun, @Async