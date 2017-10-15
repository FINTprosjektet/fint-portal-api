package no.fint.portal.contact;

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
@Data
@Entry(objectClasses = {"inetOrgPerson", "organizationalPerson", "person", "top", "fintContact"})
public class Contact implements BasicLdapEntry {

    @ApiModelProperty(value = "DN of the contact. This is automatically set.")
    @Id
    private Name dn;

    @ApiModelProperty(value = "National Idenitification Number (NIN). This would be fodselsnummer (11 digits)")
    @Attribute(name = "cn")
    private String nin;

    @ApiModelProperty(value = "First name of the contact.")
    @Attribute(name = "givenName")
    private String firstName;

    @ApiModelProperty(value = "Last name of the contact.")
    @Attribute(name = "sn")
    private String lastName;

    @ApiModelProperty(value = "Internet email address for the contact.")
    @Attribute(name = "mail")
    private String mail;

    @ApiModelProperty(value = "Mobile number of the contact. Should include landcode.")
    @Attribute(name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "OrgId of the organisation the contact is connected to. This is automatically set.")
    @Attribute(name = "fintContactOrgId")
    private String orgId;

    @ApiModelProperty(value = "Indicates if the contact is the primary technical contact for the organisation.")
    @Attribute(name = "fintContactPrimaryTechnical")
    private boolean primaryTechnical;

    @ApiModelProperty(value = "Indicates if the contact is the primary legal contact for the organisation.")
    @Attribute(name = "fintContactPrimaryLegal")
    private boolean primaryLegal;

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