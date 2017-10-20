package no.fint.portal.contact;

import no.fint.portal.organisation.Organisation;
import no.fint.portal.utilities.LdapConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;


@Service
public class ContactObjectService {

    @Value("${fint.ldap.organisation-base}")
    private String organisationBase;

    public void setupContact(Contact contact, Organisation organisation) {
        contact.setDn(getContactDn(organisation.getName(), contact.getNin()));
        contact.setOrgId(organisation.getOrgId());
    }

    public Name getContactBase(String orgUuid) {
        return LdapNameBuilder.newInstance(organisationBase)
                .add(LdapConstants.OU, orgUuid)
                .build();
    }

    public String getContactDn(String orgUuid, String nin) {
        return LdapNameBuilder.newInstance(getContactBase(orgUuid))
                .add(LdapConstants.CN, nin)
                .build().toString();
    }

}
