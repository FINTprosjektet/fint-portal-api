package no.fint.portal.utilities;

import java.util.UUID;

public enum PasswordUtility {
    ;

    public static String newPassword() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
