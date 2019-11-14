package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PolicyList {

    @JsonProperty("total")
    private int total;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("policy")
    private List<PolicyItem> policy;
}