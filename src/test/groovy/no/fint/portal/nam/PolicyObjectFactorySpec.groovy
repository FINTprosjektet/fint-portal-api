package no.fint.portal.nam

import groovy.json.JsonGenerator
import spock.lang.Ignore
import spock.lang.Specification

class PolicyObjectFactorySpec extends Specification {

    @Ignore
    def "Print JSON for policy for test purposes"() {
        given:
        def clientPolicy = PolicyObjectFactory.createAuthorizationPolicy("test",  "fint-client", "ou=utdanning_elev,ou=components,o=fint","fintClientComponents")

        when:
        def jsonDefaultOutput = new JsonGenerator.Options().excludeNulls().build()
        def json = jsonDefaultOutput.toJson(clientPolicy)
        println json

        then:
        true
    }

    def "Creating a client policy should return a client policy"() {
        when:
        def clientPolicy = PolicyObjectFactory.createAuthorizationPolicy("test",  "fint-client", "ou=utdanning_elev,ou=components,o=fint","fintClientComponents")

        then:
        clientPolicy.rule.size() == 3
        clientPolicy.rule[0].actionList.action.size() == 1
        clientPolicy.rule[0].conditionList.conditionSet.size() == 1
    }

    def "Creating a adapter policy should return a adapter policy"() {
        when:
        def adapterPolicy = PolicyObjectFactory.createAuthorizationPolicy("test",  "fint-adapter", "ou=utdanning_elev,ou=components,o=fint","fintAdapterComponents")

        then:
        adapterPolicy.rule.size() == 3
        adapterPolicy.rule[0].actionList.action.size() == 1
        adapterPolicy.rule[0].conditionList.conditionSet.size() == 1
    }
}
