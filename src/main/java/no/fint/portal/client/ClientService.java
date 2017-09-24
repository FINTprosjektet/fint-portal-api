package no.fint.portal.client;

import lombok.extern.slf4j.Slf4j;
import no.fint.portal.ldap.LdapService;
import no.fint.portal.oauth.NamOAuthClientService;
import no.fint.portal.oauth.OAuthClient;
import no.fint.portal.organisation.Organisation;
import no.fint.portal.utilities.PasswordUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClientService {

    @Autowired
    ClientObjectService clientObjectService;

    @Autowired
    LdapService ldapService;

    @Autowired
    NamOAuthClientService namOAuthClientService;

    public boolean addClient(Client client, Organisation organisation) {
        clientObjectService.setupClient(client, organisation);

        OAuthClient oAuthClient = namOAuthClientService.addOAuthClient(client.getUuid());

        client.setClientId(oAuthClient.getClientId());
        client.setClientSecret(oAuthClient.getClientSecret());

        return ldapService.createEntry(client);
    }

    public List<Client> getClients(String orgUuid) {
        return ldapService.getAll(clientObjectService.getClientBase(orgUuid).toString(), Client.class);
    }

    public Optional<Client> getClient(String clientUuid, String orgUuid) {
        return Optional.ofNullable(ldapService.getEntry(
                clientObjectService.getClientDn(clientUuid, orgUuid),
                Client.class
        ));
    }

    public boolean updateClient(Client client) {
        return ldapService.updateEntry(client);
    }

    public void deleteClient(Client client) {
        ldapService.deleteEntry(client);
    }

    public void resetClientPassword(Client client) {
        client.setSecret(PasswordUtility.generateSecret());
        ldapService.updateEntry(client);
    }

}
