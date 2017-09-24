package no.fint.portal.contact;

import lombok.extern.slf4j.Slf4j;
import no.fint.portal.ldap.LdapService;
import no.fint.portal.organisation.Organisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContactService {

    @Autowired
    private LdapService ldapService;

    @Autowired
    private ContactObjectService contactObjectService;

    public List<Contact> getContacts(String orgUuid) {
        return ldapService.getAll(contactObjectService.getContactBase(orgUuid).toString(), Contact.class);
    }

    public boolean addContact(Contact contact, Organisation organisation) {
        log.info("Creating contact: {}", contact);

        contactObjectService.setupContact(contact, organisation);
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

}
