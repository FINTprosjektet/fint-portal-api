package no.fint.portal.model.component;

import lombok.extern.slf4j.Slf4j;
import no.fint.portal.ldap.LdapService;
import no.fint.portal.model.adapter.Adapter;
import no.fint.portal.model.client.Client;
import no.fint.portal.model.organisation.Organisation;
import no.fint.portal.utilities.LdapConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ComponentService {

    @Autowired
    private LdapService ldapService;

    @Autowired
    private ComponentObjectService componentObjectService;

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

    // TODO: 17.01.2017 Refactor this method and add tests
    public List<Component> getComponentsByOrgUUID(String uuid) {
        List<Component> components = getComponents();
        List<Component> orgComponents = new ArrayList<>();
        components.forEach(component -> {
            String dn = component.getDn();
            String orgDn = String.format("ou=%s,%s", uuid, dn);
            if (ldapService.entryExists(orgDn)) {
                orgComponents.add(component);
            }
        });
        return orgComponents;
    }

    public Optional<Component> getComponentByName(String name) {
        String dn = getComponentDnByName(name);

        return Optional.ofNullable(ldapService.getEntry(dn, Component.class));
    }

    public void deleteComponent(Component component) {
        ldapService.deleteEntry(component);
    }

    public String getComponentDnByName(String name) {
        if (name != null) {
            return LdapNameBuilder.newInstance(componentBase)
                    .add(LdapConstants.OU, name)
                    .build().toString();
        }
        return null;
    }

    public void linkOrganisation(Component component, Organisation organisation) {


        component.addOrganisation(organisation.getDn());

        ldapService.updateEntry(component);
    }

    public void unLinkOrganisation(Component component, Organisation organisation) {

        component.removeOrganisation(organisation.getDn());

        ldapService.updateEntry(component);
    }

    public void linkClient(Component component, Client client) {

        component.addClient(client.getDn());
        client.addComponent(component.getDn());
        ldapService.updateEntry(client);
        ldapService.updateEntry(component);
    }

    public void unLinkClient(Component component, Client client) {

        component.removeClient(client.getDn());
        client.removeComponent(component.getDn());

        ldapService.updateEntry(client);
        ldapService.updateEntry(component);
    }

    public void linkAdapter(Component component, Adapter adapter) {

        component.addAdapter(adapter.getDn());
        adapter.addComponent(component.getDn());

        ldapService.updateEntry(adapter);
        ldapService.updateEntry(component);
    }

    public void unLinkAdapter(Component component, Adapter adapter) {

        component.removeAdapter(adapter.getDn());
        adapter.removeComponent(component.getDn());

        ldapService.updateEntry(adapter);
        ldapService.updateEntry(component);
    }
}
