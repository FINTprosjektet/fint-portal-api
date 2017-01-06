package no.fint.portal.ldap;

public interface UuidLdapEntry extends BasicLdapEntry {
    String getUuid();

    void setUuid(String uuid);
}
