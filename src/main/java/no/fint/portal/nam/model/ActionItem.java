package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionItem {

    @JsonProperty("userInterfaceID")
    private String userInterfaceID;

    @JsonProperty("actionRef")
    private ActionRef actionRef;

    @JsonProperty("instanceParameterList")
    private InstanceParameterList instanceParameterList;

    @JsonProperty("order")
    private int order;
}