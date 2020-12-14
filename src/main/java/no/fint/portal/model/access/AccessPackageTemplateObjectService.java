package no.fint.portal.model.access;

import no.fint.portal.utilities.LdapConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;

public class AccessPackageTemplateObjectService {
    private final String accessTemplateOrganisationBase;

    public AccessPackageTemplateObjectService(
            @Value("${fint.ldap.access.template.organisation-base}") String accessTemplateOrganisationBase
    ) {
        this.accessTemplateOrganisationBase = accessTemplateOrganisationBase;
    }

    public Name getAccessBase() {
        return LdapNameBuilder.newInstance(accessTemplateOrganisationBase)
                .build();
    }

    public String getAccessDn(String accessId) {
        return LdapNameBuilder.newInstance(getAccessBase())
                .add(LdapConstants.OU, accessId)
                .build()
                .toString();
    }
}

