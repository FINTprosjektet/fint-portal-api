package no.fint.portal.component;

import lombok.extern.slf4j.Slf4j;
import no.fint.portal.adapter.Adapter;
import no.fint.portal.client.Client;
import no.fint.portal.ldap.LdapService;
import no.fint.portal.organisation.Organisation;
import no.fint.portal.organisation.OrganisationService;
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

    public Optional<Component> getComponentByUUID(String uuid) {
        String dn = getComponentDnByUUID(uuid);

        return Optional.ofNullable(ldapService.getEntry(dn, Component.class));
    }

    public void deleteComponent(Component component) {
        ldapService.deleteEntry(component);
    }

    public String getComponentDnByUUID(String uuid) {
        if (uuid != null) {
            return LdapNameBuilder.newInstance(componentBase)
                    .add(LdapConstants.OU, uuid)
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

        ldapService.updateEntry(component);
    }

    public void unLinkClient(Component component, Client client) {

        component.removeClient(client.getDn());

        ldapService.updateEntry(component);
    }

    public void linkAdapter(Component component, Adapter adapter) {

        component.addAdapter(adapter.getDn());

        ldapService.updateEntry(component);
    }

    public void unLinkAdapter(Component component, Adapter adapter) {

        component.removeAdapter(adapter.getDn());

        ldapService.updateEntry(component);
    }
}
