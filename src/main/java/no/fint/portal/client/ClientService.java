package no.fint.portal.client;

import lombok.extern.slf4j.Slf4j;
import no.fint.portal.component.Component;
import no.fint.portal.component.ComponentService;
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

    @Autowired
    ComponentService componentService;

    public boolean addClient(Client client, Organisation organisation) {
        clientObjectService.setupClient(client, organisation);

        OAuthClient oAuthClient = namOAuthClientService.addOAuthClient(String.format("C__%s", client.getUuid()));

        client.setClientId(oAuthClient.getClientId());
        client.setClientSecret(oAuthClient.getClientSecret());

        return ldapService.createEntry(client);
    }

    public List<Client> getClients(String orgUuid) {
        List<Client> clients = ldapService.getAll(clientObjectService.getClientBase(orgUuid).toString(), Client.class);

        clients.forEach(client -> {
            OAuthClient oAuthClient = namOAuthClientService.getOAuthClient(client.getClientId());
            client.setClientSecret(oAuthClient.getClientSecret());
        });

        return clients;
    }

    public Optional<Client> getClient(String clientUuid, String orgUuid) {
        Optional<Client> client = Optional.ofNullable(ldapService.getEntry(
                clientObjectService.getClientDn(clientUuid, orgUuid),
                Client.class
        ));

        OAuthClient oAuthClient = namOAuthClientService.getOAuthClient(client.get().getClientId());

        client.get().setClientSecret(oAuthClient.getClientSecret());

        return client;
    }

    public boolean updateClient(Client client) {
        return ldapService.updateEntry(client);
    }

    public void deleteClient(Client client) {
        namOAuthClientService.removeOAuthClient(client.getClientId());
        ldapService.deleteEntry(client);
    }

    public void resetClientPassword(Client client) {
        client.setSecret(PasswordUtility.generateSecret());
        ldapService.updateEntry(client);
    }

    public void linkComponent(Client client, Component component) {

        client.addComponent(component.getDn());
        ldapService.updateEntry(client);

        componentService.linkClient(component, client);

    }

    public void unLinkComponent(Client client, Component component) {

        client.removeComponent(component.getDn());
        ldapService.updateEntry(client);

        componentService.unLinkClient(component, client);

    }

}
