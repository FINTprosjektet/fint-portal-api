package no.fint.portal.nam.model

import spock.lang.Specification

class ResourceServerSpec extends Specification {

    def "Should return a url encode string"() {
        given:
        def build = ResourceServer.builder().resourceServer("fint-api").scope("fint-client").build()

        when:
        def string = build.toEncodeString()

        then:
        string == "%7B%22resourceServer%22%3A%22fint-api%22%2C%22scope%22%3A%22fint-client%22%7D"
    }
}
