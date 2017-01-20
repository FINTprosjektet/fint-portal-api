package no.fint.portal.component;

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

    @Value("${fint.ldap.component-base}")
    private String componentBase;

    public void setupClient(Client client, String compUuid, Organisation organisation) {
        client.setUuid(UUID.randomUUID().toString());
        client.setDn(
                LdapNameBuilder.newInstance(getClientBase(compUuid, organisation.getUuid()))
                        .add(LdapConstants.CN, client.getUuid())
                        .build()
        );
        client.setOrgId(organisation.getOrgId());
        client.setSecret(PasswordUtility.newPassword());
    }

    public Name getClientBase(String compUuid, String orgUuid) {
        return LdapNameBuilder.newInstance(componentBase)
                .add(LdapConstants.OU, compUuid)
                .add(LdapConstants.OU, orgUuid)
                .add(LdapConstants.OU, LdapConstants.CLIENT_CONTAINER_NAME)
                .build();
    }

    public String getClientDn(String clientUuid, String compUuid, String orgUuid) {
        return LdapNameBuilder.newInstance(getClientBase(compUuid, orgUuid))
                .add(LdapConstants.CN, clientUuid)
                .build()
                .toString();
    }
}
