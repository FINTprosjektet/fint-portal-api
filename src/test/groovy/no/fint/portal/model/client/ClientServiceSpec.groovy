package no.fint.portal.model.client

import no.fint.portal.model.component.Component
import no.fint.portal.model.component.ComponentObjectService
import no.fint.portal.model.component.ComponentService
import no.fint.portal.ldap.LdapService
import no.fint.portal.oauth.NamOAuthClientService
import no.fint.portal.oauth.OAuthClient
import no.fint.portal.model.organisation.Organisation
import no.fint.portal.testutils.ObjectFactory
import spock.lang.Specification

class ClientServiceSpec extends Specification {

    private clientService
    private ldapService
    private clientObjectService
    private oauthService
    private componentService

    def setup() {
        def organisationBase = "ou=org,o=fint"
        def componentBase = "ou=comp,o=fint"

        ldapService = Mock(LdapService)
        oauthService = Mock(NamOAuthClientService)
        clientObjectService = new ClientObjectService(organisationBase: organisationBase)
        componentService = new ComponentService(
                componentBase: componentBase,
                ldapService: ldapService,
                componentObjectService: new ComponentObjectService(ldapService: ldapService),
        )
        clientService = new ClientService(
                clientObjectService: clientObjectService,
                ldapService: ldapService,
                namOAuthClientService: oauthService,
                componentService: componentService
        )
    }

    def "Add Client"() {
        given:
        def client = ObjectFactory.newClient()

        when:
        def created = clientService.addClient(client, new Organisation(name: "name"))

        then:
        created == true
        client.dn != null
        client.name != null
        1 * ldapService.createEntry(_ as Client) >> true
        1 * oauthService.addOAuthClient(_ as String) >> new OAuthClient()
    }

    def "Get Clients"() {
        when:
        def clients = clientService.getClients("orgName")

        then:
        clients.size() == 2
        1 * ldapService.getAll(_ as String, _ as Class) >> Arrays.asList(ObjectFactory.newClient(), ObjectFactory.newClient())
        2 * oauthService.getOAuthClient(_ as String) >> ObjectFactory.newOAuthClient()
    }

    def "Get Client"() {
        when:
        def client = clientService.getClient(UUID.randomUUID().toString(), UUID.randomUUID().toString())

        then:
        client.isPresent()
        1 * ldapService.getEntry(_ as String, _ as Class) >> ObjectFactory.newClient()
        1 * oauthService.getOAuthClient(_ as String) >> ObjectFactory.newOAuthClient()
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

    def "Add component to client"() {
        given:
        def client = ObjectFactory.newClient()
        def component = ObjectFactory.newComponent()

        client.setDn("name=c1")
        component.setDn("ou=comp1")

        when:
        clientService.linkComponent(client, component)

        then:
        client.getComponents().size() == 1
        1 * ldapService.updateEntry(_ as Client)
        1 * ldapService.updateEntry(_ as Component)
    }

    def "Remove component from client"() {
        given:
        def client = ObjectFactory.newClient()
        def comp1 = ObjectFactory.newComponent()
        def comp2 = ObjectFactory.newComponent()

        comp1.setDn("ou=comp1,o=fint")
        comp2.setDn("ou=comp2,o=fint")
        client.addComponent("ou=comp1,o=fint")
        client.addComponent("ou=comp2,o=fint")

        when:
        clientService.unLinkComponent(client, comp1)

        then:
        client.getComponents().size() == 1
        client.getComponents().get(0).equals("ou=comp2,o=fint")
        1 * ldapService.updateEntry(_ as Client)
        1 * ldapService.updateEntry(_ as Component)
    }
}
