package no.fint.portal.component;

import lombok.Getter;
import no.fint.portal.ldap.LdapService;
import no.fint.portal.utilities.LdapConstants;
import no.fint.portal.utilities.ObjectUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

@Service
public class ComponentObjectService {

    @Autowired
    LdapService ldapService;
    @Getter
    @Value("${fint.ldap.component-base}")
    private String componentBase;

    public void setupComponent(Component component) {
        Component componentFromLdap = ldapService.getEntryByUniqueName(component.getTechnicalName(), componentBase, Component.class);
        ObjectUtility.setupUuidContainerObject(component, componentFromLdap, componentBase);
    }
}
