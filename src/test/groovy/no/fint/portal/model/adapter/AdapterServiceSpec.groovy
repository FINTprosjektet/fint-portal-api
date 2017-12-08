package no.fint.portal.model.adapter

import no.fint.portal.model.component.Component
import no.fint.portal.model.component.ComponentObjectService
import no.fint.portal.model.component.ComponentService
import no.fint.portal.ldap.LdapService
import no.fint.portal.oauth.NamOAuthClientService
import no.fint.portal.oauth.OAuthClient
import no.fint.portal.model.organisation.Organisation
import no.fint.portal.testutils.ObjectFactory
import spock.lang.Specification

class AdapterServiceSpec extends Specification {

    private adapterService
    private ldapService
    private adapterObjectService
    private oauthService
    private componentService

    def setup() {
        def organisationBase = "ou=org,o=fint"
        def componentBase = "ou=comp,o=fint"
        ldapService = Mock(LdapService)
        adapterObjectService = new AdapterObjectService(organisationBase: organisationBase)
        oauthService = Mock(NamOAuthClientService)
        componentService = new ComponentService(
                componentBase: componentBase,
                ldapService: ldapService,
                componentObjectService: new ComponentObjectService(ldapService: ldapService))
        adapterService = new AdapterService(
                adapterObjectService: adapterObjectService,
                ldapService: ldapService,
                namOAuthClientService: oauthService,
                componentService: componentService
        )

    }

    def "Add Adapter"() {
        given:
        def adapter = ObjectFactory.newAdapter()

        when:
        def created = adapterService.addAdapter(adapter, new Organisation(name: "name"))

        then:
        created == true
        adapter.dn != null
        adapter.name != null
        1 * ldapService.createEntry(_ as Adapter) >> true
        1 * oauthService.addOAuthClient(_ as String) >> new OAuthClient()
    }

    def "Get Adapters"() {
        when:
        def adapters = adapterService.getAdapters(UUID.randomUUID().toString())

        then:
        adapters.size() == 2
        1 * ldapService.getAll(_ as String, _ as Class) >> Arrays.asList(ObjectFactory.newAdapter(), ObjectFactory.newAdapter())
        2 * oauthService.getOAuthClient(_ as String) >> ObjectFactory.newOAuthClient()
    }

    def "Get Adapter"() {
        when:
        def adapter = adapterService.getAdapter(UUID.randomUUID().toString(), UUID.randomUUID().toString())

        then:
        adapter.isPresent()
        1 * ldapService.getEntry(_ as String, _ as Class) >> ObjectFactory.newAdapter()
        1 * oauthService.getOAuthClient(_ as String) >> ObjectFactory.newOAuthClient()
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
        1 * ldapService.updateEntry(_ as Adapter)
    }

    def "Add component to adapter"() {
        given:
        def adapter = ObjectFactory.newAdapter()
        def component = ObjectFactory.newComponent()
        adapter.setDn("name=a1")
        component.setDn("ou=comp1")

        when:
        adapterService.linkComponent(adapter, component)

        then:
        adapter.getComponents().size() == 1
        1 * ldapService.updateEntry(_ as Adapter)
        1 * ldapService.updateEntry(_ as Component)
    }

    def "Remove component form adapter"() {
        given:
        def adapter = ObjectFactory.newAdapter()
        def comp1 = ObjectFactory.newComponent()
        def comp2 = ObjectFactory.newComponent()

        comp1.setDn("ou=comp1,o=fint")
        comp2.setDn("ou=comp2,o=fint")
        adapter.addComponent("ou=comp1,o=fint")
        adapter.addComponent("ou=comp2,o=fint")

        when:
        //adapterService.unLinkComponent(adapter, comp1)
        componentService.unLinkAdapter(comp1, adapter)

        then:
        adapter.getComponents().size() == 1
        adapter.getComponents().get(0) == "ou=comp2,o=fint"
        1 * ldapService.updateEntry(_ as Adapter)
        1 * ldapService.updateEntry(_ as Component)
    }
}
