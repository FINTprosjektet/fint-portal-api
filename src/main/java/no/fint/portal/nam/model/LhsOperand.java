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
public class LhsOperand {

    @JsonProperty("value")
    private String value;

    @JsonProperty("contextDataElementRef")
    private ContextDataElementRef contextDataElementRef;
}