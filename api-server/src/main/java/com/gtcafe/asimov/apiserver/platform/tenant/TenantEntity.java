package com.gtcafe.asimov.apiserver.platform.tenant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tenants")
public class TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
	private long id;

	// for display only, modifiable
	@Column(name = "display_name")
	private String displayName;

	// a key value, unique, non-modifiable
	@Column(name = "tenant_key")
	private String tenantKey;

	@Column(name = "description")
	private String description;

	// @OneToOne(cascade = CascadeType.ALL)
	// @MapsId
	// @JoinColumn(name = "rootAccount")
  	// private AccountEntity rootAccount;


	public TenantEntity() {}

	// public TenantEntity(String tenantKey, String rootAccount) {
	// 	this.tenantKey = tenantKey;
	// 	this.displayName = tenantKey;
	// 	// this.rootAccount = rootAccount;
	// }

}
