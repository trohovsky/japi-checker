package com.googlecode.japi.checker.rules;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Rule;
import com.googlecode.japi.checker.model.JavaItem;

public class ClassRules implements Rule {
	
	private List<Rule> rules = new ArrayList<Rule>();
	
	public ClassRules() {
		rules.add(new ChangeKindOfAPIType());
		rules.add(new CheckChangeOfScope());
		rules.add(new CheckClassVersion());
		rules.add(new CheckInheritanceChanges());
		rules.add(new CheckRemovedField());
		rules.add(new CheckRemovedMethod());
		rules.add(new CheckSuperClass());
		rules.add(new ClassChangedToAbstract());
		rules.add(new ClassChangedToFinal());
	}

	@Override
	public void checkBackwardCompatibility(Reporter reporter,
			JavaItem reference, JavaItem newItem) {
		for (Rule rule : rules) {
            rule.checkBackwardCompatibility(reporter, reference, newItem);
        }
	}

}
