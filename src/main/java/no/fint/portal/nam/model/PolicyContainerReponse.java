package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PolicyContainerReponse {

    @JsonProperty("response")
    private Response response;

    @JsonProperty("policyList")
    private PolicyList policyList;
}