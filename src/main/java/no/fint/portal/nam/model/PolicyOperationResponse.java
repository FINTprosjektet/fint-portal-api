package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PolicyOperationResponse {

    @JsonProperty("response")
    private Response response;

    @JsonProperty("policy")
    private PolicyResponseItem policy;
}