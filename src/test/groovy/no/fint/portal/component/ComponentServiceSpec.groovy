package no.fint.portal.component

import no.fint.portal.ldap.LdapService
import no.fint.portal.testutils.ObjectFactory
import spock.lang.Specification

class ComponentServiceSpec extends Specification {
    private componentService
    private componentObjectService
    private ldapService

    def setup() {
        def componentBase = "ou=comp,o=fint"
        ldapService = Mock(LdapService)
        componentObjectService = new ComponentObjectService(ldapService: ldapService, componentBase: componentBase)
        componentService = new ComponentService(
                ldapService: ldapService,
                componentBase: componentBase,
                componentObjectService: componentObjectService,
        )
    }

    def "Create Component"() {
        given:
        def component = ObjectFactory.newComponent()

        when:
        def created = componentService.createComponent(component)

        then:
        component.dn != null
        component.name != null
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

    def "Get Component DN By UUID"() {
        given:
        def uuid = UUID.randomUUID().toString()

        when:
        def dn1 = componentService.getComponentDnByUUID(uuid)
        def dn2 = componentService.getComponentDnByUUID(null)

        then:
        dn1 != null
        dn1 == String.format("ou=%s,%s", uuid, componentObjectService.getComponentBase())
        dn1.contains(uuid) == true
        dn2 == null

    }

    def "Add organisation to component"() {
        given:
        def organisation = ObjectFactory.newOrganisation()
        def component = ObjectFactory.newComponent()

        organisation.setDn("ou=org1")
        component.setDn("ou=comp1")

        when:
        componentService.linkOrganisation(component, organisation)

        then:
        component.getOrganisations().size() == 1
        1 * ldapService.updateEntry(_ as Component)
    }

    def "Remove organisation from component"() {
        given:
        def component = ObjectFactory.newComponent()
        def org1 = ObjectFactory.newOrganisation()
        def org2 = ObjectFactory.newOrganisation()

        org1.setDn("ou=org1,o=fint")
        org2.setDn("ou=org2,o=fint")
        component.addOrganisation(org1.getDn())
        component.addOrganisation(org2.getDn())

        when:
        componentService.unLinkOrganisation(component, org1)

        then:
        component.getOrganisations().size() == 1
        component.getOrganisations().get(0).equals("ou=org2,o=fint")
        1 * ldapService.updateEntry(_ as Component)
    }

    def "Add client to component"() {
        given:
        def client = ObjectFactory.newClient()
        def component = ObjectFactory.newComponent()

        client.setDn("cn=c1")
        component.setDn("ou=comp1")

        when:
        componentService.linkClient(component, client)

        then:
        component.getClients().size() == 1
        1 * ldapService.updateEntry(_ as Component)
    }

    def "Remove client from component"() {
        given:
        def component = ObjectFactory.newComponent()
        def c1 = ObjectFactory.newClient()
        def c2 = ObjectFactory.newClient()

        c1.setDn("cn=c1,o=fint")
        c2.setDn("cn=c2,o=fint")
        component.addClient(c1.getDn())
        component.addClient(c2.getDn())

        when:
        componentService.unLinkClient(component, c1)

        then:
        component.getClients().size() == 1
        component.getClients().get(0) == "cn=c2,o=fint"
        1 * ldapService.updateEntry(_ as Component)
    }

    def "Add adapter to component"() {
        given:
        def adapter = ObjectFactory.newAdapter()
        def component = ObjectFactory.newComponent()

        adapter.setDn("cn=a1")
        component.setDn("ou=comp1")

        when:
        componentService.linkAdapter(component, adapter)

        then:
        component.getAdapters().size() == 1
        1 * ldapService.updateEntry(_ as Component)
    }

    def "Remove adapter from component"() {
        given:
        def component = ObjectFactory.newComponent()
        def a1 = ObjectFactory.newAdapter()
        def a2 = ObjectFactory.newAdapter()

        a1.setDn("cn=a1,o=fint")
        a2.setDn("cn=a2,o=fint")
        component.addAdapter(a1.getDn())
        component.addAdapter(a2.getDn())

        when:
        componentService.unLinkAdapter(component, a1)

        then:
        component.getAdapters().size() == 1
        component.getAdapters().get(0) == "cn=a2,o=fint"
        1 * ldapService.updateEntry(_ as Component)
    }

}
