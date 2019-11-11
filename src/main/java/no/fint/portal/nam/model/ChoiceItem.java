package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChoiceItem {

    @JsonProperty("choiceName")
    private String choiceName;

    @JsonProperty("enumerativeValue")
    private int enumerativeValue;

    @JsonProperty("userInterfaceID")
    private String userInterfaceID;

    @JsonProperty("parameter")
    private List<Object> parameter;

    @JsonProperty("enabled")
    private boolean enabled;

    @JsonProperty("order")
    private int order;
}