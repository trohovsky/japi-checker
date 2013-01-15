package com.googlecode.japi.checker.rules;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Rule;
import com.googlecode.japi.checker.model.JavaItem;

public class FieldRules implements Rule {
	
	private List<Rule> rules = new ArrayList<Rule>();
	
	public FieldRules() {
		rules.add(new CheckChangeOfScope());
		rules.add(new CheckFieldChangeOfType());
		rules.add(new CheckFieldChangeToFinal());
		rules.add(new CheckFieldChangeToStatic());
		rules.add(new CheckFieldChangeToTransient());
		rules.add(new CheckSerialVersionUIDField());
	}

	@Override
	public void checkBackwardCompatibility(Reporter reporter,
			JavaItem reference, JavaItem newItem) {
		for (Rule rule : rules) {
            rule.checkBackwardCompatibility(reporter, reference, newItem);
        }
	}

}
