package no.fint.portal.component;

import lombok.extern.slf4j.Slf4j;
import no.fint.portal.ldap.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
        String dn = componentObjectService.getComponentDnByUUID(uuid);

        return Optional.ofNullable(ldapService.getEntry(dn, Component.class));
    }

    public void deleteComponent(Component component) {
        ldapService.deleteEntry(component);
    }

    public void addOrganisationToComponent(String componentUuid, String organistionUuid) {

    }

    public void removeOrganisationFromComponent(String componentUuid, String organistionUuid) {

    }

}
