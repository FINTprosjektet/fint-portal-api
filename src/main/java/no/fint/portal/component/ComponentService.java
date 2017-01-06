package no.fint.portal.component;

import lombok.extern.slf4j.Slf4j;
import no.fint.portal.ldap.LdapService;
import no.fint.portal.utilities.LdapConstants;
import no.fint.portal.utilities.PasswordUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ComponentService {

    @Autowired
    private LdapService ldapService;

    @Autowired
    private ComponentObjectService componentObjectService;

    @Autowired
    private OrganisationComponentService organisationComponentService;

    @Autowired
    private AdapterObjectService adapterObjectService;

    @Autowired
    private ClientObjectService clientObjectService;

    @Value("${fint.ldap.component-base}")
    private String componentBase;

    public boolean createComponent(Component component) {
        log.info("Creating component: {}", component);

        if (component.getDn() == null) {
            componentObjectService.setupComponent(component);
        }
        return ldapService.createEntry(component);
    }

    public boolean updateComponent(Component component) {
        log.info("Updating component: {}", component);

        return ldapService.updateEntry(component);
    }

    public List<Component> getComponents() {
        return ldapService.getAll(componentBase, Component.class);
    }

  /*
  public Optional<Component> getComponentByTechnicalName(String technicalName) {
    Optional<String> stringDnById = Optional.ofNullable(ldapService.getStringDnByUniqueName(technicalName, componentBase, Component.class));

    if (stringDnById.isPresent()) {
      return Optional.of(ldapService.getEntry(stringDnById.get(), Component.class));
    }
    return Optional.empty();
  }
  */

    public Optional<Component> getComponentByUUID(String uuid) {
        String dn = componentObjectService.getComponentDnByUUID(uuid);

        return Optional.ofNullable(ldapService.getEntry(dn, Component.class));
    }

    public void deleteComponent(Component component) {
        ldapService.deleteEntry(component);
    }

    public void addOrganisationToComponent(String componentUuid, String organistionUuid) {
        Container organisationContainer = new Container();
        Container clientContainer = new Container();
        Container adapterContainer = new Container();

        organisationContainer.setOu(organistionUuid);
        clientContainer.setOu(LdapConstants.CLIENT_CONTAINER_NAME);
        adapterContainer.setOu(LdapConstants.ADAPTER_CONTAINER_NAME);

        organisationComponentService.setOrganisationContainerDN(organisationContainer, componentUuid);
        ldapService.createEntry(organisationContainer);

        organisationComponentService.setClientContainerDN(clientContainer, organisationContainer);
        ldapService.createEntry(clientContainer);

        organisationComponentService.setAdapterContainerDN(adapterContainer, organisationContainer);
        ldapService.createEntry(adapterContainer);
    }

    public void removeOrganisationFromComponent(String componentUuid, String organistionUuid) {
        Container organisationContainer = new Container();
        Container clientContainer = new Container();
        Container adapterContainer = new Container();

        organisationContainer.setOu(organistionUuid);
        clientContainer.setOu(LdapConstants.CLIENT_CONTAINER_NAME);
        adapterContainer.setOu(LdapConstants.ADAPTER_CONTAINER_NAME);

        List<Client> clients = getClients(componentUuid, organistionUuid);
        if (clients != null) {
            clients.forEach(client -> ldapService.deleteEntry(client));
        }

        List<Adapter> adapters = getAdapters(componentUuid, organistionUuid);
        if (adapters != null) {
            adapters.forEach(adapter -> ldapService.deleteEntry(adapter));
        }

        organisationComponentService.setOrganisationContainerDN(organisationContainer, componentUuid);

        organisationComponentService.setClientContainerDN(clientContainer, organisationContainer);
        ldapService.deleteEntry(clientContainer);

        organisationComponentService.setAdapterContainerDN(adapterContainer, organisationContainer);
        ldapService.deleteEntry(adapterContainer);

        ldapService.deleteEntry(organisationContainer);
    }

    public boolean addClient(Client client, String compUuid, String orgUuid) {
        clientObjectService.setupClient(client, compUuid, orgUuid);
        return ldapService.createEntry(client);
    }

    public boolean addAdapter(Adapter adapter, String compUuid, String orgUuid) {
        adapterObjectService.setupAdapter(adapter, compUuid, orgUuid);
        return ldapService.createEntry(adapter);
    }

    public List<Client> getClients(String compUuid, String orgUuid) {
        return ldapService.getAll(clientObjectService.getClientBase(compUuid, orgUuid).toString(), Client.class);
    }

    public List<Adapter> getAdapters(String compUuid, String orgUuid) {
        return ldapService.getAll(adapterObjectService.getAdapterBase(compUuid, orgUuid).toString(), Adapter.class);
    }

    public Optional<Client> getClient(String clientUuid, String compUuid, String orgUuid) {
        return Optional.ofNullable(ldapService.getEntry(
                clientObjectService.getClientDn(clientUuid, compUuid, orgUuid),
                Client.class
        ));
    }

    public Optional<Adapter> getAdapter(String adapterUuid, String compUuid, String orgUuid) {
        return Optional.ofNullable(ldapService.getEntry(
                adapterObjectService.getAdapterDn(adapterUuid, compUuid, orgUuid),
                Adapter.class
        ));
    }

    public boolean updateClient(Client client) {
        return ldapService.updateEntry(client);
    }

    public boolean updateAdapter(Adapter adapter) {
        return ldapService.updateEntry(adapter);
    }

    public void deleteClient(Client client) {
        ldapService.deleteEntry(client);
    }

    public void deleteAdapter(Adapter adapter) {
        ldapService.deleteEntry(adapter);
    }

    public void resetClientPassword(Client client) {
        client.setPassword(PasswordUtility.newPassword());
        ldapService.updateEntry(client);
    }

    public void resetAdapterPassword(Adapter adapter) {
        adapter.setPassword(PasswordUtility.newPassword());
        ldapService.updateEntry(adapter);
    }
}
