package no.fint.portal.model.access;

import no.fint.portal.ldap.LdapService;
import no.fint.portal.model.client.Client;
import no.fint.portal.model.organisation.Organisation;
import no.fint.portal.utilities.LdapConstants;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessService {

    private final LdapService ldapService;
    private final AccessObjectService accessObjectService;

    public AccessService(LdapService ldapService, AccessObjectService accessObjectService) {
        this.ldapService = ldapService;
        this.accessObjectService = accessObjectService;
    }

    public List<AccessPackage> getAccesses(String orgName) {
        return ldapService.getAll(accessObjectService.getAccessBase(orgName).toString(), AccessPackage.class);
    }

    public boolean addAccess(AccessPackage accessPackage, Organisation organisation) {
        accessPackage.setDn(
                LdapNameBuilder.newInstance(organisation.getDn())
                .add(LdapConstants.OU, "access")
                .add(LdapConstants.OU, accessPackage.getName())
                .build()
        );

        return ldapService.createEntry(accessPackage);
    }

    public boolean updateAccess(AccessPackage accessPackage) {
        return ldapService.updateEntry(accessPackage);
    }

    public void removeAccess(AccessPackage accessPackage) {
        ldapService.deleteEntry(accessPackage);
    }

    public void linkClientToAccess(AccessPackage accessPackage, Client client) {
        accessPackage.addClient(client.getDn());
        client.setAccessPackage(accessPackage.getDn());
        ldapService.updateEntry(accessPackage);
        ldapService.updateEntry(client);
    }

    public void unlinkClientFromAccess(AccessPackage accessPackage, Client client) {
        accessPackage.removeClient(client.getDn());
        client.getAccessPackages().clear();
        ldapService.updateEntry(accessPackage);
        ldapService.updateEntry(client);
    }

    public AccessPackage getAccess(String accessId, String orgName) {
        return getAccessByDn(accessObjectService.getAccessDn(accessId, orgName));
    }

    private AccessPackage getAccessByDn(String accessDn) {
        return ldapService.getEntry(accessDn, AccessPackage.class);
    }
}
