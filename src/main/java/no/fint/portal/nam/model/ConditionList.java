package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConditionList {

    @JsonProperty("conditionSet")
    private List<ConditionSetItem> conditionSet;
}