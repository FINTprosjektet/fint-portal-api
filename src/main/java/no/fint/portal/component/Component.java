package no.fint.portal.component;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import no.fint.portal.ldap.UuidLdapEntry;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
@Entry(objectClasses = {"organizationalUnit", "top", "fintComponent"})
public class Component implements UuidLdapEntry {

    @Id
    private Name dn;

    @ApiModelProperty(value = "Unique identifier for the component (UUID). This is automatically generated and should not be set")
    @Attribute(name = "ou")
    private String uuid;

    @ApiModelProperty(value = "Technical name of the component.")
    @Attribute(name = "fintCompTechnicalName")
    private String technicalName;

    @ApiModelProperty(value = "Displayname of the component.")
    @Attribute(name = "fintCompDisplayName")
    private String displayName;

    @ApiModelProperty(value = "A description of what the component does.")
    @Attribute(name = "description")
    private String description;

    @Attribute(name = "fintComponentOrganisations")
    private List<String> organisations;

    @Attribute(name = "fintComponentClients")
    private List<String> clients;

    @Attribute(name = "fintComponentAdapters")
    private List<String> adapters;

    public Component() {

        organisations = new ArrayList<>();
        clients = new ArrayList<>();
        adapters = new ArrayList<>();
    }

    public void addOrganisation(String organisationDn) {
        if (!organisations.stream().anyMatch(organisationDn::equalsIgnoreCase)) {
            organisations.add(organisationDn);
        }
    }

    public void removeOrganisation(String organisationDn) {
        organisations.removeIf(component -> component.equalsIgnoreCase(organisationDn));
    }

    public void addClient(String clientDn) {
        if (!clients.stream().anyMatch(clientDn::equalsIgnoreCase)) {
            clients.add(clientDn);
        }
    }

    public void removeClient(String clientDn) {
        clients.removeIf(client -> client.equalsIgnoreCase(clientDn));
    }

    public void addAdapter(String adapterDn) {
        if (!adapters.stream().anyMatch(adapterDn::equalsIgnoreCase)) {
            adapters.add(adapterDn);
        }
    }

    public void removeAdapter(String adapterDn) {
        adapters.removeIf(adapter -> adapter.equalsIgnoreCase(adapterDn));
    }

    @Override
    public String getDn() {
        if (dn != null) {
            return dn.toString();
        } else {
            return null;
        }
    }

    @Override
    public void setDn(String dn) {
        this.dn = LdapNameBuilder.newInstance(dn).build();
    }

    @Override
    public void setDn(Name dn) {
        this.dn = dn;

    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

