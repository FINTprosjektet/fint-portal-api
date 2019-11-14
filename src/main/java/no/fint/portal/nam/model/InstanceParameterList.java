package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstanceParameterList {

    @JsonProperty("parameter")
    private List<ParameterItem> parameter;

    @JsonProperty("parameterGroup")
    private List<ParameterGroupItem> parameterGroup;
}