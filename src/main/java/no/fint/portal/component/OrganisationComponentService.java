package no.fint.portal.component;

import no.fint.portal.utilities.LdapConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

@Service
public class OrganisationComponentService {

    @Autowired
    ComponentObjectService componentObjectService;

    public void setOrganisationContainerDN(Container organisationContainer, String componentUuid) {
        organisationContainer.setDn(
                LdapNameBuilder.newInstance(componentObjectService.getComponentDnByUUID(componentUuid))
                        .add(LdapConstants.OU, organisationContainer.getOu())
                        .build()
        );
    }

    public void setClientContainerDN(Container clientContainer, Container organisationContainer) {
        clientContainer.setDn(
                LdapNameBuilder.newInstance(organisationContainer.getDn())
                        .add(LdapConstants.OU, clientContainer.getOu())
                        .build()
        );
    }

    public void setAdapterContainerDN(Container adapterContainer, Container organisationContainer) {
        adapterContainer.setDn(
                LdapNameBuilder.newInstance(organisationContainer.getDn())
                        .add(LdapConstants.OU, adapterContainer.getOu())
                        .build()
        );
    }
}
