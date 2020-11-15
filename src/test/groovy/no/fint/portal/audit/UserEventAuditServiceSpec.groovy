package no.fint.portal.audit


import no.fint.portal.model.adapter.Adapter
import no.fint.portal.model.contact.ContactService
import no.fint.portal.model.organisation.OrganisationService
import spock.lang.Specification

class UserEventAuditServiceSpec extends Specification {


    UserEventRepository userEventRepository = Mock(UserEventRepository)
    ContactService contactService = Mock(ContactService)
    OrganisationService organisationService = Mock(OrganisationService)
    UserEventAuditService userEventAuditService = new UserEventAuditService(
            userEventRepository,
            contactService,
            organisationService
    )


    def "Get organisation name from object"() {

        when:
        def organisationName = userEventAuditService
                .getOrganisationNameFromObject(
                        new Adapter(
                                dn: "cn=test2@adapter.fintlabs.no,ou=adapters,ou=fintlabs_no,ou=organisations,o=fint"
                        )
                )

        then:
        organisationName == "fintlabs_no"

    }
}
