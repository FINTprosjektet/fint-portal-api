package no.fint.portal.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import no.fint.portal.ldap.BasicLdapEntry;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;

@ApiModel
//@Data
@Entry(objectClasses = {"fintAdapter", "inetOrgPerson", "organizationalPerson", "person", "top"})
public class Adapter implements BasicLdapEntry {

    @ApiModelProperty(value = "DN of the adapter. This is automatically set.")
    @Id
    private Name dn;

    @ApiModelProperty(value = "Username for the adapter. This is automatically set.")
    @Attribute(name = "cn")
    private String uuid;

    @ApiModelProperty(value = "Short description of the adapter")
    @Attribute(name = "sn")
    private String shortDescription;

    @ApiModelProperty(value = "OrgId of the organisation the adapter is connected to. This is automatically set.")
    @Attribute(name = "fintAdapterOrgId")
    private String orgId;

    @ApiModelProperty(value = "A note of the adapter.")
    @Attribute(name = "description")
    private String note;

    @JsonIgnore
    @Attribute(name = "userPassword")
    private String password;

    @ApiModelProperty(value = "Adapter secret.")
    @Attribute(name = "fintAdapterSecret")
    private String secret;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.password = secret;
        this.secret = secret;
    }

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
