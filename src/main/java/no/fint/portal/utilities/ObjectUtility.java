package no.fint.portal.utilities;

import no.fint.portal.ldap.UuidLdapEntry;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;
import java.util.UUID;

public enum ObjectUtility {
    ;

    public static void setupUuidContainerObject(UuidLdapEntry uuidLdapEntry, UuidLdapEntry uuidEntryFromLdap, String base) {
        Name dn;
        String uuid = UUID.randomUUID().toString();

        if (uuidEntryFromLdap == null) {
            dn = LdapNameBuilder.newInstance(base)
                    .add(LdapConstants.OU, uuid)
                    .build();
            uuidLdapEntry.setUuid(uuid);
            uuidLdapEntry.setDn(dn);
        } else {
            uuidLdapEntry.setDn(LdapNameBuilder.newInstance(uuidEntryFromLdap.getDn()).build());
            uuidLdapEntry.setUuid(uuidEntryFromLdap.getUuid());
        }
    }
}
