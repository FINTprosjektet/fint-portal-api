package no.fint.portal.nam;

import no.fint.portal.nam.model.Policy;
import no.fint.portal.nam.model.RuleItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AuthorizationPolicyService {


    @Value(("${fint.nam.client.scope:fint-client}"))
    private String clientScope;

    @Value(("${fint.nam.adapter.scope:fint-client}"))
    private String adapterScope;


    public Policy createClientPolicy(String name, String componentOU) {
        return createPolicy(String.format("a_client_%s", name), "", clientScope);
    }

    public Policy createAdapterPolicy(String name, String componentOU, String scope) {
        return createPolicy(String.format("a_adapter_%s", name), "", adapterScope);
    }

    private Policy createPolicy(String name, String componentOU, String scope) {
        return ObjectFactory.createAuthorizationPolicy(name, scope);
    }
}
