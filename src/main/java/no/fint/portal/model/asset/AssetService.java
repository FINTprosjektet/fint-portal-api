package no.fint.portal.model.asset;

import no.fint.portal.ldap.LdapService;
import no.fint.portal.model.adapter.Adapter;
import no.fint.portal.model.client.Client;
import no.fint.portal.model.organisation.Organisation;
import no.fint.portal.utilities.LdapConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    @Autowired
    LdapService ldapService;

    public boolean addAsset(Asset asset, Organisation organisation) {
        asset.setName(asset.getAssetId().replace(".", "_"));
        asset.setDn(
                LdapNameBuilder.newInstance(organisation.getDn())
                        .add(LdapConstants.OU, LdapConstants.ASSET_CONTAINER_NAME)
                        .add(LdapConstants.OU, asset.getName())
                        .build()
        );
        asset.setOrganisation(organisation.getDn());

        return ldapService.createEntry(asset);
    }

    public void removeAsset(Asset asset) {
        ldapService.deleteEntry(asset);
    }

    public boolean updateAsset(Asset asset) {
        return ldapService.updateEntry(asset);
    }

    public void linkClientToAsset(Asset asset, Client client) {

        asset.addClient(client.getDn());
        client.setAssetId(asset.getAssetId());

        ldapService.updateEntry(asset);
        ldapService.updateEntry(client);
    }

    public void unlinkClientFromAsset(Asset asset, Client client) {

        asset.removeClient(client.getDn());
        client.setAssetId(null);

        ldapService.updateEntry(asset);
        ldapService.updateEntry(client);
    }

    public void linkAdapterToAsset(Asset asset, Adapter adapter) {

        asset.addAdapter(adapter.getDn());
        adapter.addAsset(asset.getAssetId());

        ldapService.updateEntry(asset);
        ldapService.updateEntry(adapter);
    }

    public void unlinkAdapterFromAsset(Asset asset, Adapter adapter) {

        asset.removeAdapter(adapter.getDn());
        adapter.removeAsset(asset.getAssetId());

        ldapService.updateEntry(asset);
        ldapService.updateEntry(adapter);
    }

    public List<Asset> getAssets(Organisation organisation) {

        return ldapService.getAll(LdapNameBuilder.newInstance(organisation.getDn())
                        .add(LdapConstants.OU, LdapConstants.ASSET_CONTAINER_NAME)
                        .build().toString(),
                Asset.class);
    }
}
