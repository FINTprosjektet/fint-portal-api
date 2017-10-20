package no.fint.portal.component;

import lombok.Getter;
import no.fint.portal.ldap.LdapService;
import no.fint.portal.utilities.LdapConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;

@Service
public class ComponentObjectService {

    @Autowired
    LdapService ldapService;
    @Getter
    @Value("${fint.ldap.component-base}")
    private String componentBase;

    public void setupComponent(Component component) {
        Component componentFromLdap = ldapService.getEntryByUniqueName(component.getName(), componentBase, Component.class);

        if (componentFromLdap == null) {
            Name dn = LdapNameBuilder.newInstance(componentBase)
                    .add(LdapConstants.OU, component.getName())
                    .build();
            component.setDn(dn);
        } else {
            component.setDn(LdapNameBuilder.newInstance(componentFromLdap.getDn()).build());
            component.setName(componentFromLdap.getName());
        }
    }

}
