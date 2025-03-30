package com.gtcafe.asimov.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.framework.context.ApiMetadataContext;
import com.gtcafe.asimov.framework.context.HttpRequestContext;
import com.gtcafe.asimov.framework.utils.Slogan;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name = "System/PredefinedAPI", description = "Entry Controller")
public class EntryController {

    @Autowired
    private Slogan slogan;

    @GetMapping(value = "/", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> getRootMessage() {
        return ResponseEntity.ok("ok");
    }

    @GetMapping(value = "/version", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> getSlogon() {
        ApiMetadataContext ctx = ApiMetadataContext.GetCurrentContext();
        log.info("API Metadata: kind: [{}], opId: [{}]", ctx.getKind(), ctx.getOperationId());
        return ResponseEntity.ok(slogan.apiSlogan(HttpRequestContext.GetCurrentContext().getRequestId()));
    }

    @GetMapping(value = "/metrics", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> metric() {
        return ResponseEntity.ok("metrics");
    }

    @GetMapping(value = "/health", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("health");
    }

    @GetMapping(value = "/apimeta", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> apimeta() {
        ApiMetadataContext ctx = ApiMetadataContext.GetCurrentContext();
// 
        return ResponseEntity.ok(ctx.toString());
    }

}
