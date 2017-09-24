package no.fint.portal.oauth;

public class NamOAuthConstants {

    public static final String SCOPE = "urn:netiq.com:nam:scope:oauth:registration:full";
    public static final String GRANT_TYPE = "password";
    public static final String ACCESS_TOKEN_URL_TEMPLATE = "https://%s//nidp/oauth/nam/token";
    public static final String CLIENT_API_URL_TEMPLATE = "https://%s/nidp/oauth/nam/clients/";
    public static final String APPLICATION_TYPE = "web";

    public static final String DEFAULT_REDIRECT_URI = "https://dummy.com";
    public static final String RESPONSE_TYPE = "token";
}
