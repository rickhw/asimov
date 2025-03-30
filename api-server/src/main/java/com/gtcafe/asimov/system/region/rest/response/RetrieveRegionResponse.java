package com.gtcafe.asimov.system.region.rest.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RetrieveRegionResponse {

    private String regionCode;

    private String description;
}
