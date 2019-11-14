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