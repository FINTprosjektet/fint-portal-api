package no.fint.portal.organisation

import no.fint.portal.ldap.LdapService
import spock.lang.Specification

class ContactObjectServiceSpec extends Specification {

    def contactObjectService

    void setup() {
        def ldapServiceMock = Mock(LdapService)
        def organisationObjectService = new OrganisationObjectService(ldapService: ldapServiceMock, organisationBase: "ou=org,o=fint")
        contactObjectService = new ContactObjectService(organisationObjectService: organisationObjectService)
    }

    def "Set Contact Dn"() {
        given:
        def contact = new Contact(nin: "12345")

        when:
        contactObjectService.setContactDn(contact, "test")

        then:
        contact.dn != null
        contact.dn.contains(contact.nin)
        contact.dn.contains("test")
    }

    def "Get Contact Dn"() {
        when:
        def dn = contactObjectService.getContactDn("orgUUID", "nin")

        then:
        dn != null
        dn.contains("cn")
        dn.contains("ou")
        dn.contains("orgUUID")
        dn.contains("nin")
    }
}
