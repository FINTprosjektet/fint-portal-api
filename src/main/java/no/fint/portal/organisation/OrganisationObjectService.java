package no.fint.portal.organisation;

import lombok.Getter;
import no.fint.portal.ldap.LdapService;
import no.fint.portal.utilities.LdapConstants;
import no.fint.portal.utilities.ObjectUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

@Service
public class OrganisationObjectService {

    @Autowired
    LdapService ldapService;

    @Getter
    @Value("${fint.ldap.organisation-base}")
    private String organisationBase;

    public void setupOrganisation(Organisation organisation) {
        Organisation organisationFromLdap = ldapService.getEntryByUniqueName(organisation.getOrgId(), organisationBase, Organisation.class);
        ObjectUtility.setupUuidContainerObject(organisation, organisationFromLdap, organisationBase);
    }
}
