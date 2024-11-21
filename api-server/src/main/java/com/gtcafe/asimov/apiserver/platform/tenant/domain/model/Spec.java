package com.gtcafe.asimov.apiserver.platform.tenant.domain.model;

public class Spec {
    String rootAccount; 	// "rick@abc.com"
    String description;		// "this is a tenant"
    public String getRootAccount() {
        return rootAccount;
    }
    public void setRootAccount(String rootAccount) {
        this.rootAccount = rootAccount;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}