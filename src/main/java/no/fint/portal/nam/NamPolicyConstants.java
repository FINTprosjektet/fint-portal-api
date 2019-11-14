package no.fint.portal.nam;

public final class NamPolicyConstants {

    public static final String XPEML_CONTEXT_DATA_ELEMENT_OAUTH_SCOPE = "xpemlContextDataElement_OAuthScope";
    public static final String XPEML_CONTEXT_DATA_ELEMENT_SELECTED_OAUTH_SCOPE = "xpemlContextDataElement_SelectedOAuthScope";
    public static final String XPEML_CONTEXT_DATA_ELEMENT_LDAP_ATTRIBUTE = "xpemlContextDataElement_LdapAttribute";
    public static final String EXTERNAL_WITH_ID_REF = "ExternalWithIDRef";
    public static final String XPEML_PEP_AG_AUTHORIZATION = "xpemlPEP_AGAuthorization";
    public static final String XPEML_CONDITION_STRING = "xpemlCondition_string";
    public static final String NXPE_OPERATOR_STRING_EQUALS = "nxpeOperator_string-equals";
    public static final String XPEML_ACTION_DENY = "xpemlAction_Deny";
    public static final String XPEML_ACTION_PERMIT = "xpemlAction_Permit";

    public static final String DNF = "DNF";


    public static final String CASE_SENSITIVE = "case-sensitive";
    public static final String CASE_INSENSITIVE = "case-insensitive";


    public static final String POLICY_URL_TEMPLATE = "https://%s/amsvc/v1/policycontainers/%s/policies/%s";
    public static final String POLICY_CONTAINER_URL_TEMPLATE = "https://%s/amsvc/v1/policycontainers/%s/policies";

    public static final String DENY_CHOICE_SEND_BLOCK_MESSAGE = "SendBlockMessage";
    public static final String DENY_CHOICE_DEFAULT_BLOCK_PAGE = "DefaultBlockPage";
    public static final String DENY_CHOICE_REDIRECT_TO_LOCATION = "RedirectToLocation";
    public static final String DENY_CHOICE_PARAMETER_MESSAGE = "Message";
    public static final String DENY_CHOICE_PARAMETER_REDIRECT = "Redirect";
    public static final String DENY_PARAMETERS = "DenyParameters";
    public static final String PARAMETER_NAME_FLAGS = "flags";
}
