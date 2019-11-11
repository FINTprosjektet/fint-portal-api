package no.fint.portal.nam.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.annotation.Generated;

@Data
public class PolicyList{

	@JsonProperty("total")
	private int total;

	@JsonProperty("uri")
	private String uri;

	@JsonProperty("policy")
	private List<PolicyItem> policy;
}