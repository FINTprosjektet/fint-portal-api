package no.fint.portal.adapter

import no.fint.portal.organisation.Organisation
import spock.lang.Specification

class AdapterObjectServiceSpec extends Specification {
    def adapterObjectService

    def setup() {
        adapterObjectService = new AdapterObjectService(organisationBase: "ou=comp,o=fint")
    }

    def "Get Adapter Base"() {
        when:
        def dn = adapterObjectService.getAdapterBase("orgUuid")

        then:
        dn != null
        dn.toString().contains("orgUuid") == true
    }

    def "Get Adapter Dn"() {
        when:
        def dn = adapterObjectService.getAdapterDn("adapterUuid", "orgUuid")

        then:
        dn != null
        dn.contains("adapterUuid")
    }

    def "Setup Adapter"() {
        given:
        def adapter = new Adapter(name: "TestAdapter")

        when:
        adapterObjectService.setupAdapter(adapter, new Organisation(orgId: "test.no", name: "name"))

        then:
        adapter.secret != null
        adapter.dn != null
        adapter.name != null
    }
}
