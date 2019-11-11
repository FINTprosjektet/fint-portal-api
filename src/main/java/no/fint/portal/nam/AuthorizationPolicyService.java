package no.fint.portal.nam;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fint.portal.nam.model.Policy;
import no.fint.portal.nam.model.PolicyContainerReponse;
import no.fint.portal.nam.model.PolicyCreateResponse;
import no.fint.portal.utilities.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class AuthorizationPolicyService {


    @Value(("${fint.nam.client.scope:fint-client}"))
    private String clientScope;

    @Value(("${fint.nam.client.attribute:fintClientComponents}"))
    private String clientAttribute;

    @Value(("${fint.nam.adapter.scope:fint-client}"))
    private String adapterScope;

    @Value(("${fint.nam.adapter.attribute:fintAdapterComponents}"))
    private String adapterAttribute;

    @Value("${fint.nam.oauth.admin-console-hostname:https://localhost:8443/amsvc/v1/policycontainers/8thk0xs6bn8wp3v/policies}")
    private String adminConsoleHostname;

    @Autowired
    private ObjectMapper mapper;

    private RestTemplate restTemplate;

    @PostConstruct
    private void init() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        restTemplate = restTemplateBuilder.basicAuthorization("", "").build();
    }

    public String createClientPolicy(String name, String componentOU) {
        Policy policy = PolicyObjectFactory.createAuthorizationPolicy(String.format("a_client_%s", name), componentOU, clientScope, clientAttribute);
        return addPolicy(policy);
    }

    public String createAdapterPolicy(String name, String componentOU) {
        Policy policy = PolicyObjectFactory.createAuthorizationPolicy(String.format("a_adapter_%s", name), componentOU, adapterScope, adapterAttribute);
        return addPolicy(policy);
    }

    public Policy getPolicy(String policyId) {
        return restTemplate.getForObject(adminConsoleHostname, Policy.class);
    }

    private String addPolicy(Policy policy) {
        try {
            String policyJson = mapper.writeValueAsString(policy);
            HttpEntity<String> request = new HttpEntity<>(policyJson, HeaderUtils.createHeaders());
            String response = restTemplate.postForObject(adminConsoleHostname, request, String.class);
            PolicyCreateResponse policyCreateResponse = mapper.readValue(response, PolicyCreateResponse.class);
            log.info("Policy created {}: ", policyCreateResponse.getResponse().getCode());

            return getAuthorizationPolicyId(policy.getPolicyName());
        } catch (Exception e) {
            log.error("Unable to create policy {}", policy.getPolicyName(), e);
            throw new RuntimeException(e);
        }
    }

    private String getAuthorizationPolicyId(String policyName) {
        log.info("Fetching policy {}...", policyName);
        PolicyContainerReponse policyContainerReponse = restTemplate.getForObject(adminConsoleHostname, PolicyContainerReponse.class);
        try {
            if (policyContainerReponse != null) {
                return policyContainerReponse.getPolicyList()
                        .getPolicy()
                        .stream()
                        .filter(policy -> policy.getPolicyName().equals(policyName))
                        .findFirst().orElseThrow(Exception::new).getPolicyId();
            }
        } catch (Exception e) {
            log.error("Unable to get policy {}", policyName);
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Unable to get policy");
    }

}
