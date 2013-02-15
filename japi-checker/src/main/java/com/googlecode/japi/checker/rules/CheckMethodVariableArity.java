package com.googlecode.japi.checker.rules;

import com.googlecode.japi.checker.Difference;
import com.googlecode.japi.checker.DifferenceType;
import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Rule;
import com.googlecode.japi.checker.model.JavaItem;
import com.googlecode.japi.checker.model.MethodData;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
// METHOD
public class CheckMethodVariableArity implements Rule {
	
	@Override
    public void checkBackwardCompatibility(Reporter reporter, JavaItem reference, JavaItem newItem) {

    	if (((MethodData) reference).isVariableArity() && !((MethodData) newItem).isVariableArity()) {
			reporter.report(new Difference(reference, newItem,
					DifferenceType.METHOD_VARARG_PARAM_TO_ARRAY, reference));
        }
    }
}
