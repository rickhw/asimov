package com.gtcafe.asimov.apiserver.system.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EntryController {

    // @Autowired
    // private Utils utils;

    @GetMapping(value = "/", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> getRootMessage() {

            // String reqId = response.getHeader(HttpHeaderConstants.R_REQUEST_ID);
        log.info("Entry Controller");

        return ResponseEntity.ok("ok");
    }


}
