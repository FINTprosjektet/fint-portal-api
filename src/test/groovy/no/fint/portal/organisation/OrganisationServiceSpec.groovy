package no.fint.portal.organisation

import no.fint.portal.adapter.Adapter
import no.fint.portal.adapter.AdapterObjectService
import no.fint.portal.adapter.AdapterService
import no.fint.portal.client.Client
import no.fint.portal.client.ClientObjectService
import no.fint.portal.client.ClientService
import no.fint.portal.contact.Contact
import no.fint.portal.contact.ContactObjectService
import no.fint.portal.contact.ContactService
import no.fint.portal.ldap.LdapService
import no.fint.portal.model.Container
import no.fint.portal.testutils.ObjectFactory
import spock.lang.Specification

class OrganisationServiceSpec extends Specification {
    private organisationService
    private ldapService
    private organisationObjectService
    private contactService
    private adapterService
    private clientService

    def setup() {
        def organisationBase = "ou=org,o=fint"
        def contactObjectService = new ContactObjectService(organisationBase: organisationBase)
        def clientObjectService = new ClientObjectService(organisationBase: organisationBase)
        def adapterObjectService = new AdapterObjectService(organisationBase: organisationBase)

        ldapService = Mock(LdapService)
        adapterService = new AdapterService(adapterObjectService: adapterObjectService, ldapService: ldapService)
        clientService = new ClientService(clientObjectService: clientObjectService, ldapService: ldapService)
        contactService = new ContactService(contactObjectService: contactObjectService, ldapService: ldapService)
        organisationObjectService = new OrganisationObjectService(organisationBase: organisationBase, ldapService: ldapService)
        organisationService = new OrganisationService(
                organisationBase: organisationBase,
                ldapService: ldapService,
                organisationObjectService: organisationObjectService,
                contactService: contactService,
                adapterService: adapterService,
                clientService: clientService
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

    def "Delete Organisation"() {
        given:
        def organisation = ObjectFactory.newOrganisation()
        organisation.uuid = UUID.randomUUID().toString()
        organisation.dn = String.format("ou=%s,ou=org,o=fint", organisation.uuid)

        when:
        organisationService.deleteOrganisation(organisation)

        then:
        2 * ldapService.deleteEntry(_ as Contact)
        1 * ldapService.deleteEntry(_ as Organisation)
        2 * ldapService.deleteEntry(_ as Client)
        2 * ldapService.deleteEntry(_ as Adapter)
        2 * ldapService.deleteEntry(_ as Container)
        3 * ldapService.getAll(_ as String, _ as Class) >>
                Arrays.asList(ObjectFactory.newContact(), ObjectFactory.newContact()) >>
                Arrays.asList(ObjectFactory.newAdapter(), ObjectFactory.newAdapter()) >>
                Arrays.asList(ObjectFactory.newClient(), ObjectFactory.newClient())

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
