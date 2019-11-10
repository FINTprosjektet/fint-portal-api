package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LhsOperand{

	@JsonProperty("value")
	private String value;

	@JsonProperty("contextDataElementRef")
	private ContextDataElementRef contextDataElementRef;
}