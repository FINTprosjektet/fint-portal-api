package no.fint.portal.adapter;

import no.fint.portal.organisation.Organisation;
import no.fint.portal.utilities.LdapConstants;
import no.fint.portal.utilities.PasswordUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.util.UUID;

@Service
public class AdapterObjectService {

    @Value("${fint.ldap.organisation-base}")
    private String organisationBase;

    public void setupAdapter(Adapter adapter, Organisation organisation) {
        adapter.setUuid(UUID.randomUUID().toString());
        adapter.setDn(
                LdapNameBuilder.newInstance(getAdapterBase(organisation.getUuid()))
                        .add(LdapConstants.CN, adapter.getUuid())
                        .build()
        );
        adapter.setOrgId(organisation.getOrgId());
        adapter.setSecret(PasswordUtility.generateSecret());
    }

    public Name getAdapterBase(String orgUuid) {
        return LdapNameBuilder.newInstance(organisationBase)
                .add(LdapConstants.OU, orgUuid)
                .add(LdapConstants.OU, LdapConstants.ADAPTER_CONTAINER_NAME)
                .build();
    }

    public String getAdapterDn(String adapterUuid, String orgUuid) {
        return LdapNameBuilder.newInstance(getAdapterBase(orgUuid))
                .add(LdapConstants.CN, adapterUuid)
                .build()
                .toString();
    }
}