package no.fint.portal.model.contact;

import lombok.extern.slf4j.Slf4j;
import no.fint.portal.ldap.LdapService;
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

    public List<Contact> getContacts() {
        return ldapService.getAll(contactObjectService.getContactBase().toString(), Contact.class);
    }

    public boolean addContact(Contact contact) {
        log.info("Creating contact: {}", contact);

        contactObjectService.setupContact(contact);
        return ldapService.createEntry(contact);
    }

    public Optional<Contact> getContact(String nin) {

        return Optional.ofNullable(ldapService.getEntry(contactObjectService.getContactDn(nin), Contact.class));
    }

    public boolean updateContact(Contact contact) {
        return ldapService.updateEntry(contact);
    }

    public void deleteContact(Contact contact) {
        ldapService.deleteEntry(contact);
    }

}
