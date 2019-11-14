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