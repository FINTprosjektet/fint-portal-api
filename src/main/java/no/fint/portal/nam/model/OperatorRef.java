package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperatorRef {

    @JsonProperty("elementRefType")
    private String elementRefType;

    @JsonProperty("externalElementRef")
    private String externalElementRef;

    @JsonProperty("externalDocRef")
    private String externalDocRef;
}