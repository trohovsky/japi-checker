package com.googlecode.japi.checker.rules;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Rule;
import com.googlecode.japi.checker.model.JavaItem;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
public class MethodRules implements Rule {
	
	private List<Rule> rules = new ArrayList<Rule>();;

	public MethodRules() {
		rules.add(new CheckChangeOfScope());
		rules.add(new CheckMethodChangedToAbstract());
		rules.add(new CheckMethodChangedToFinal());
		rules.add(new CheckMethodChangedToStatic());
		rules.add(new CheckMethodExceptions());
	}
	
	@Override
	public void checkBackwardCompatibility(Reporter reporter, JavaItem reference, JavaItem newItem) {
		for (Rule rule : rules) {
            rule.checkBackwardCompatibility(reporter, reference, newItem);
        }
	}
}
