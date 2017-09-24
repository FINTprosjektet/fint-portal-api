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

    /*
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
    */
}
