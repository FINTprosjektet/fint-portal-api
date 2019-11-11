package no.fint.portal.utilities;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public final class HeaderUtils {

    private HeaderUtils() {
    }

    public static HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;

    }
}
