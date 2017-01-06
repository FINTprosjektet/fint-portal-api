package no.fint.portal.organisation

import no.fint.portal.ldap.LdapService
import no.fint.portal.testutils.ObjectFactory
import spock.lang.Specification

class OrganisationServiceSpec extends Specification {
    private organisationService
    private ldapService
    private organisationObjectService
    private contactObjectService

    def setup() {
        def organisationBase = "ou=org,o=fint"
        ldapService = Mock(LdapService)
        organisationObjectService = new OrganisationObjectService(organisationBase: organisationBase, ldapService: ldapService)
        contactObjectService = new ContactObjectService(organisationObjectService: organisationObjectService)
        organisationService = new OrganisationService(
                organisationBase: organisationBase,
                ldapService: ldapService,
                organisationObjectService: organisationObjectService,
                contactObjectService: contactObjectService
        )
    }

    def "Create Organisation"() {
        given:
        def organisation = ObjectFactory.newOrganisation()

        when:
        def created = organisationService.createOrganisation(organisation)

        then:
        created == true
        organisation.dn != null
        organisation.uuid != null
        1 * ldapService.createEntry(_ as Organisation) >> true
    }

    def "Update Organisation"() {
        when:
        def updated = organisationService.updateOrganisation(ObjectFactory.newOrganisation())

        then:
        updated == true
        1 * ldapService.updateEntry(_ as Organisation) >> true
    }

    def "Get Organisations"() {
        when:
        def organisations = organisationService.getOrganisations()

        then:
        organisations.size() == 2
        1 * ldapService.getAll(_ as String, _ as Class) >> Arrays.asList(ObjectFactory.newOrganisation(), ObjectFactory.newOrganisation())
    }

    def "Get Organisation By OrgId"() {
        when:
        def organisation1 = organisationService.getOrganisationByOrgId("test1.no")
        def organisation2 = organisationService.getOrganisationByOrgId("test2.no")

        then:
        organisation1.isPresent()
        organisation2.empty()
        2 * ldapService.getStringDnByUniqueName(_ as String, _ as String, _ as Class) >> "o=fint" >> null
        1 * ldapService.getEntry(_ as String, _ as Class) >> ObjectFactory.newOrganisation()
    }

    def "Get Contacts"() {
        when:
        def contacts = organisationService.getContacts(UUID.randomUUID().toString())

        then:
        contacts.size() == 2
        1 * ldapService.getAll(_ as String, _ as Class) >> Arrays.asList(ObjectFactory.newContact(), ObjectFactory.newContact())
    }

    def "Add Contact"() {
        given:
        def contact = ObjectFactory.newContact()

        when:
        def created = organisationService.addContact(contact, UUID.randomUUID().toString())

        then:
        created == true
        contact.dn != null
        1 * ldapService.getEntry(_ as String, _ as Class) >> ObjectFactory.newOrganisation()
        1 * ldapService.createEntry(_ as Contact) >> true
    }

    def "Get Contact"() {
        when:
        def contact1 = organisationService.getContact(UUID.randomUUID().toString(), "11111111111")
        def contact2 = organisationService.getContact(UUID.randomUUID().toString(), "11111111111")

        then:
        contact1.isPresent()
        contact2.empty()
        2 * ldapService.getEntry(_ as String, _ as Class) >> ObjectFactory.newContact() >> null
    }

    def "Update Contact"() {
        when:
        def updated = organisationService.updateContact(ObjectFactory.newContact())

        then:
        updated == true
        1 * ldapService.updateEntry(_ as Contact) >> true
    }

    def "Delete Contact"() {
        when:
        organisationService.deleteContact(ObjectFactory.newContact())

        then:
        1 * ldapService.deleteEntry(_ as Contact)
    }

    def "Delete Organisation"() {
        given:
        def organisation = ObjectFactory.newOrganisation()
        organisation.uuid = UUID.randomUUID().toString()

        when:
        organisationService.deleteOrganisation(organisation)

        then:
        2 * ldapService.deleteEntry(_ as Contact)
        1 * ldapService.deleteEntry(_ as Organisation)
        1 * ldapService.getAll(_ as String, _ as Class) >> Arrays.asList(ObjectFactory.newContact(), ObjectFactory.newContact())
    }

    def "Get Organisation By UUID"() {
        when:
        def organisation1 = organisationService.getOrganisationByUUID(UUID.randomUUID().toString())
        def organisation2 = organisationService.getOrganisationByUUID(UUID.randomUUID().toString())

        then:
        organisation1.isPresent()
        organisation2.empty()
        2 * ldapService.getEntry(_ as String, _ as Class) >> ObjectFactory.newOrganisation() >> null
    }
}
