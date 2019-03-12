package com.viglet.turing.sn.tr;

public enum TurSNTargetingRuleMethod {
	AND("AND"), OR("OR");

	private String id;

	TurSNTargetingRuleMethod(String id) {
		this.id = id;
	}

	public String id() {
		return id;
	}
}
