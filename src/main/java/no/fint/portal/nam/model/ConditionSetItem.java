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
public class ConditionSetItem {

    @JsonProperty("setOrder")
    private int setOrder;

    @JsonProperty("condition")
    private List<ConditionItem> condition;

    @JsonProperty("not")
    private boolean not;

    @JsonProperty("enable")
    private boolean enable;

    @JsonProperty("userInterfaceID")
    private String userInterfaceID;

    @JsonProperty("order")
    private int order;
}