package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PolicyResponse {

    @JsonProperty("response")
    private Response response;

    @JsonProperty("policy")
    private Policy policy;
}
