package no.fint.portal.organisation;

import lombok.extern.slf4j.Slf4j;
import no.fint.portal.ldap.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
@Slf4j
@Service
public class OrganisationService {

    @Autowired
    OrganisationObjectService organisationObjectService;

    //@Autowired
    //private ObjectService objectService;
    @Autowired
    ContactObjectService contactObjectService;
    @Autowired
    private LdapService ldapService;
    @Value("${fint.ldap.organisation-base}")
    private String organisationBase;

    public boolean createOrganisation(Organisation organisation) {
        log.info("Creating organisation: {}", organisation);

        if (organisation.getDn() == null) {
            organisationObjectService.setupOrganisation(organisation);
        }
        return ldapService.createEntry(organisation);
    }

    public boolean updateOrganisation(Organisation organisation) {
        return ldapService.updateEntry(organisation);
    }

    public List<Organisation> getOrganisations() {
        return ldapService.getAll(organisationBase, Organisation.class);
    }

    public Optional<Organisation> getOrganisationByOrgId(String orgId) {
        Optional<String> stringDnById = Optional.ofNullable(ldapService.getStringDnByUniqueName(orgId, organisationBase, Organisation.class));

        if (stringDnById.isPresent()) {
            return Optional.of(ldapService.getEntry(stringDnById.get(), Organisation.class));
        }
        return Optional.empty();
    }

    public List<Contact> getContacts(String uuid) {
        return ldapService.getAll(organisationObjectService.getOrganisationDnByUUID(uuid), Contact.class);
    }

    public boolean addContact(Contact contact, String orgUUID) {
        log.info("Creating contact: {}", contact);

        if (contact.getDn() == null) {
            contactObjectService.setContactDn(contact, orgUUID);
        }
        contact.setOrgId(getOrganisationId(orgUUID));
        return ldapService.createEntry(contact);
    }

    public Optional<Contact> getContact(String orgUUID, String nin) {

        return Optional.ofNullable(ldapService.getEntry(contactObjectService.getContactDn(orgUUID, nin), Contact.class));
    }

    public boolean updateContact(Contact contact) {
        return ldapService.updateEntry(contact);
    }

    public void deleteContact(Contact contact) {
        ldapService.deleteEntry(contact);
    }

    public void deleteOrganisation(Organisation organisation) {
        List<Contact> contacts = getContacts(organisation.getUuid());

        if (contacts != null) {
            contacts.forEach(contact -> ldapService.deleteEntry(contact));
        }

        ldapService.deleteEntry(organisation);
    }

    private String getOrganisationId(String uuid) {
        String dn = organisationObjectService.getOrganisationDnByUUID(uuid);
        Organisation organisation = ldapService.getEntry(dn, Organisation.class);

        return organisation.getOrgId();

    }

    public Optional<Organisation> getOrganisationByUUID(String uuid) {
        String dn = organisationObjectService.getOrganisationDnByUUID(uuid);

        return Optional.ofNullable(ldapService.getEntry(dn, Organisation.class));

    }
}
