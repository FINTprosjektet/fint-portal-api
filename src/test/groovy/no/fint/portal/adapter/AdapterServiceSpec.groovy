package no.fint.portal.adapter

import no.fint.portal.ldap.LdapService
import no.fint.portal.organisation.Organisation
import no.fint.portal.testutils.ObjectFactory
import spock.lang.Specification

class AdapterServiceSpec extends Specification {

    private adapterService
    private ldapService
    private adapterObjectService

    def setup() {
        def organisationBase = "ou=org,o=fint"
        ldapService = Mock(LdapService)
        adapterObjectService = new AdapterObjectService(organisationBase: organisationBase)
        adapterService = new AdapterService(adapterObjectService: adapterObjectService, ldapService: ldapService)

    }

    def "Add Adapter"() {
        given:
        def adapter = ObjectFactory.newAdapter()

        when:
        def created = adapterService.addAdapter(adapter, new Organisation(orgId: "test.no", uuid: "uuid"))

        then:
        created == true
        adapter.dn != null
        adapter.uuid != null
        1 * ldapService.createEntry(_ as Adapter) >> true
    }

    def "Get Adapters"() {
        when:
        def adapters = adapterService.getAdapters(UUID.randomUUID().toString())

        then:
        adapters.size() == 2
        1 * ldapService.getAll(_ as String, _ as Class) >> Arrays.asList(ObjectFactory.newAdapter(), ObjectFactory.newAdapter())
    }

    def "Get Adapter"() {
        when:
        def adapter = adapterService.getAdapter(UUID.randomUUID().toString(), UUID.randomUUID().toString())

        then:
        adapter.isPresent()
        1 * ldapService.getEntry(_ as String, _ as Class) >> ObjectFactory.newAdapter()
    }

    def "Update Adapter"() {
        when:
        def updated = adapterService.updateAdapter(ObjectFactory.newAdapter())

        then:
        updated == true
        1 * ldapService.updateEntry(_ as Adapter) >> true
    }

    def "Delete Adapter"() {
        when:
        adapterService.deleteAdapter(ObjectFactory.newAdapter())

        then:
        1 * ldapService.deleteEntry(_ as Adapter)
    }

    def "Reset Adapter Password"() {
        given:
        def adapter = ObjectFactory.newAdapter()

        when:
        adapterService.resetAdapterPassword(adapter)

        then:
        adapter.secret != null
        1 * ldapService.updateEntry(_ as Adapter)
    }
}
