package no.fint.portal.component

import spock.lang.Specification

class ClientObjectServiceSpec extends Specification {
    def clientObjectService

    void setup() {
        clientObjectService = new ClientObjectService(componentBase: "ou=felleskomponenter,o=fint")
    }

    def "Setup Client"() {
        given:
        def client = new Client()

        when:
        clientObjectService.setupClient(client, "compUuid", "orgUuid")

        then:
        client.password != null
        client.dn.contains("compUuid")
        client.dn.contains("orgUuid")
        client.uuid.length() == 36
    }

    def "Get Client Base"() {
        when:
        def dn = clientObjectService.getClientBase("clientUuid", "orgUuid")

        then:
        dn != null
        dn.toString().contains("clientUuid") == true
        dn.toString().contains("orgUuid") == true
    }

    def "Get Client Dn"() {
        when:
        def dn = clientObjectService.getClientDn("clientUuid", "compUuid", "orgUuid")

        then:
        dn != null
        dn.contains("clientUuid")
    }
}
