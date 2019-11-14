package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PolicyItem {

    @JsonProperty("policyEnforcementPointRef")
    private PolicyEnforcementPointRef policyEnforcementPointRef;

    @JsonProperty("policyId")
    private String policyId;

    @JsonProperty("policyName")
    private String policyName;

    @JsonProperty("enable")
    private boolean enable;

    @JsonProperty("rule")
    private List<Object> rule;

    @JsonProperty("uri")
    private String uri;
}