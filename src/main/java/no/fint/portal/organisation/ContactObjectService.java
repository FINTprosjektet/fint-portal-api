package no.fint.portal.organisation;

import no.fint.portal.utilities.LdapConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

@Service
public class ContactObjectService {
    @Autowired
    OrganisationObjectService organisationObjectService;

    public void setContactDn(Contact contact, String orgUUID) {
        contact.setDn(getContactDn(orgUUID, contact.getNin()));
    }

    public String getContactDn(String orgUUID, String nin) {
        return LdapNameBuilder.newInstance(organisationObjectService.getOrganisationDnByUUID(orgUUID))
                .add(LdapConstants.CN, nin)
                .build().toString();
    }
}
