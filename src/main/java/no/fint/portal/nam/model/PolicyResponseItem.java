package no.fint.portal.nam.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PolicyResponseItem {

    @JsonProperty("enable")
    private boolean enable;

    @JsonProperty("rule")
    private List<Object> rule;

    @JsonProperty("uri")
    private String uri;
}
