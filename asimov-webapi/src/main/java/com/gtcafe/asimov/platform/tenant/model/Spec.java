package com.gtcafe.asimov.platform.tenant.model;

import lombok.Getter;
import lombok.Setter;

public class Spec {

    @Setter @Getter
    String rootAccount; 	// "rick@abc.com"

    @Setter @Getter
    String description;		// "this is a tenant"

    @Setter @Getter
    String displayName;		// "this is a tenant"

    
}