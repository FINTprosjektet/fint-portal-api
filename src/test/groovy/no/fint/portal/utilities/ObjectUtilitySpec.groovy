package no.fint.portal.utilities

import no.fint.portal.organisation.Organisation
import spock.lang.Specification


class ObjectUtilitySpec extends Specification {

    def "Setup Uuid Container Object"() {
        given:
        def organisation = new Organisation()
        def uuid = UUID.randomUUID().toString()
        def dn = String.format("ou=%s,o=fint", uuid)
        def orgFromLdap = new Organisation(uuid: uuid, dn: dn)
        def newOrg = new Organisation()

        when:
        ObjectUtility.setupUuidContainerObject(organisation, null, "o=fint")
        ObjectUtility.setupUuidContainerObject(newOrg, orgFromLdap, "o=fint")

        then:
        organisation.uuid.length() == 36
        organisation.dn.contains(organisation.uuid)
        newOrg.uuid == uuid
        newOrg.dn == dn
    }
}
