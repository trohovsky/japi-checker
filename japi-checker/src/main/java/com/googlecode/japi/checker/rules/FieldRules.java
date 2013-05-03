package com.googlecode.japi.checker.rules;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.model.JavaItem;
import com.googlecode.japi.checker.model.Scope;

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
		nonAPIrules.add(new CheckSerialVersionUIDField());
		nonAPIrules.add(new CheckFieldChangeToTransient());
		
		rules.add(new CheckFieldChangeOfType());
		rules.add(new CheckFieldChangeToFinal());
		rules.add(new CheckFieldChangeToStatic());
		rules.add(new CheckFieldChangedValue());
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
