package no.fint.portal.nam

import groovy.json.JsonOutput
import no.fint.portal.nam.model.ActionItem
import no.fint.portal.nam.model.ActionList
import no.fint.portal.nam.model.ActionRef
import no.fint.portal.nam.model.ChoiceItem
import no.fint.portal.nam.model.ConditionItem
import no.fint.portal.nam.model.ConditionList
import no.fint.portal.nam.model.ConditionRef
import no.fint.portal.nam.model.ConditionSetItem
import no.fint.portal.nam.model.ContextDataElementRef
import no.fint.portal.nam.model.InstanceParameterList
import no.fint.portal.nam.model.LhsOperand
import no.fint.portal.nam.model.OperatorRef
import no.fint.portal.nam.model.ParameterGroupItem
import no.fint.portal.nam.model.ParameterItem
import no.fint.portal.nam.model.Policy
import no.fint.portal.nam.model.PolicyEnforcementPointRef
import no.fint.portal.nam.model.RhsOperand
import no.fint.portal.nam.model.RuleItem
import spock.lang.Specification

class NAMPolicySpec extends Specification {

    def "Name"() {
        given:
        def policy = new Policy()
        policy.setPolicyName("Test policy")
        policy.setPolicyEnforcementPointRef(new PolicyEnforcementPointRef(elementRefType: "ExternalWithIDRef", externalElementRef: "xpemlPEP_AGAuthorization"))
        policy.setDescription("This is a test 111")

        def conditionItem = new ConditionItem()
        conditionItem.setConditionRef(new ConditionRef(externalElementRef: "xpemlCondition_string", elementRefType: "ExternalWithIDRef"))
        conditionItem.setOperatorRef(new OperatorRef(externalElementRef: "nxpeOperator_string-equals", elementRefType: "ExternalWithIDRef"))
        conditionItem.setLhsOperand(new LhsOperand(value: "", contextDataElementRef: new ContextDataElementRef(externalElementRef: "xpemlContextDataElement_OAuthScope", elementRefType: "ExternalWithIDRef")))
        conditionItem.setRhsOperand(new RhsOperand(contextDataElementRef: new ContextDataElementRef(externalElementRef: "xpemlContextDataElement_SelectedOAuthScope", elementRefType: "ExternalWithIDRef"), value: "%7B%22resourceServer%22%3A%22fint-api%22%2C%22scope%22%3A%22fint-client%22%7D"))
        conditionItem.setInstanceParameterList(new InstanceParameterList(parameter: [new ParameterItem(name: "flags", value: "case-sensitive", enumerativeValue: 1)]))
        conditionItem.setNot(true)
        conditionItem.setOrder(1)

        def set = new ConditionSetItem(condition: [conditionItem])
        set.setEnable(true)
        set.setNot(false)
        set.setOrder(1)
        set.setSetOrder(1)
        def conditionList = new ConditionList(conditionSet: [set])

        def actionItem = new ActionItem()
        actionItem.setActionRef(new ActionRef(externalElementRef: "xpemlAction_Deny", elementRefType: "ExternalWithIDRef"))
        actionItem.setInstanceParameterList(new InstanceParameterList(parameterGroup: [new ParameterGroupItem(choice: [new ChoiceItem(
                parameter: [new ParameterItem(name: "Message", value: "Testing 123", enumerativeValue: 1)],
        choiceName: "SendBlockMessage", order: 2, enabled: true, enumerativeValue: 20)])]))
        actionItem.setOrder(1)
        def actionList = new ActionList(action: [actionItem])


        policy.setRule([new RuleItem(conditionList: conditionList, actionList: actionList, conditionCombiningAlgorithm: "DNF", ruleOrder: 1)])



        when:
        def json = JsonOutput.toJson(policy)

        then:
        println json

    }
}
