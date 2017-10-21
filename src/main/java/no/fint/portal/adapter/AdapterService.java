package no.fint.portal.adapter;

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
public class AdapterService {

    @Autowired
    AdapterObjectService adapterObjectService;

    @Autowired
    LdapService ldapService;

    @Autowired
    NamOAuthClientService namOAuthClientService;

    @Autowired
    ComponentService componentService;

    public boolean addAdapter(Adapter adapter, Organisation organisation) {
        adapterObjectService.setupAdapter(adapter, organisation);

        OAuthClient oAuthClient = namOAuthClientService.addOAuthClient(String.format("A_%s_%s", organisation.getOrgId().replace(".", "_"), adapter.getName()));

        adapter.setClientId(oAuthClient.getClientId());
        adapter.setClientSecret(oAuthClient.getClientSecret());

        return ldapService.createEntry(adapter);
    }

    public List<Adapter> getAdapters(String orgUuid) {
        List<Adapter> adapters = ldapService.getAll(adapterObjectService.getAdapterBase(orgUuid).toString(), Adapter.class);

        adapters.forEach(adapter -> {
            OAuthClient oAuthClient = namOAuthClientService.getOAuthClient(adapter.getClientId());
            adapter.setClientSecret(oAuthClient.getClientSecret());
        });

        return adapters;
    }

    public Optional<Adapter> getAdapter(String adapterUuid, String orgUuid) {
        Optional<Adapter> adapter = Optional.ofNullable(ldapService.getEntry(
                adapterObjectService.getAdapterDn(adapterUuid, orgUuid),
                Adapter.class
        ));

        OAuthClient oAuthClient = namOAuthClientService.getOAuthClient(adapter.get().getClientId());

        adapter.get().setClientSecret(oAuthClient.getClientSecret());

        return adapter;
    }

    public boolean updateAdapter(Adapter adapter) {
        return ldapService.updateEntry(adapter);
    }

    public void deleteAdapter(Adapter adapter) {
        namOAuthClientService.removeOAuthClient(adapter.getClientId());
        ldapService.deleteEntry(adapter);
    }

    public void resetAdapterPassword(Adapter adapter) {
        adapter.setSecret(PasswordUtility.generateSecret());
        ldapService.updateEntry(adapter);
    }

    public void linkComponent(Adapter adapter, Component component) {

        adapter.addComponent(component.getDn());
        ldapService.updateEntry(adapter);

        componentService.linkAdapter(component, adapter);

    }

    public void unLinkComponent(Adapter adapter, Component component) {

        adapter.removeComponent(component.getDn());
        ldapService.updateEntry(adapter);

        componentService.unLinkAdapter(component, adapter);

    }
}
