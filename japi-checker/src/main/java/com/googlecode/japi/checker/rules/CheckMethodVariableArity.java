package com.googlecode.japi.checker.rules;

import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Reporter.Level;
import com.googlecode.japi.checker.Reporter.Report;
import com.googlecode.japi.checker.Rule;
import com.googlecode.japi.checker.model.JavaItem;
import com.googlecode.japi.checker.model.MethodData;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
public class CheckMethodVariableArity implements Rule {
	
	@Override
    public void checkBackwardCompatibility(Reporter reporter, JavaItem reference, JavaItem newItem) {

    	if (((MethodData) reference).isVariableArity() && !((MethodData) newItem).isVariableArity()) {
			reporter.report(new Report(Level.ERROR, "The parameter of the "
					+ reference
					+ " has been changed from variable arity to array.", 
					reference, newItem));
        }
    }
}
