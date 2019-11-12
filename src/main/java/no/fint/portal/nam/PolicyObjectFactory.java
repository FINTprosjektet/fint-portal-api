package no.fint.portal.nam;

import no.fint.portal.nam.model.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static no.fint.portal.nam.Constants.*;

public final class PolicyObjectFactory {

    public static Policy createAuthorizationPolicy(String name, String scope, String component, String attributeName) {
        return Policy.builder()
                .enable(true)
                .policyName(name)
                .description("Automatically created by the admin portal")
                .policyEnforcementPointRef(createPolicyEnforcementPointRefAuthorization())
                .rule(Arrays.asList(createScopeRuleItem(scope), createComponentRuleItem(component, attributeName), createDenyCleanupRuleItem()))
                .build();
    }

    private static ContextDataElementRef createContextDataElementRef(String externalElementRef) {
        return ContextDataElementRef.builder()
                .elementRefType(EXTERNAL_WITH_ID_REF)
                .externalElementRef(externalElementRef)
                .build();
    }

    private static ConditionRef createConditionRefString() {
        return ConditionRef.builder()
                .elementRefType(EXTERNAL_WITH_ID_REF)
                .externalElementRef(XPEML_CONDITION_STRING)
                .build();
    }

    private static OperatorRef createOperationRefStringEquals() {
        return OperatorRef.builder()
                .elementRefType(EXTERNAL_WITH_ID_REF)
                .externalElementRef(NXPE_OPERATOR_STRING_EQUALS)
                .build();
    }

    private static LhsOperand createLhsOperandContextDataElementOauthScope() {
        return LhsOperand.builder()
                .contextDataElementRef(createContextDataElementRef(XPEML_CONTEXT_DATA_ELEMENT_OAUTH_SCOPE))
                .value("")
                .build();
    }

    private static LhsOperand createLhsOperandContextDataElementLdapAttribute(String attributeName) {
        return LhsOperand.builder()
                .contextDataElementRef(createContextDataElementRef(XPEML_CONTEXT_DATA_ELEMENT_LDAP_ATTRIBUTE))
                .value(attributeName)
                .build();
    }

    private static RhsOperand createRhsOperandContextDataElementSelectedOauthScope(String scope) {
        return RhsOperand.builder()
                .contextDataElementRef(createContextDataElementRef(XPEML_CONTEXT_DATA_ELEMENT_SELECTED_OAUTH_SCOPE))
                .value(ResourceServer.builder().resourceServer("fint-api").scope(scope).build().toEncodeString())
                .build();
    }

    private static RhsOperand createRhsOperandContextDataElementDataEntryField(String data) {
        try {
            return RhsOperand.builder()
                    .value(URLEncoder.encode(data, "UTF-8"))
                    .build();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<ParameterItem> createParameterItemFlagsCaseSensitive() {
        return Collections.singletonList(ParameterItem.builder()
                .name("flags")
                .value(CASE_SENSITIVE)
                .enumerativeValue(1)
                .build());
    }

    private static List<ParameterItem> createParameterItemFlagsCaseInSensitive() {
        return Collections.singletonList(ParameterItem.builder()
                .name("flags")
                .value(CASE_INSENSITIVE)
                .enumerativeValue(1)
                .build());
    }

    private static List<ActionItem> createActionItemPermit() {
        return Collections.singletonList(
                ActionItem.builder()
                        .actionRef(createActionRef(XPEML_ACTION_PERMIT))
                        .instanceParameterList(
                                InstanceParameterList.builder()
                                        .parameterGroup(Collections.emptyList())
                                        .parameter(Collections.emptyList())
                                        .build()
                        ).order(1)
                        .build()
        );
    }

    private static List<ActionItem> createActionItemDenyWithMessage(String message, int order) {
        return Collections.singletonList(ActionItem.builder()
                .actionRef(createActionRef(XPEML_ACTION_DENY))
                .instanceParameterList(InstanceParameterList.builder().parameterGroup(
                        Collections.singletonList(ParameterGroupItem.builder()
                                .enumerativeValue(1)
                                .choice(Arrays.asList(
                                        ChoiceItem.builder()
                                                .parameter(Collections.singletonList(
                                                        ParameterItem.builder()
                                                                .name("Message")
                                                                .value(message)
                                                                .enumerativeValue(20).build()
                                                ))
                                                .choiceName("SendBlockMessage")
                                                .order(2)
                                                .enabled(true)
                                                .enumerativeValue(20)
                                                .build(),
                                        ChoiceItem.builder()
                                                .parameter(Collections.emptyList())
                                                .choiceName("DefaultBlockPage")
                                                .order(1)
                                                .enabled(false)
                                                .enumerativeValue(10)
                                                .build(),
                                        ChoiceItem.builder()
                                                .parameter(Collections.singletonList(
                                                        ParameterItem.builder()
                                                                .name("Redirect")
                                                                .value("")
                                                                .enumerativeValue(1)
                                                                .build()
                                                ))
                                                .choiceName("RedirectToLocation")
                                                .order(3)
                                                .enumerativeValue(30)
                                                .enabled(false)
                                                .build()
                                        )
                                )
                                .groupName("DenyParameters")
                                .build()
                        ))
                        .parameter(Collections.emptyList())
                        .build())
                .order(order)
                .build());
    }

    private static ActionRef createActionRef(String externalElementRef) {
        return ActionRef.builder()
                .elementRefType(EXTERNAL_WITH_ID_REF)
                .externalElementRef(externalElementRef)
                .build();
    }

    private static List<ConditionItem> createConditionItemScopeRule(String scope) {
        return Collections.singletonList(ConditionItem.builder()
                .conditionRef(createConditionRefString())
                .operatorRef(createOperationRefStringEquals())
                .lhsOperand(createLhsOperandContextDataElementOauthScope())
                .rhsOperand(createRhsOperandContextDataElementSelectedOauthScope(scope))
                .instanceParameterList(
                        InstanceParameterList.builder()
                                .parameter(createParameterItemFlagsCaseSensitive())
                                .build()
                )
                .not(true)
                .enable(true)
                .order(1)
                .build());
    }

    private static List<ConditionItem> createConditionItemComponentRule(String component, String attributeName) {
        return Collections.singletonList(ConditionItem.builder()
                .conditionRef(createConditionRefString())
                .operatorRef(createOperationRefStringEquals())
                .lhsOperand(createLhsOperandContextDataElementLdapAttribute(attributeName))
                .rhsOperand(createRhsOperandContextDataElementDataEntryField(component))
                .instanceParameterList(
                        InstanceParameterList.builder()
                                .parameter(createParameterItemFlagsCaseInSensitive())
                                .build()
                )
                .not(true)
                .enable(true)
                .order(1)
                .build());
    }


    private static List<ConditionSetItem> createConditionSetItemScopeRule(String scope) {
        return Collections.singletonList(ConditionSetItem.builder()
                .condition(createConditionItemScopeRule(scope))
                .enable(true)
                .not(false)
                .order(1)
                .setOrder(1)
                .build());
    }

    private static List<ConditionSetItem> createConditionSetItemComponentRule(String component, String attributeName) {
        return Collections.singletonList(ConditionSetItem.builder()
                .condition(createConditionItemComponentRule(component, attributeName))
                .enable(true)
                .not(false)
                .order(1)
                .setOrder(1)
                .build());
    }

    private static RuleItem createScopeRuleItem(String scope) {
        return RuleItem.builder()
                .conditionCombiningAlgorithm(DNF)
                .ruleOrder(1)
                .description("Deny if scope is missing")
                .conditionList(ConditionList.builder()
                        .conditionSet(createConditionSetItemScopeRule(scope))
                        .build())
                .actionList(ActionList.builder()
                        .action(createActionItemDenyWithMessage(
                                String.format("Missing '%s' scope. You need to acquire '%s' scope.", scope, scope),
                                1)
                        )
                        .build())
                .build();
    }

    private static RuleItem createComponentRuleItem(String componentDn, String attributeName) {
        return RuleItem.builder()
                .conditionCombiningAlgorithm(DNF)
                .ruleOrder(1)
                .description(String.format(" Allow access if %s is available", componentDn))
                .actionList(
                        ActionList.builder()
                                .action(createActionItemPermit())
                                .build()
                )
                .conditionList(
                        ConditionList.builder()
                                .conditionSet(createConditionSetItemComponentRule(componentDn, attributeName))
                                .build()
                )
                .build();
    }

    private static RuleItem createDenyCleanupRuleItem() {
        return RuleItem.builder()
                .conditionCombiningAlgorithm(DNF)
                .ruleOrder(1)
                .description("Deny all")
                .actionList(ActionList.builder()
                        .action(createActionItemDenyWithMessage("Deny access", 1))
                        .build())
                .build();
    }

    private static PolicyEnforcementPointRef createPolicyEnforcementPointRefAuthorization() {
        return PolicyEnforcementPointRef.builder()
                .elementRefType(EXTERNAL_WITH_ID_REF)
                .externalElementRef(XPEML_PEP_AG_AUTHORIZATION)
                .build();
    }

}
