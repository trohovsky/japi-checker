package com.googlecode.japi.checker.rules;

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
public class CheckMethodDefaultValue implements Rule {

    @Override
    public void checkBackwardCompatibility(Reporter reporter, JavaItem reference, JavaItem newItem) {

    	if (((MethodData) reference).getDefaultValue() != null && ((MethodData) newItem).getDefaultValue() == null) {
			reporter.report(reference, newItem, DifferenceType.METHOD_REMOVED_DEFAULT_VALUE, reference);
        }
    }
}
