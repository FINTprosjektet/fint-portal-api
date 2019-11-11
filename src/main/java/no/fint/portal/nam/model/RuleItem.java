package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RuleItem {

    @JsonProperty("conditionList")
    private ConditionList conditionList;

    @JsonProperty("enable")
    private boolean enable;

    @JsonProperty("userInterfaceID")
    private String userInterfaceID;

    @JsonProperty("actionList")
    private ActionList actionList;

    @JsonProperty("description")
    private String description;

    @JsonProperty("ruleOrder")
    private int ruleOrder;

    @JsonProperty("conditionCombiningAlgorithm")
    private String conditionCombiningAlgorithm;

    @JsonProperty("ruleID")
    private String ruleID;

    @JsonProperty("priority")
    private int priority;
}