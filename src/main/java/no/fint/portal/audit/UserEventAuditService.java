package no.fint.portal.audit;

import no.fint.portal.ldap.BasicLdapEntry;
import no.fint.portal.model.adapter.Adapter;
import no.fint.portal.model.client.Client;
import no.fint.portal.model.contact.ContactService;
import no.fint.portal.model.organisation.Organisation;
import no.fint.portal.model.organisation.OrganisationService;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.ldap.LdapName;

@Service
public class UserEventAuditService {

    public static final int ORGANISATION_NAME_DN_POSITION = 2;

    private final UserEventRepository userEventRepository;
    private final ContactService contactService;
    private final OrganisationService organisationService;

    public UserEventAuditService(UserEventRepository userEventRepository, ContactService contactService, OrganisationService organisationService) {
        this.userEventRepository = userEventRepository;
        this.contactService = contactService;
        this.organisationService = organisationService;
    }

    private void audit(Organisation organisation, String nin, UserEventOperation operation, String type, Object object) {
        userEventRepository.save(
                UserEvent
                        .builder()
                        .organisation(organisation)
                        .user(contactService.getContact(nin).orElse(null))
                        .operation(operation)
                        .type(type)
                        .object(object)
                        .build()
        );
    }

    public <T> void audit(String organisationName, String nin, UserEventOperation operation, Class<T> clazz) {
        audit(organisationService.getOrganisation(organisationName).orElse(null), nin, operation, clazz.getCanonicalName(), null);
    }

    public <T> void audit(Organisation organisation, String nin, UserEventOperation operation, Class<T> clazz) {
        audit(organisation, nin, operation, clazz.getCanonicalName(), null);
    }

    public void audit(Organisation organisation, String nin, UserEventOperation operation, Object object) {
        audit(organisation, nin, operation, object.getClass().getCanonicalName(), clearSecrets(object));
    }

    public void audit(String nin, UserEventOperation operation, BasicLdapEntry object) {

        audit(
                organisationService.getOrganisation(getOrganisationNameFromObject(object)).orElse(null),
                nin,
                operation,
                object.getClass().getCanonicalName(),
                clearSecrets(object)
        );
    }

    public String getOrganisationNameFromObject(BasicLdapEntry object) {
        LdapName ldapNameBuilder = LdapNameBuilder.newInstance(object.getDn()).build();
        return ldapNameBuilder.getRdn(ORGANISATION_NAME_DN_POSITION).getValue().toString();
    }

    private Object clearSecrets(Object object) {
        if (object instanceof Adapter) {
            ((Adapter) object).setSecret(null);
        }
        if (object instanceof Client) {
            ((Client) object).setSecret(null);
        }

        return object;
    }


}
