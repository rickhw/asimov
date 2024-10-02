package com.gtcafe.asimov.apiserver.platform.entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
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
