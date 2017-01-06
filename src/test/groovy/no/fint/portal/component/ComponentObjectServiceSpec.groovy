package no.fint.portal.component

import no.fint.portal.ldap.LdapService
import spock.lang.Specification

class ComponentObjectServiceSpec extends Specification {
    private componentObjectService
    private ldapServiceMock

    void setup() {
        ldapServiceMock = Mock(LdapService)
        componentObjectService = new ComponentObjectService(componentBase: "ou=comp,o=fint", ldapService: ldapServiceMock)
    }

    def "Setup Component"() {
        given:
        def component = new Component(technicalName: "test")

        when:
        componentObjectService.setupComponent(component)

        then:
        component.dn != null
        component.uuid.length() == 36
        1 * ldapServiceMock.getEntryByUniqueName(_ as String, _ as String, _ as Class) >> null
    }

    def "Get Component DN By UUID"() {
        given:
        def uuid = UUID.randomUUID().toString()

        when:
        def dn1 = componentObjectService.getComponentDnByUUID(uuid)
        def dn2 = componentObjectService.getComponentDnByUUID(null)

        then:
        dn1 != null
        dn1 == String.format("ou=%s,%s", uuid, componentObjectService.getComponentBase())
        dn1.contains(uuid) == true
        dn2 == null

    }
}
