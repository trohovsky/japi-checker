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
import com.googlecode.japi.checker.Scope;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
// FIELD
public class CheckFieldChangeToFinal implements Rule {

    @Override
    public void checkBackwardCompatibility(Reporter reporter, JavaItem reference, JavaItem newItem) {
    	
        if (reference instanceof FieldData) {
            if (reference.getVisibility().isMoreVisibleThan(Scope.PACKAGE) &&
            	newItem.getVisibility().isMoreVisibleThan(Scope.PACKAGE)) {
            	
                if (!reference.isFinal() && newItem.isFinal()) {
                    reporter.report(new Report(Level.ERROR, "The field " + reference.getName() + " has been made final.", reference, newItem));
                } else if (reference.isFinal() && !newItem.isFinal() 
                		&& reference.isStatic() && newItem.isStatic()
                		&& ((FieldData) reference).isCompileTimeConstant()) {
                	// if field is static with compile-time constant value
                    reporter.report(new Report(Level.ERROR, "The field " + reference.getName() + " has been made non-final. (it has compile-time constant value)", reference, newItem));
                }
            }
        }
    }

}
