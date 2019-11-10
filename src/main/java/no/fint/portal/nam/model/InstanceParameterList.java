package no.fint.portal.nam.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstanceParameterList{

	@JsonProperty("parameter")
	private List<ParameterItem> parameter;

	@JsonProperty("parameterGroup")
	private List<ParameterGroupItem> parameterGroup;
}