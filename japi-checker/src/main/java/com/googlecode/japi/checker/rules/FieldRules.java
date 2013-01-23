package com.googlecode.japi.checker.rules;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Rule;
import com.googlecode.japi.checker.Scope;
import com.googlecode.japi.checker.model.JavaItem;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
public class FieldRules implements Rule {
	
	private List<Rule> rules = new ArrayList<Rule>();
	private List<Rule> nonAPIrules = new ArrayList<Rule>();
	
	public FieldRules() {
		nonAPIrules.add(new CheckChangeOfScope());
		
		rules.add(new CheckFieldChangeOfType());
		rules.add(new CheckFieldChangeToFinal());
		rules.add(new CheckFieldChangeToStatic());
		rules.add(new CheckFieldChangeToTransient()); // TODO not sure if it should check only API fields
		rules.add(new CheckFieldChangeValue());
		rules.add(new CheckSerialVersionUIDField()); // TODO not sure if it should check only API fields
	}

	@Override
	public void checkBackwardCompatibility(Reporter reporter, JavaItem reference, JavaItem newItem) {
		
		// rules where fields do not have to be API
		for (Rule rule : nonAPIrules) {
			rule.checkBackwardCompatibility(reporter, reference, newItem);
		}
		
		// rules where fields have to be API
		if (reference.getOwner().getVisibility().isMoreVisibleThan(Scope.PACKAGE) && 
			newItem.getOwner().getVisibility().isMoreVisibleThan(Scope.PACKAGE)) {

			if (reference.getVisibility().isMoreVisibleThan(Scope.PACKAGE) &&
				newItem.getVisibility().isMoreVisibleThan(Scope.PACKAGE)) {
				
				for (Rule rule : rules) {
					rule.checkBackwardCompatibility(reporter, reference, newItem);
				}
			}
		}
	}

}
