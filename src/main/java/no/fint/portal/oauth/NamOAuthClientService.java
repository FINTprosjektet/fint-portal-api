package no.fint.portal.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;

@Service
public class NamOAuthClientService {

    @Autowired
    ObjectMapper mapper;
    @Value("${fint.nam.oauth.username}")
    private String username;
    @Value("${fint.nam.oauth.password}")
    private String password;
    @Value("${fint.nam.oauth.idp-hostname}")
    private String idpHostname;
    @Value("${fint.nam.oauth.clientId}")
    private String clientId;
    @Value("${fint.nam.oauth.clientSecret}")
    private String clientSecret;

    private OAuth2RestTemplate restTemplate;

    @PostConstruct
    private void init() {

        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername(username);
        resourceDetails.setPassword(password);
        resourceDetails.setAccessTokenUri(String.format(NamOAuthConstants.ACCESS_TOKEN_URL_TEMPLATE, idpHostname));
        resourceDetails.setClientId(clientId);
        resourceDetails.setClientSecret(clientSecret);
        resourceDetails.setGrantType(NamOAuthConstants.PASSWORD_GRANT_TYPE);
        resourceDetails.setScope(Arrays.asList(new String(NamOAuthConstants.SCOPE)));

        restTemplate = new OAuth2RestTemplate(resourceDetails);
    }

    public OAuthClient addOAuthClient(String name) {
        OAuthClient oAuthClient = new OAuthClient(name);
        String jsonOAuthClient = null;

        try {
            jsonOAuthClient = mapper.writeValueAsString(oAuthClient);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity request = new HttpEntity(jsonOAuthClient, headers);


        String response = restTemplate.postForObject(String.format(NamOAuthConstants.CLIENT_REGISTRATION_URL_TEMPLATE, idpHostname), request, String.class);

        try {
            return mapper.readValue(response, OAuthClient.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void removeOAuthClient(String clientId) {
        restTemplate.delete(String.format(NamOAuthConstants.CLIENT_URL_TEMPLATE, idpHostname, clientId));
    }

    public OAuthClient getOAuthClient(String clientId) {
        return restTemplate.getForObject(String.format(NamOAuthConstants.CLIENT_URL_TEMPLATE, idpHostname, clientId), OAuthClient.class);
    }
}
