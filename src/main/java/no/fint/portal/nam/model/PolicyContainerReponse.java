package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.annotation.Generated;

@Data
public class PolicyContainerReponse {

    @JsonProperty("response")
    private Response response;

    @JsonProperty("policyList")
    private PolicyList policyList;

    @JsonProperty("code")
    private String code;
}