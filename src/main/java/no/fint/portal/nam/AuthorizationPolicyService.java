package no.fint.portal.nam;

import no.fint.portal.nam.model.Policy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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


    public Policy createClientPolicy(String name, String componentOU) {
        return createPolicy(String.format("a_client_%s", name), componentOU, clientScope, clientAttribute);
    }

    public Policy createAdapterPolicy(String name, String componentOU) {
        return createPolicy(String.format("a_adapter_%s", name), componentOU, adapterScope, adapterAttribute);
    }

    private Policy createPolicy(String name, String componentOu, String scope, String attributeName) {
        return ObjectFactory.createAuthorizationPolicy(name, scope, componentOu, attributeName);
    }
}
