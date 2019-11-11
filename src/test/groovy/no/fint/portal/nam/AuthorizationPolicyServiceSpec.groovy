package no.fint.portal.nam

import groovy.json.JsonGenerator
import spock.lang.Ignore
import spock.lang.Specification

class AuthorizationPolicyServiceSpec extends Specification {

    private AuthorizationPolicyService authorizationPolicyService;

    void setup() {
        authorizationPolicyService = new AuthorizationPolicyService(
                adapterScope: "fint-client",
                clientScope: "fint-client",
                clientAttribute: "fintClientComponents",
                adapterAttribute: "fintAdapterComponents"
        )
    }

    @Ignore
    def "Print JSON for policy for test purposes"() {
        given:
        def clientPolicy = authorizationPolicyService.createClientPolicy("test", "ou=utdanning_elev,ou=components,o=fint")

        when:
        def jsonDefaultOutput = new JsonGenerator.Options().excludeNulls().build()
        def json = jsonDefaultOutput.toJson(clientPolicy)
        println json

        then:
        true
    }

    def "Creating a client policy should return a client policy"() {
        when:
        def clientPolicy = authorizationPolicyService.createClientPolicy("test", "ou=utdanning_elev,ou=components,o=fint")

        then:
        clientPolicy.policyName == "a_client_test"
        clientPolicy.rule.size() == 3
        clientPolicy.rule[0].actionList.action.size() == 1
        clientPolicy.rule[0].conditionList.conditionSet.size() == 1
    }

    def "Creating a adapter policy should return a adapter policy"() {
        when:
        def adapterPolicy = authorizationPolicyService.createAdapterPolicy("test", "ou=utdanning_elev,ou=components,o=fint")

        then:
        adapterPolicy.policyName == "a_adapter_test"
        adapterPolicy.rule.size() == 3
        adapterPolicy.rule[0].actionList.action.size() == 1
        adapterPolicy.rule[0].conditionList.conditionSet.size() == 1
    }
}
