package no.fint.portal.nam;

import no.fint.portal.nam.model.Policy;
import no.fint.portal.nam.model.PolicyEnforcementPointRef;
import no.fint.portal.nam.model.RuleItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static no.fint.portal.nam.Constants.EXTERNAL_WITH_ID_REF;
import static no.fint.portal.nam.Constants.XPEML_PEP_AG_AUTHORIZATION;

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
        Policy policy = new Policy();
        policy.setEnable(true);
        policy.setPolicyName(name);
        policy.setDescription("Automatically created by the admin portal");

        PolicyEnforcementPointRef policyEnforcementPointRef = new PolicyEnforcementPointRef();
        policyEnforcementPointRef.setElementRefType(EXTERNAL_WITH_ID_REF);
        policyEnforcementPointRef.setExternalElementRef(XPEML_PEP_AG_AUTHORIZATION);
        policy.setPolicyEnforcementPointRef(policyEnforcementPointRef);

        policy.setRule(Arrays.asList(createScopeRule(scope)/*, createComponentRule(), createDenyRule()*/));

        return policy;
    }

    private RuleItem createScopeRule(String scope) {
        return ObjectFactory.createScopeRuleItem(scope);
    }

    private RuleItem createComponentRule() {
        return RuleItem.builder().build();
    }

    private RuleItem createDenyRule() {
        return RuleItem.builder().build();
    }
}
