package no.fint.portal.model.organisation

import no.fint.portal.model.adapter.Adapter
import no.fint.portal.model.adapter.AdapterObjectService
import no.fint.portal.model.adapter.AdapterService
import no.fint.portal.model.client.Client
import no.fint.portal.model.client.ClientObjectService
import no.fint.portal.model.client.ClientService
import no.fint.portal.model.component.Component
import no.fint.portal.model.component.ComponentObjectService
import no.fint.portal.model.component.ComponentService
import no.fint.portal.model.contact.Contact
import no.fint.portal.model.contact.ContactObjectService
import no.fint.portal.model.contact.ContactService
import no.fint.portal.ldap.LdapService
import no.fint.portal.ldap.Container
import no.fint.portal.oauth.NamOAuthClientService
import no.fint.portal.testutils.ObjectFactory
import spock.lang.Specification

class OrganisationServiceSpec extends Specification {
    private organisationService
    private ldapService
    private organisationObjectService
    private contactService
    private adapterService
    private clientService
    private oauthService
    private componentService

    def setup() {
        def organisationBase = "ou=org,o=fint"
        def componentBase = "ou=comp,o=fint"
        def contactObjectService = new ContactObjectService(organisationBase: organisationBase)
        def clientObjectService = new ClientObjectService(organisationBase: organisationBase)
        def adapterObjectService = new AdapterObjectService(organisationBase: organisationBase)

        ldapService = Mock(LdapService)
        oauthService = Mock(NamOAuthClientService)
        adapterService = new AdapterService(adapterObjectService: adapterObjectService, ldapService: ldapService, namOAuthClientService: oauthService)
        clientService = new ClientService(clientObjectService: clientObjectService, ldapService: ldapService, namOAuthClientService: oauthService)
        contactService = new ContactService(contactObjectService: contactObjectService, ldapService: ldapService)
        organisationObjectService = new OrganisationObjectService(organisationBase: organisationBase, ldapService: ldapService)
        componentService = new ComponentService(
                componentBase: componentBase,
                ldapService: ldapService,
                componentObjectService: new ComponentObjectService(ldapService: ldapService),
        )
        organisationService = new OrganisationService(
                organisationBase: organisationBase,
                ldapService: ldapService,
                organisationObjectService: organisationObjectService,
                contactService: contactService,
                adapterService: adapterService,
                clientService: clientService,
                componentService: componentService
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
        organisation.name != null
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
        def organisation1 = organisationService.getOrganisation("test1.no")
        def organisation2 = organisationService.getOrganisation("test2.no")

        then:
        organisation1.isPresent()
        organisation2.empty()
        2 * ldapService.getStringDnByUniqueName(_ as String, _ as String, _ as Class) >> "o=fint" >> null
        1 * ldapService.getEntry(_ as String, _ as Class) >> ObjectFactory.newOrganisation()
    }

    def "Delete Organisation"() {
        given:
        def organisation = ObjectFactory.newOrganisation()
        organisation.name = UUID.randomUUID().toString()
        organisation.dn = String.format("ou=%s,ou=org,o=fint", organisation.name)

        when:
        organisationService.deleteOrganisation(organisation)

        then:
        2 * ldapService.deleteEntry(_ as Contact)
        1 * ldapService.deleteEntry(_ as Organisation)
        2 * ldapService.deleteEntry(_ as Client)
        2 * ldapService.deleteEntry(_ as Adapter)
        2 * ldapService.deleteEntry(_ as Container)
        4 * oauthService.getOAuthClient(_ as String) >> ObjectFactory.newOAuthClient()
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

    def "Get Organisation DN By UUID"() {
        given:
        def uuid = UUID.randomUUID().toString()

        when:
        def dn1 = organisationService.getOrganisationDnByUUID(uuid)
        def dn2 = organisationService.getOrganisationDnByUUID(null)

        then:
        dn1 != null
        dn1 == String.format("ou=%s,%s", uuid, organisationObjectService.getOrganisationBase())
        dn1.contains(uuid) == true
        dn2 == null

    }

    def "Add component to organisation"() {
        given:
        def organisation = ObjectFactory.newOrganisation()
        def component = ObjectFactory.newComponent()

        organisation.setDn("ou=org1")
        component.setDn("ou=comp1")

        when:
        organisationService.linkComponent(organisation, component)

        then:
        organisation.getComponents().size() == 1
        1 * ldapService.updateEntry(_ as Organisation)
        1 * ldapService.updateEntry(_ as Component)
    }

    def "Remove component from organisation"() {
        given:
        def organisation = ObjectFactory.newOrganisation()
        def comp1 = ObjectFactory.newComponent()
        def comp2 = ObjectFactory.newComponent()

        comp1.setDn("ou=comp1,o=fint")
        comp2.setDn("ou=comp2,o=fint")
        organisation.addComponent("ou=comp1,o=fint")
        organisation.addComponent("ou=comp2,o=fint")

        when:
        organisationService.unLinkComponent(organisation, comp1)

        then:
        organisation.getComponents().size() == 1
        organisation.getComponents().get(0).equals("ou=comp2,o=fint")
        1 * ldapService.updateEntry(_ as Organisation)
        1 * ldapService.updateEntry(_ as Component)
    }
}
