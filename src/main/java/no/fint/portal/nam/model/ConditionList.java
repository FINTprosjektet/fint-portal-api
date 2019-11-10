package no.fint.portal.nam.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConditionList{

	@JsonProperty("conditionSet")
	private List<ConditionSetItem> conditionSet;
}