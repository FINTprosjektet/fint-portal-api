package no.fint.portal.nam;

import no.fint.portal.nam.model.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static no.fint.portal.nam.Constants.*;

public final class ObjectFactory {


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
                .contextDataElementRef(ObjectFactory.createContextDataElementRef(XPEML_CONTEXT_DATA_ELEMENT_OAUTH_SCOPE))
                .value("")
                .build();
    }

    private static RhsOperand createRhsOperandContextDataElementSlectedOauthScope(String scope) {
        return RhsOperand.builder()
                .contextDataElementRef(ObjectFactory.createContextDataElementRef(XPEML_CONTEXT_DATA_ELEMENT_SELECTED_OAUTH_SCOPE))
                .value(ResourceServer.builder().resourceServer("fint-api").scope(scope).build().toEncodeString())
                .build();
    }

    private static List<ParameterItem> createParameterItemFlagsCaseSensitive() {
        return Collections.singletonList(ParameterItem.builder()
                .name("flags")
                .value("case-sensitive")
                .enumerativeValue(1)
                .build());
    }

    private static List<ActionItem> createActionItemDeny(String value, int order) {
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
                                                                .value(value)
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
                .conditionRef(ObjectFactory.createConditionRefString())
                .operatorRef(ObjectFactory.createOperationRefStringEquals())
                .lhsOperand(ObjectFactory.createLhsOperandContextDataElementOauthScope())
                .rhsOperand(ObjectFactory.createRhsOperandContextDataElementSlectedOauthScope(scope))
                .instanceParameterList(
                        InstanceParameterList.builder()
                                .parameter(ObjectFactory.createParameterItemFlagsCaseSensitive())
                                .build()
                )
                .not(true)
                .enable(true)
                .order(1)
                .build());
    }


    private static List<ConditionSetItem> createConditionSetItemScopeRule(String scope) {
        return Collections.singletonList(ConditionSetItem.builder()
                .condition(ObjectFactory.createConditionItemScopeRule(scope))
                .enable(true)
                .not(false)
                .order(1)
                .setOrder(1)
                .build());
    }

    public static RuleItem createScopeRuleItem(String scope) {
        return RuleItem.builder()
                .conditionCombiningAlgorithm(DNF)
                .ruleOrder(1)
                .description("Deny if scope is missing")
                .conditionList(ConditionList.builder()
                        .conditionSet(ObjectFactory.createConditionSetItemScopeRule(scope))
                        .build())
                .actionList(ActionList.builder()
                        .action(ObjectFactory.createActionItemDeny(
                                String.format("Missing '%s' scope. You need to acquire '%s' scope.", scope, scope),
                                1)
                        )
                        .build())
                .build();
    }
}
