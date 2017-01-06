package no.fint.portal.component;

import no.fint.portal.utilities.LdapConstants;
import no.fint.portal.utilities.PasswordUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.util.UUID;

@Service
public class AdapterObjectService {

    @Value("${fint.ldap.component-base}")
    private String componentBase;

    public void setupAdapter(Adapter adapter, String compUuid, String orgUuid) {
        adapter.setUuid(UUID.randomUUID().toString());
        adapter.setDn(
                LdapNameBuilder.newInstance(getAdapterBase(compUuid, orgUuid))
                        .add(LdapConstants.CN, adapter.getUuid())
                        .build()
        );
        adapter.setPassword(PasswordUtility.newPassword());
    }

    public Name getAdapterBase(String compUuid, String orgUuid) {
        return LdapNameBuilder.newInstance(componentBase)
                .add(LdapConstants.OU, compUuid)
                .add(LdapConstants.OU, orgUuid)
                .add(LdapConstants.OU, LdapConstants.ADAPTER_CONTAINER_NAME)
                .build();
    }

    public String getAdapterDn(String adapterUuid, String compUuid, String orgUuid) {
        return LdapNameBuilder.newInstance(getAdapterBase(compUuid, orgUuid))
                .add(LdapConstants.CN, adapterUuid)
                .build()
                .toString();
    }
}
