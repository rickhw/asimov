package com.gtcafe.asimov.platform.region.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<RegionEntity, Long>{

    RegionEntity findByRegionCode(String regionCode);

    // RegionEntity saveAndFlush(RegionEntity entity);
}
