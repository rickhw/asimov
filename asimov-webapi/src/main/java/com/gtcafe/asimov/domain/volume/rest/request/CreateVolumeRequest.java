package com.gtcafe.asimov.domain.volume.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateVolumeRequest {

	@NotBlank(message = "accountName cannot empty")
    @Size(max = 50, message = "accountName max lenght is 50.")
    private String accountName;

    @NotBlank(message = "description cannot empty")
    @Size(max = 255, message = "description max lenght is 255.")
    private String description;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
