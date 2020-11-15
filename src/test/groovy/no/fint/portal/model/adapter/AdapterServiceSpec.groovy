package no.fint.portal.model.adapter

import no.fint.portal.audit.UserEventAuditService
import no.fint.portal.ldap.LdapService
import no.fint.portal.model.asset.AssetService
import no.fint.portal.model.contact.Contact
import no.fint.portal.model.organisation.Organisation
import no.fint.portal.oauth.NamOAuthClientService
import no.fint.portal.oauth.OAuthClient
import no.fint.portal.testutils.ObjectFactory
import spock.lang.Specification

class AdapterServiceSpec extends Specification {

    private adapterService
    private ldapService
    private adapterObjectService
    private oauthService
    private assetService
    private userEventAuditService

    def setup() {
        def organisationBase = "ou=org,o=fint"
        ldapService = Mock(LdapService)
        assetService = Mock(AssetService)
        adapterObjectService = new AdapterObjectService(organisationBase: organisationBase)
        oauthService = Mock(NamOAuthClientService)
        userEventAuditService = Mock(UserEventAuditService)
        adapterService = new AdapterService(
                adapterObjectService: adapterObjectService,
                ldapService: ldapService,
                namOAuthClientService: oauthService,
                assetService: assetService,
                userEventAuditService: userEventAuditService
        )

    }

    def "Add Adapter"() {
        given:
        def adapter = ObjectFactory.newAdapter()

        when:
        def created = adapterService.addAdapter(adapter, new Organisation(name: "name"), new Contact())

        then:
        created == true
        adapter.dn != null
        adapter.name != null
        1 * ldapService.createEntry(_ as Adapter) >> true
        1 * oauthService.addOAuthClient(_ as String) >> new OAuthClient()
    }

    def "Get Adapters"() {
        when:
        def adapters = adapterService.getAdapters(UUID.randomUUID().toString(), UUID.randomUUID().toString())

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

    def "Get Adapter OpenID Secret"() {
        when:
        def adapter = adapterService.getAdapter(UUID.randomUUID().toString(), UUID.randomUUID().toString())
        def secret = adapterService.getAdapterSecret(adapter.get())

        then:
        secret
        1 * ldapService.getEntry(_ as String, _ as Class) >> ObjectFactory.newAdapter()
        1 * oauthService.getOAuthClient(_ as String) >> ObjectFactory.newOAuthClient()
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
        adapterService.resetAdapterPassword(adapter, "FIXME")

        then:
        1 * ldapService.updateEntry(_ as Adapter)
    }

}
