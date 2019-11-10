package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.annotation.Generated;

@Data
public class PolicyEnforcementPointRef{

	@JsonProperty("elementRefType")
	private String elementRefType;

	@JsonProperty("externalElementRef")
	private String externalElementRef;

	@JsonProperty("externalDocRef")
	private String externalDocRef;
}