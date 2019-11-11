package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InstanceParameterList {

    @JsonProperty("parameter")
    private List<ParameterItem> parameter;

    @JsonProperty("parameterGroup")
    private List<ParameterGroupItem> parameterGroup;
}