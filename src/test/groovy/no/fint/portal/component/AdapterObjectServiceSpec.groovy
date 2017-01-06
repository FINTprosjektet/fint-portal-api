package no.fint.portal.component

import spock.lang.Specification

class AdapterObjectServiceSpec extends Specification {
    def adapterObjectService

    def setup() {
        adapterObjectService = new AdapterObjectService(componentBase: "ou=comp,o=fint")
    }

    def "Get Adapter Base"() {
        when:
        def dn = adapterObjectService.getAdapterBase("compUuid", "orgUuid")

        then:
        dn != null
        dn.toString().contains("compUuid") == true
        dn.toString().contains("orgUuid") == true
    }

    def "Get Adapter Dn"() {
        when:
        def dn = adapterObjectService.getAdapterDn("adapterUuid", "compUuid", "orgUuid")

        then:
        dn != null
        dn.contains("adapterUuid")
    }

    def "Setup Adapter"() {
        given:
        def adapter = new Adapter()


        when:
        adapterObjectService.setupAdapter(adapter, "compUuid", "orgUuid")

        then:
        adapter.password != null
        adapter.dn != null
        adapter.uuid != null
    }
}
