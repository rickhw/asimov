package com.gtcafe.asimov.platform.tenant;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tenants")
public class TenantEntity {

    @Id
    @Column(name = "id")
	@Setter @Getter
	private String id;

	// for display only, modifiable
	@Column(name = "display_name")
	@Setter @Getter
	private String displayName;

	// a key value, unique, non-modifiable
	@Column(name = "tenant_key")
	@Setter @Getter
	private String tenantKey;

	// a key value, unique, non-modifiable
	@Column(name = "root_account")
	@Setter @Getter
	private String rootAccount;
	
	@Column(name = "description")
	@Setter @Getter
	private String description;

	public TenantEntity() {
		this.id = UUID.randomUUID().toString();
	}

}
