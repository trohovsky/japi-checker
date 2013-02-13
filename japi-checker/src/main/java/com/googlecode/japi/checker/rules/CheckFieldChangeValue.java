/*
 * Copyright 2011 William Bernardet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.japi.checker.rules;

import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Reporter.Level;
import com.googlecode.japi.checker.Reporter.Report;
import com.googlecode.japi.checker.model.FieldData;
import com.googlecode.japi.checker.model.JavaItem;
import com.googlecode.japi.checker.Rule;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
// FIELD
public class CheckFieldChangeValue implements Rule {

    @Override
    public void checkBackwardCompatibility(Reporter reporter, JavaItem reference, JavaItem newItem) {
            			
		if (isValueOfCompileTimeConstantChanged((FieldData) reference, (FieldData) newItem)) {
			reporter.report(new Report(Level.ERROR, "The value of "
					+ reference
					+ " has been changed from "
					+ ((FieldData) reference).getValue() + " to "
					+ ((FieldData) newItem).getValue(), reference, newItem));
		}
	}

	private boolean isValueOfCompileTimeConstantChanged(FieldData reference, FieldData newItem) {
				
		if (reference.isFinal() && newItem.isFinal()) {
			final Object rVal = reference.getValue();
			if (rVal != null) {
				final String rValString = rVal.toString();
				final Object nVal = newItem.getValue();
				if (nVal != null) {
					return !rValString.equals(nVal.toString());
				} else {
					return true;
				}
			}
		}
		return false;
	}

}
