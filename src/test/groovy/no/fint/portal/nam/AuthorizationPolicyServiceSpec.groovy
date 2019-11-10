package no.fint.portal.nam

import groovy.json.JsonGenerator
import spock.lang.Specification

class AuthorizationPolicyServiceSpec extends Specification {

    def "Name"() {
        given:
        def authorizationPolicyService = new AuthorizationPolicyService(adapterScope: "fint-client", clientScope: "fint-client")

        when:
        def clientPolicy = authorizationPolicyService.createClientPolicy("test", "component")

        then:
        clientPolicy.policyName == "a_client_test"
        clientPolicy.rule.size() == 1
        clientPolicy.rule[0].actionList.action.size() == 1
        clientPolicy.rule[0].conditionList.conditionSet.size() == 1

        def jsonDefaultOutput = new JsonGenerator.Options().excludeNulls().build()
        def json = jsonDefaultOutput.toJson(clientPolicy)
        println json

    }
}
