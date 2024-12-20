package com.gtcafe.asimov.platform.region.repository;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "region")
@Data
public class RegionEntity {

    @Id
    @Column(name = "id")
	private String id;

	@Column(name = "region_code", unique = true)
	private String regionCode;

	@Column(name = "description")
	private String description;

	public RegionEntity() {
		this.id = UUID.randomUUID().toString();
	}

}
