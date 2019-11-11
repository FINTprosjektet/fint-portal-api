package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PolicyResponseItem {

    @JsonProperty("enable")
    private boolean enable;

    @JsonProperty("rule")
    private List<Object> rule;

    @JsonProperty("uri")
    private String uri;
}
