package no.fint.portal.contact

import no.fint.portal.organisation.Organisation
import spock.lang.Specification

class ContactObjectServiceSpec extends Specification {

    def contactObjectService

    void setup() {
        def organisationBase = "ou=org,o=fint"
        contactObjectService = new ContactObjectService(organisationBase: organisationBase)
    }

    def "Set Contact Dn"() {
        given:
        def contact = new Contact(nin: "12345")
        def organisation = new Organisation(orgId: "test", uuid: "uuid")

        when:
        contactObjectService.setupContact(contact, organisation)

        then:
        contact.dn.contains(contact.nin)
        contact.dn.contains("uuid")
        contact.orgId.contains("test")
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
