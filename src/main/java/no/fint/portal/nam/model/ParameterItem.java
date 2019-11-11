package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParameterItem {

    @JsonProperty("enumerativeValue")
    private int enumerativeValue;

    @JsonProperty("userInterfaceID")
    private String userInterfaceID;

    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private String value;
}