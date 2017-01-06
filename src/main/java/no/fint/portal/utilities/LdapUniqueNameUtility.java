package no.fint.portal.utilities;

import no.fint.portal.component.Component;
import no.fint.portal.organisation.Contact;
import no.fint.portal.organisation.Organisation;

public enum LdapUniqueNameUtility {
    ;

    public static <T> String getUniqueNameAttribute(Class<T> type) {

        if (type.getName().equals(Organisation.class.getName())) {
            return "fintOrgId";
        }

        if (type.getName().equals(Component.class.getName())) {
            return "fintCompTechnicalName";
        }

        if (type.getName().equals(Contact.class.getName())) {
            return "cn";
        }
        return null;
    }
}
