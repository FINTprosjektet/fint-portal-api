package no.fint.portal.organisation

import no.fint.portal.ldap.LdapService
import org.junit.Ignore
import spock.lang.Specification

@Ignore
class OrganisationObjectServiceSpec extends Specification {
    def organisationObjectService
    def ldapServiceMock

    def setup() {
        ldapServiceMock = Mock(LdapService)
        organisationObjectService = new OrganisationObjectService(organisationBase: "ou=org,o=fint", ldapService: ldapServiceMock)
    }

    def "Setup Organisation"() {
        given:
        def organisation = new Organisation(orgId: "test.no")

        when:
        organisationObjectService.setupOrganisation(organisation)

        then:
        organisation.dn != null
        organisation.uuid.length() == 36
        1 * ldapServiceMock.getEntryByUniqueName(_ as String, _ as String, _ as Class) >> null
    }

    def "Get Organisation DN By UUID"() {
        given:
        def uuid = UUID.randomUUID().toString()

        when:
        def dn1 = organisationObjectService.getOrganisationDnByUUID(uuid)
        def dn2 = organisationObjectService.getOrganisationDnByUUID(null)

        then:
        dn1 != null
        dn1 == String.format("ou=%s,%s", uuid, organisationObjectService.getOrganisationBase())
        dn1.contains(uuid) == true
        dn2 == null

    }
}
