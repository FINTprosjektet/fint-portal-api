package no.fint.portal.ldap;

import javax.naming.Name;

public interface BasicLdapEntry {
    String getDn();

    void setDn(Name dn);

    void setDn(String dn);
}
