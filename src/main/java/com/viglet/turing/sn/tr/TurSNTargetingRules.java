package com.viglet.turing.sn.tr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

@Component
public class TurSNTargetingRules {

	public String run(TurSNTargetingRuleMethod method, List<String> trs) {

		switch (method) {
		case AND:
			return this.andMethod(trs);
		case OR:
			return this.orMethod(trs);
		}
		return "";
	}

	private String andMethod(List<String> trs) {
		// (attribute1:Group OR attribute1:Group2 OR (*:* NOT attribute1:*)) AND
		// (attribute2:Value2 OR (*:* NOT attribute2:*))
		StringBuilder targetingRuleQuery = new StringBuilder();

		Map<String, List<String>> trMap = new HashMap<String, List<String>>();

		for (String tr : trs) {
			String[] targetingRuleParts = tr.split(":");
			if (targetingRuleParts.length == 2) {
				String attribute = targetingRuleParts[0].trim();
				String value = targetingRuleParts[1];
				if (!trMap.containsKey(attribute))
					trMap.put(attribute, new ArrayList<String>());
				trMap.get(attribute).add(value);
			}
		}

		String targetingRuleAND = "";

		for (Entry<String, List<String>> trEntry : trMap.entrySet()) {			
			String targetingRuleOR = "";
			targetingRuleQuery.append(targetingRuleAND);
			targetingRuleQuery.append("(");
			for (String trEntryValue : trEntry.getValue()) {
				targetingRuleQuery.append(" ");
				targetingRuleQuery
						.append(String.format("%s %s:%s", targetingRuleOR, trEntry.getKey(), trEntryValue).trim());
				targetingRuleOR = "OR";
			}
			targetingRuleQuery.append(String.format(" OR (*:* NOT %s:*)", trEntry.getKey()));
			targetingRuleQuery.append(" )");
			targetingRuleAND = " AND ";
		}

		return targetingRuleQuery.toString();
	}

	private String orMethod(List<String> trs) {
		// Sample: "(groups:Group1 OR groups:Group2) OR (*:* NOT groups:*)");
		List<String> emptyTargetingRules = new ArrayList<String>();
		StringBuilder targetingRuleQuery = new StringBuilder();
		targetingRuleQuery.append("(");
		String targetingRuleOR = "";
		for (String tr : trs) {
			targetingRuleQuery.append(targetingRuleOR).append(tr);
			targetingRuleOR = " OR ";
			String[] targetingRuleParts = tr.split(":");
			if (targetingRuleParts.length == 2 && !emptyTargetingRules.contains(targetingRuleParts[0])) {
				emptyTargetingRules.add(targetingRuleParts[0]);
			}
		}
		targetingRuleQuery.append(")");

		if (emptyTargetingRules.size() > 0) {
			for (String emptyTargetRule : emptyTargetingRules) {
				targetingRuleQuery.append(String.format(" OR (*:* NOT %s:*)", emptyTargetRule));
			}
		}

		return targetingRuleQuery.toString();
	}
}
