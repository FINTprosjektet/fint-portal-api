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
public class Policy {

    @JsonProperty("policyName")
    private String policyName;

    @JsonProperty("userInterfaceID")
    private String userInterfaceID;

    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy;

    @JsonProperty("rule")
    private List<RuleItem> rule;

    @JsonProperty("description")
    private String description;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("dateArchived")
    private String dateArchived;

    @JsonProperty("policyEnforcementPointRef")
    private PolicyEnforcementPointRef policyEnforcementPointRef;

    @JsonProperty("dateCreated")
    private String dateCreated;

    @JsonProperty("policyId")
    private String policyId;

    @JsonProperty("enable")
    private boolean enable;

    @JsonProperty("lastModified")
    private String lastModified;

    @JsonProperty("category")
    private String category;
}