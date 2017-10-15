package no.fint.portal.client;

import no.fint.portal.organisation.Organisation;
import no.fint.portal.utilities.LdapConstants;
import no.fint.portal.utilities.PasswordUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.util.UUID;

@Service
public class ClientObjectService {

    @Value("${fint.ldap.organisation-base}")
    private String organisationBase;

    public void setupClient(Client client, Organisation organisation) {
        client.setUuid(UUID.randomUUID().toString());
        client.setDn(
                LdapNameBuilder.newInstance(getClientBase(organisation.getUuid()))
                        .add(LdapConstants.CN, client.getUuid())
                        .build()
        );
        client.setOrgId(organisation.getOrgId());
        client.setSecret(PasswordUtility.generateSecret());
    }

    public Name getClientBase(String orgUuid) {
        return LdapNameBuilder.newInstance(organisationBase)
                .add(LdapConstants.OU, orgUuid)
                .add(LdapConstants.OU, LdapConstants.CLIENT_CONTAINER_NAME)
                .build();
    }

    public String getClientDn(String clientUuid, String orgUuid) {
        return LdapNameBuilder.newInstance(getClientBase(orgUuid))
                .add(LdapConstants.CN, clientUuid)
                .build()
                .toString();
    }
}
