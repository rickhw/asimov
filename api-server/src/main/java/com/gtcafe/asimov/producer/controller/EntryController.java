package com.gtcafe.asimov.producer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntryController {

    Logger logger = LoggerFactory.getLogger(EntryController.class);

    // @Autowired
    // private Utils utils;

    @GetMapping(value = "/", produces = { "text/plain" })
    public ResponseEntity<String> getRootMessage() {

            // String reqId = response.getHeader(HttpHeaderConstants.R_REQUEST_ID);
        logger.info("Entry Controller");

        return ResponseEntity.ok("ok");
    }


}
