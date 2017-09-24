package no.fint.portal.adapter;

import lombok.extern.slf4j.Slf4j;
import no.fint.portal.ldap.LdapService;
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

    public boolean addAdapter(Adapter adapter, Organisation organisation) {
        adapterObjectService.setupAdapter(adapter, organisation);
        return ldapService.createEntry(adapter);
    }

    public List<Adapter> getAdapters(String orgUuid) {
        return ldapService.getAll(adapterObjectService.getAdapterBase(orgUuid).toString(), Adapter.class);
    }

    public Optional<Adapter> getAdapter(String adapterUuid, String orgUuid) {
        return Optional.ofNullable(ldapService.getEntry(
                adapterObjectService.getAdapterDn(adapterUuid, orgUuid),
                Adapter.class
        ));
    }

    public boolean updateAdapter(Adapter adapter) {
        return ldapService.updateEntry(adapter);
    }

    public void deleteAdapter(Adapter adapter) {
        ldapService.deleteEntry(adapter);
    }

    public void resetAdapterPassword(Adapter adapter) {
        adapter.setSecret(PasswordUtility.generateSecret());
        ldapService.updateEntry(adapter);
    }
}
