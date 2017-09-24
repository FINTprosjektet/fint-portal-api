package no.fint.portal.organisation;

import lombok.extern.slf4j.Slf4j;
import no.fint.portal.adapter.Adapter;
import no.fint.portal.adapter.AdapterService;
import no.fint.portal.client.Client;
import no.fint.portal.client.ClientService;
import no.fint.portal.contact.Contact;
import no.fint.portal.contact.ContactService;
import no.fint.portal.ldap.LdapService;
import no.fint.portal.model.Container;
import no.fint.portal.utilities.LdapConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
@Slf4j
@Service
public class OrganisationService {

    @Autowired
    private OrganisationObjectService organisationObjectService;

    @Autowired
    private LdapService ldapService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private AdapterService adapterService;

    @Autowired
    private ClientService clientService;

    @Value("${fint.ldap.organisation-base}")
    private String organisationBase;

    public boolean createOrganisation(Organisation organisation) {
        log.info("Creating organisation: {}", organisation);

        if (organisation.getDn() == null) {
            organisationObjectService.setupOrganisation(organisation);
        }

        boolean createdOrganisation = ldapService.createEntry(organisation);

        createClientContainer(organisation.getDn());
        createAdapterContainer(organisation.getDn());

        return createdOrganisation;
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

    public void deleteOrganisation(Organisation organisation) {

        removeAllContacts(organisation.getUuid());
        removeAllAdapters(organisation.getUuid());
        removeAllClients(organisation.getUuid());
        removeAdapterContainer(organisation.getDn());
        removeClientContainer(organisation.getDn());

        ldapService.deleteEntry(organisation);
    }

    private void removeAllContacts(String organisationUuid) {
        List<Contact> contacts = contactService.getContacts(organisationUuid);

        if (contacts != null) {
            contacts.forEach(contact -> ldapService.deleteEntry(contact));
        }
    }

    private void removeAllAdapters(String organisationUuid) {
        List<Adapter> adapters = adapterService.getAdapters(organisationUuid);

        if (adapters != null) {
            adapters.forEach(adapter -> ldapService.deleteEntry(adapter));
        }
    }

    private void removeAllClients(String organisationUuid) {
        List<Client> clients = clientService.getClients(organisationUuid);

        if (clients != null) {
            clients.forEach(client -> ldapService.deleteEntry(client));
        }
    }

    private void removeClientContainer(String organisationDn) {
        Container clientContainer = new Container();

        clientContainer.setOu(LdapConstants.CLIENT_CONTAINER_NAME);
        clientContainer.setDn(LdapNameBuilder.newInstance(organisationDn)
                .add(LdapConstants.OU, LdapConstants.CLIENT_CONTAINER_NAME)
                .build());


        ldapService.deleteEntry(clientContainer);

    }

    private void removeAdapterContainer(String organisationDn) {
        Container adapterContainer = new Container();

        adapterContainer.setOu(LdapConstants.ADAPTER_CONTAINER_NAME);
        adapterContainer.setDn(LdapNameBuilder.newInstance(organisationDn)
                .add(LdapConstants.OU, LdapConstants.ADAPTER_CONTAINER_NAME)
                .build());
        ldapService.deleteEntry(adapterContainer);
    }

    public String getOrganisationId(String uuid) {
        String dn = organisationObjectService.getOrganisationDnByUUID(uuid);
        Organisation organisation = ldapService.getEntry(dn, Organisation.class);

        return organisation.getOrgId();

    }

    public Optional<Organisation> getOrganisationByUUID(String uuid) {
        String dn = organisationObjectService.getOrganisationDnByUUID(uuid);

        return Optional.ofNullable(ldapService.getEntry(dn, Organisation.class));

    }

    private void createClientContainer(String organisationDn) {
        Container clientContainer = new Container();

        clientContainer.setOu(LdapConstants.CLIENT_CONTAINER_NAME);
        clientContainer.setDn(LdapNameBuilder.newInstance(organisationDn)
                .add(LdapConstants.OU, LdapConstants.CLIENT_CONTAINER_NAME)
                .build());


        ldapService.createEntry(clientContainer);

    }

    private void createAdapterContainer(String organisationDn) {
        Container adapterContainer = new Container();

        adapterContainer.setOu(LdapConstants.ADAPTER_CONTAINER_NAME);
        adapterContainer.setDn(LdapNameBuilder.newInstance(organisationDn)
                .add(LdapConstants.OU, LdapConstants.ADAPTER_CONTAINER_NAME)
                .build());
        ldapService.createEntry(adapterContainer);
    }
}
