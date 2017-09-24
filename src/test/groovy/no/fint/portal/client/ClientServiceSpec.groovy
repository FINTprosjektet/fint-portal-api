package no.fint.portal.client

import no.fint.portal.ldap.LdapService
import no.fint.portal.oauth.NamOAuthClientService
import no.fint.portal.oauth.OAuthClient
import no.fint.portal.organisation.Organisation
import no.fint.portal.testutils.ObjectFactory
import org.junit.Ignore
import spock.lang.Specification

class ClientServiceSpec extends Specification {

    private clientService
    private ldapService
    private clientObjectService
    private oauthService

    def setup() {
        def organisationBase = "ou=org,o=fint"
        ldapService = Mock(LdapService)
        oauthService = Mock(NamOAuthClientService)
        clientObjectService = new ClientObjectService(organisationBase: organisationBase)
        clientService = new ClientService(clientObjectService: clientObjectService, ldapService: ldapService, namOAuthClientService: oauthService)
    }

    def "Add Client"() {
        given:
        def client = ObjectFactory.newClient()

        when:
        def created = clientService.addClient(client, new Organisation(orgId: "test.no", uuid: "uuid"))

        then:
        created == true
        client.dn != null
        client.uuid != null
        1 * ldapService.createEntry(_ as Client) >> true
        1 * oauthService.addOAuthClient(_ as String) >> new OAuthClient()
    }

    def "Get Clients"() {
        when:
        def clients = clientService.getClients(UUID.randomUUID().toString())

        then:
        clients.size() == 2
        1 * ldapService.getAll(_ as String, _ as Class) >> Arrays.asList(ObjectFactory.newClient(), ObjectFactory.newClient())
    }

    def "Get Client"() {
        when:
        def client = clientService.getClient(UUID.randomUUID().toString(), UUID.randomUUID().toString())

        then:
        client.isPresent()
        1 * ldapService.getEntry(_ as String, _ as Class) >> ObjectFactory.newClient()
    }

    def "Update Client"() {
        when:
        def updated = clientService.updateClient(ObjectFactory.newClient())

        then:
        updated == true
        1 * ldapService.updateEntry(_ as Client) >> true
    }

    def "Delete Client"() {
        when:
        clientService.deleteClient(ObjectFactory.newClient())

        then:
        1 * ldapService.deleteEntry(_ as Client)
    }

    def "Reset Client Password"() {
        given:
        def client = ObjectFactory.newClient()

        when:
        clientService.resetClientPassword(client)

        then:
        client.password != null
        1 * ldapService.updateEntry(_ as Client)
    }
}
