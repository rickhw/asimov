package com.gtcafe.asimov.system.region;

import com.gtcafe.asimov.rest.admin.region.request.CreateRegionRequest;
import com.gtcafe.asimov.rest.admin.region.response.RetrieveRegionResponse;
import com.gtcafe.asimov.system.region.repository.RegionEntity;
import com.gtcafe.asimov.system.region.schema.Region;

public class RegionMapper {
    
    public static Region mapEntityToDomain(RegionEntity entity) {
        Region domain = new Region();
        domain.setRegionCode(entity.getRegionCode());
        domain.setDescription(entity.getDescription());

        return domain;
    }

    public static RetrieveRegionResponse mapDomainToResponse(Region region) {
        RetrieveRegionResponse response = RetrieveRegionResponse.builder()
            .regionCode(region.getRegionCode())
            .description(region.getDescription())
            .build();
        return response;
    }

    public static RegionEntity mapDomainToEntity(Region region) {
        RegionEntity entity = new RegionEntity();
        entity.setRegionCode(region.getRegionCode());
        entity.setDescription(region.getDescription());
        return entity;
    }

    public static Region mapRequestToDomain(CreateRegionRequest request) {

        Region domain = new Region();
        domain.setRegionCode(request.getRegionCode());
        domain.setDescription(request.getDescription());

        return domain;
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'mapRequestToDomain'");
    }

}
