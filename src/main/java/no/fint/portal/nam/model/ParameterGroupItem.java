package no.fint.portal.nam.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParameterGroupItem{

	@JsonProperty("groupName")
	private String groupName;

	@JsonProperty("enumerativeValue")
	private int enumerativeValue;

	@JsonProperty("userInterfaceID")
	private String userInterfaceID;

	@JsonProperty("choice")
	private List<ChoiceItem> choice;

	@JsonProperty("order")
	private int order;
}