package no.fint.portal.organisation;

import lombok.Getter;
import no.fint.portal.ldap.LdapService;
import no.fint.portal.utilities.LdapConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;

@Service
public class OrganisationObjectService {

    @Autowired
    LdapService ldapService;

    @Getter
    @Value("${fint.ldap.organisation-base}")
    private String organisationBase;

    public void setupOrganisation(Organisation organisation) {
        Organisation organisationFromLdap = ldapService.getEntryByUniqueName(organisation.getOrgId(), organisationBase, Organisation.class);
        //ObjectUtility.setupUuidContainerObject(organisation, organisationFromLdap, organisationBase);


        if (organisationFromLdap == null) {
            Name dn = LdapNameBuilder.newInstance(organisationBase)
                    .add(LdapConstants.OU, organisation.getOrgId().replace(".", "_"))
                    .build();
            organisation.setDn(dn);
        } else {
            organisation.setDn(LdapNameBuilder.newInstance(organisationFromLdap.getDn()).build());
        }
    }
}
