package no.fint.portal.model.contact

import no.fint.portal.ldap.LdapService
import no.fint.portal.testutils.ObjectFactory
import spock.lang.Specification

class ContactServiceSpec extends Specification {

    private contactService
    private ldapService
    private contactObjectService

    def setup() {
        def contactBase = "ou=contacts,o=fint"
        ldapService = Mock(LdapService)
        contactObjectService = new ContactObjectService(contactBase: contactBase)
        contactService = new ContactService(contactObjectService: contactObjectService, ldapService: ldapService)

    }

    def "Get All Contacts"() {
        when:
        def contacts = contactService.getContacts()

        then:
        contacts.size() == 2
        1 * ldapService.getAll(_ as String, _ as Class) >> Arrays.asList(ObjectFactory.newContact(), ObjectFactory.newContact())
    }

    def "Add Contact"() {
        given:
        def contact = ObjectFactory.newContact()

        when:
        def created = contactService.addContact(contact)

        then:
        created == true
        contact.dn != null
        1 * ldapService.createEntry(_ as Contact) >> true
    }

    def "Get Contact"() {
        when:
        def contact1 = contactService.getContact("11111111111")
        def contact2 = contactService.getContact("11111111111")

        then:
        contact1.isPresent()
        contact2.empty()
        2 * ldapService.getEntry(_ as String, _ as Class) >> ObjectFactory.newContact() >> null
    }

    def "Update Contact"() {
        when:
        def updated = contactService.updateContact(ObjectFactory.newContact())

        then:
        updated == true
        1 * ldapService.updateEntry(_ as Contact) >> true
    }

    def "Delete Contact"() {
        when:
        contactService.deleteContact(ObjectFactory.newContact())

        then:
        1 * ldapService.deleteEntry(_ as Contact)
    }
}
