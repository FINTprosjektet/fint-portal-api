package no.fint.portal.ldap;

import javax.naming.Name;

public interface BasicLdapEntry {
    String getDn();

    void setDn(String dn);

    void setDn(Name dn);
}
