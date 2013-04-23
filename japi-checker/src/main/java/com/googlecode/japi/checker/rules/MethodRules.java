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
public class MethodRules implements Rule {
	
	private List<Rule> rules = new ArrayList<Rule>();
	private List<Rule> nonAPIrules = new ArrayList<Rule>();

	public MethodRules() {
		nonAPIrules.add(new CheckChangeOfScope());
		
		rules.add(new CheckMethodVariableArity());
		rules.add(new CheckMethodDefaultValue());
		rules.add(new CheckMethodChangedToAbstract());
		rules.add(new CheckMethodChangedToFinal());
		rules.add(new CheckMethodChangedToStatic());
		rules.add(new CheckMethodExceptions());
		rules.add(new CheckTypeParameters());
	}
	
	@Override
	public void checkBackwardCompatibility(Reporter reporter, JavaItem reference, JavaItem newItem) {
		
		// rules where methods do not have to be API
		for (Rule rule : nonAPIrules) {
			rule.checkBackwardCompatibility(reporter, reference, newItem);
		}
				
		// rules where methods have to be API
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
