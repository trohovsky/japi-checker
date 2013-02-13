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
public class CheckMethodDefaultValue implements Rule {

    @Override
    public void checkBackwardCompatibility(Reporter reporter, JavaItem reference, JavaItem newItem) {

    	if (((MethodData) reference).getDefaultValue() != null && ((MethodData) newItem).getDefaultValue() == null) {
			reporter.report(new Report(Level.ERROR, "The default value of the "
					+ reference
					+ " has been removed.", 
					reference, newItem));
        }
    }
}
