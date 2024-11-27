package com.gtcafe.asimov.apiserver.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.core.common.context.HttpRequestContext;
import com.gtcafe.asimov.core.common.utils.Slogan;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EntryController {

    @Autowired
    private Slogan slogan;

    @GetMapping(value = "/", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> getRootMessage() {
        return ResponseEntity.ok("root");
    }

    @GetMapping(value = "/version", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> getSlogon() {
        return ResponseEntity.ok(slogan.apiSlogan(HttpRequestContext.getCurrentContext().getRequestId()));
    }

    @GetMapping(value = "/metric", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> metric() {
        return ResponseEntity.ok("metric");
    }

    @GetMapping(value = "/health", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("health");
    }

}
