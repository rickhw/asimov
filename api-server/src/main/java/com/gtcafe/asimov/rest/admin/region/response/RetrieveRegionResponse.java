package com.gtcafe.asimov.rest.admin.region.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RetrieveRegionResponse {

    private String regionCode;

    private String description;
}
