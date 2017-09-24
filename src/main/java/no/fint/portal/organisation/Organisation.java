package no.fint.portal.organisation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import no.fint.portal.ldap.UuidLdapEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
@ApiModel
@Data
@Entry(objectClasses = {"organizationalUnit", "top", "fintOrg"})
public final class Organisation implements UuidLdapEntry {

    @Id
    private Name dn;

    @ApiModelProperty(
            value = "Unique identifier for the organisation (UUID). This is automatically generated and should not be set."
    )
    @Attribute(name = "ou")
    private String uuid;

    @ApiModelProperty(value = "The organisation number from Enhetsregisteret (https://w2.brreg.no/enhet/sok/index.jsp)")
    @Attribute(name = "fintOrgNumber")
    private String orgNumber;

    @ApiModelProperty(
            value = "Id of the organisation. Should be the official domain of the organisation. For example rogfk.no"
    )
    @Attribute(name = "fintOrgId")
    private String orgId;

    @ApiModelProperty(
            value = "The official name of the organisation. See Enhetsregisteret (https://w2.brreg.no/enhet/sok/index.jsp)"
    )
    @Attribute(name = "fintOrgDisplayName")
    private String displayName;

    @Attribute(name = "fintOrganisationComponents")
    private List<String> components;

    public Organisation() {
        components = new ArrayList<>();
    }

    public void addComponent(String componentDn) {
        if (!components.stream().anyMatch(componentDn::equalsIgnoreCase)) {
            components.add(componentDn);
        }
    }

    public void removeComponent(String componentDn) {
        components.removeIf(component  -> component.equalsIgnoreCase(componentDn));
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
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
    public void setDn(Name dn) {
        this.dn = dn;
    }

    @Override
    public void setDn(String dn) {
        this.dn = LdapNameBuilder.newInstance(dn).build();
    }
}
