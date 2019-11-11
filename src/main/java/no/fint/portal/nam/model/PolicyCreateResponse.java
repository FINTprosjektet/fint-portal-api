package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.annotation.Generated;

@Data
public class PolicyResponse{

	@JsonProperty("response")
	private Response response;

	@JsonProperty("policy")
	private PolicyResponseItem policy;
}