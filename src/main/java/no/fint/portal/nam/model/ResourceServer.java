package no.fint.portal.nam.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder
public class ResourceServer {

    private String resourceServer;
    private String scope;

    public String toEncodeString() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return URLEncoder.encode(objectMapper.writeValueAsString(this), "UTF-8");

        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
