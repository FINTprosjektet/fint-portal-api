package no.fint.portal.component

import no.fint.portal.ldap.LdapService
import org.junit.Ignore
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
}
