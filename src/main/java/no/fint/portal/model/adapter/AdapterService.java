package no.fint.portal.model.adapter;

import lombok.extern.slf4j.Slf4j;
import no.fint.portal.audit.UserEventAuditService;
import no.fint.portal.audit.UserEventOperation;
import no.fint.portal.ldap.LdapService;
import no.fint.portal.model.asset.Asset;
import no.fint.portal.model.asset.AssetService;
import no.fint.portal.model.organisation.Organisation;
import no.fint.portal.oauth.NamOAuthClientService;
import no.fint.portal.oauth.OAuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdapterService {

    @Autowired
    private AdapterObjectService adapterObjectService;

    @Autowired
    private LdapService ldapService;

    @Autowired
    private NamOAuthClientService namOAuthClientService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserEventAuditService userEventAuditService;

    public boolean addAdapter(Adapter adapter, Organisation organisation, String nin) {
        adapterObjectService.setupAdapter(adapter, organisation);

        OAuthClient oAuthClient = namOAuthClientService.addOAuthClient(
                String.format("a_%s", adapter.getName()
                        .replace("@", "_")
                        .replace(".", "_")
                )
        );

        adapter.setClientId(oAuthClient.getClientId());

        boolean created = ldapService.createEntry(adapter);
        if (created) {
            Asset primaryAsset = assetService.getPrimaryAsset(organisation);
            assetService.linkAdapterToAsset(primaryAsset, adapter);
        }

        userEventAuditService.audit(organisation, nin, UserEventOperation.ADD, adapter);

        return created;
    }

    public List<Adapter> getAdapters(String orgName) {
        return ldapService.getAll(adapterObjectService.getAdapterBase(orgName).toString(), Adapter.class);
    }

    public Optional<Adapter> getAdapter(String adapterName, String orgName) {

        return Optional.ofNullable(ldapService.getEntry(
                adapterObjectService.getAdapterDn(adapterName, orgName),
                Adapter.class
        ));

    }

    public Optional<Adapter> getAdapterByDn(String dn) {
        return Optional.ofNullable(ldapService.getEntry(dn, Adapter.class));
    }

    public String getAdapterSecret(Adapter adapter, String nin) {
        userEventAuditService.audit(nin, UserEventOperation.GET_SECRET, adapter);

        return namOAuthClientService.getOAuthClient(adapter.getClientId()).getClientSecret();
    }

    public boolean updateAdapter(Adapter adapter, String nin) {
        userEventAuditService.audit(nin, UserEventOperation.UPDATE, adapter);

        return ldapService.updateEntry(adapter);
    }

    public void deleteAdapter(Adapter adapter, String nin) {
        userEventAuditService.audit(nin, UserEventOperation.DELETE, adapter);
        namOAuthClientService.removeOAuthClient(adapter.getClientId());
        ldapService.deleteEntry(adapter);

    }

    public void resetAdapterPassword(Adapter adapter, String newPassword, String nin) {
        userEventAuditService.audit(nin, UserEventOperation.RESET_PASSWORD, adapter);
        adapter.setSecret(newPassword);
        ldapService.updateEntry(adapter);
    }

}
