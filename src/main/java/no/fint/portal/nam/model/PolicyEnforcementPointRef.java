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
public class PolicyEnforcementPointRef {

    @JsonProperty("elementRefType")
    private String elementRefType;

    @JsonProperty("externalElementRef")
    private String externalElementRef;

    @JsonProperty("externalDocRef")
    private String externalDocRef;
}