package no.fint.portal.component

import no.fint.portal.ldap.LdapService
import no.fint.portal.organisation.Organisation
import no.fint.portal.testutils.ObjectFactory
import spock.lang.Specification

class ComponentServiceSpec extends Specification {
    private componentService
    private componentObjectService
    private ldapService
    private organisationComponentService
    private adapterObjectService
    private clientObjectService

    def setup() {
        def componentBase = "ou=comp,o=fint"
        ldapService = Mock(LdapService)
        componentObjectService = new ComponentObjectService(ldapService: ldapService, componentBase: componentBase)
        organisationComponentService = new OrganisationComponentService(componentObjectService: componentObjectService)
        adapterObjectService = new AdapterObjectService(componentBase: componentBase)
        clientObjectService = new ClientObjectService(componentBase: componentBase)
        componentService = new ComponentService(
                ldapService: ldapService,
                componentBase: componentBase,
                organisationComponentService: organisationComponentService,
                componentObjectService: componentObjectService,
                adapterObjectService: adapterObjectService,
                clientObjectService: clientObjectService
        )
    }

    def "Create Component"() {
        given:
        def component = ObjectFactory.newComponent()

        when:
        def created = componentService.createComponent(component)

        then:
        component.dn != null
        component.uuid != null
        created == true
        1 * ldapService.createEntry(_ as Component) >> true
    }

    def "Update Component"() {
        when:
        def updated = componentService.updateComponent(ObjectFactory.newComponent())

        then:
        updated == true
        1 * ldapService.updateEntry(_ as Component) >> true
    }

    def "Get Components"() {
        when:
        def components = componentService.getComponents()

        then:
        components.size() == 2
        1 * ldapService.getAll(_ as String, _ as Class) >> Arrays.asList(ObjectFactory.newComponent(), ObjectFactory.newComponent())
    }

    def "Get Component By UUID"() {
        when:
        def component = componentService.getComponentByUUID(UUID.randomUUID().toString())

        then:
        component != null
        1 * ldapService.getEntry(_ as String, _ as Class) >> ObjectFactory.newComponent()
    }

    def "Delete Component"() {
        when:
        componentService.deleteComponent(ObjectFactory.newComponent())

        then:
        1 * ldapService.deleteEntry(_ as Component)
    }

    def "Add Organisation To Component"() {
        when:
        componentService.addOrganisationToComponent(UUID.randomUUID().toString(), UUID.randomUUID().toString())

        then:
        3 * ldapService.createEntry(_ as Container)
    }

    def "Remove Organisation From Component"() {
        when:
        componentService.removeOrganisationFromComponent(UUID.randomUUID().toString(), UUID.randomUUID().toString())

        then:
        2 * ldapService.getAll(_ as String, _ as Class) >> Arrays.asList(ObjectFactory.newClient(), ObjectFactory.newClient()) >> Arrays.asList(ObjectFactory.newAdapter())
        3 * ldapService.deleteEntry(_ as Container)
        1 * ldapService.deleteEntry(_ as Adapter)
        2 * ldapService.deleteEntry(_ as Client)
    }

    def "Add Client"() {
        given:
        def client = ObjectFactory.newClient()

        when:
        def created = componentService.addClient(client, UUID.randomUUID().toString(),  new Organisation(orgId: "test.no", uuid: "uuid"))

        then:
        created == true
        client.dn != null
        client.uuid != null
        1 * ldapService.createEntry(_ as Client) >> true
    }

    def "Add Adapter"() {
        given:
        def adapter = ObjectFactory.newAdapter()

        when:
        def created = componentService.addAdapter(adapter, UUID.randomUUID().toString(),  new Organisation(orgId: "test.no", uuid: "uuid"))

        then:
        created == true
        adapter.dn != null
        adapter.uuid != null
        1 * ldapService.createEntry(_ as Adapter) >> true
    }

    def "Get Clients"() {
        when:
        def clients = componentService.getClients(UUID.randomUUID().toString(), UUID.randomUUID().toString())

        then:
        clients.size() == 2
        1 * ldapService.getAll(_ as String, _ as Class) >> Arrays.asList(ObjectFactory.newClient(), ObjectFactory.newClient())
    }

    def "Get Adapters"() {
        when:
        def adapters = componentService.getAdapters(UUID.randomUUID().toString(), UUID.randomUUID().toString())

        then:
        adapters.size() == 2
        1 * ldapService.getAll(_ as String, _ as Class) >> Arrays.asList(ObjectFactory.newAdapter(), ObjectFactory.newAdapter())
    }

    def "Get Client"() {
        when:
        def client = componentService.getClient(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString())

        then:
        client.isPresent()
        1 * ldapService.getEntry(_ as String, _ as Class) >> ObjectFactory.newClient()
    }

    def "Get Adapter"() {
        when:
        def adapter = componentService.getAdapter(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString())

        then:
        adapter.isPresent()
        1 * ldapService.getEntry(_ as String, _ as Class) >> ObjectFactory.newAdapter()
    }

    def "Update Client"() {
        when:
        def updated = componentService.updateClient(ObjectFactory.newClient())

        then:
        updated == true
        1 * ldapService.updateEntry(_ as Client) >> true
    }

    def "Update Adapter"() {
        when:
        def updated = componentService.updateAdapter(ObjectFactory.newAdapter())

        then:
        updated == true
        1 * ldapService.updateEntry(_ as Adapter) >> true
    }

    def "Delete Client"() {
        when:
        componentService.deleteClient(ObjectFactory.newClient())

        then:
        1 * ldapService.deleteEntry(_ as Client)
    }

    def "Delete Adapter"() {
        when:
        componentService.deleteAdapter(ObjectFactory.newAdapter())

        then:
        1 * ldapService.deleteEntry(_ as Adapter)
    }

    def "Reset Client Password"() {
        given:
        def client = ObjectFactory.newClient()

        when:
        componentService.resetClientPassword(client)

        then:
        client.password != null
        1 * ldapService.updateEntry(_ as Client)
    }

    def "Reset Adapter Password"() {
        given:
        def adapter = ObjectFactory.newAdapter()

        when:
        componentService.resetAdapterPassword(adapter)

        then:
        adapter.secret != null
        1 * ldapService.updateEntry(_ as Adapter)
    }
}
