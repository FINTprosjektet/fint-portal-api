package no.fint.portal.client

import no.fint.portal.organisation.Organisation
import spock.lang.Specification

class ClientObjectServiceSpec extends Specification {
    def clientObjectService

    def setup() {
        clientObjectService = new ClientObjectService(organisationBase: "ou=comp,o=fint")
    }

    def "Get Adapter Base"() {
        when:
        def dn = clientObjectService.getClientBase("orgUuid")

        then:
        dn != null
        dn.toString().contains("orgUuid") == true
    }

    def "Get Adapter Dn"() {
        when:
        def dn = clientObjectService.getClientDn("adapterUuid", "orgUuid")

        then:
        dn != null
        dn.contains("adapterUuid")
    }

    def "Setup Adapter"() {
        given:
        def client = new Client()


        when:
        clientObjectService.setupClient(client, new Organisation(orgId: "test.no", uuid: "uuid"))

        then:
        client.secret != null
        client.dn != null
        client.uuid != null
    }
}
