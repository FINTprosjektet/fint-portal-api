package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConditionItem {

    @JsonProperty("not")
    private boolean not;

    @JsonProperty("rhsOperand")
    private RhsOperand rhsOperand;

    @JsonProperty("enable")
    private boolean enable;

    @JsonProperty("userInterfaceID")
    private String userInterfaceID;

    @JsonProperty("conditionRef")
    private ConditionRef conditionRef;

    @JsonProperty("resultOnError")
    private boolean resultOnError;

    @JsonProperty("instanceParameterList")
    private InstanceParameterList instanceParameterList;

    @JsonProperty("lhsOperand")
    private LhsOperand lhsOperand;

    @JsonProperty("operatorRef")
    private OperatorRef operatorRef;

    @JsonProperty("order")
    private int order;
}