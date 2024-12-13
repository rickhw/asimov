package com.gtcafe.asimov.platform.tenant.domain.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class KeySet {

 	/* write once, read manay by user */
     @Getter @Setter
     private String tenantKey; 
 
     @Getter @Setter
     @JsonProperty("_resourceId")
     private String resourceId; // "75ac3d12-d237-4a81-8173-049e948906d4"
 
     @Getter @Setter
     @JsonProperty("_tenantId")
     private String tenantId; 	// "t-1234567890"

     public KeySet() {
        this.resourceId = UUID.randomUUID().toString();
    }

}