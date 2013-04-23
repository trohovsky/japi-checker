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
package com.googlecode.japi.checker;

import java.util.List;

import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.FieldData;
import com.googlecode.japi.checker.model.MethodData;
import com.googlecode.japi.checker.model.Scope;
import com.googlecode.japi.checker.rules.ClassRules;
import com.googlecode.japi.checker.rules.FieldRules;
import com.googlecode.japi.checker.rules.MethodRules;

public class BCChecker {
    
    private ClassRules classRules;
    private FieldRules fieldRules;
    private MethodRules methodRules;
    
    public BCChecker() {
        classRules = new ClassRules();
    	fieldRules = new FieldRules();
        methodRules = new MethodRules();
    }
    
    public void checkBackwardCompatibility(Reporter reporter, List<? extends ClassData> referenceClasses, List<? extends ClassData> newClasses) {
        
        for (ClassData referenceClass : referenceClasses) {
            boolean found = false;
            for (ClassData newClass : newClasses) {
                if (referenceClass.isSame(newClass)) {
                	// checking class rules
                    classRules.checkBackwardCompatibility(reporter, referenceClass, newClass);
                    
                    // checking field rules
                    for (FieldData referenceField : referenceClass.getFields()) {
                        for (FieldData newField: newClass.getFields()) {
                            if (referenceField.isSame(newField)) {
                            	fieldRules.checkBackwardCompatibility(reporter, referenceField, newField);
                            }
                        }
                    }
                    
                    // checking method rules
                    for (MethodData referenceMethod : referenceClass.getMethods()) {
                        for (MethodData newMethod: newClass.getMethods()) {
                            if (referenceMethod.isSame(newMethod)) {
                            	methodRules.checkBackwardCompatibility(reporter, referenceMethod, newMethod);
                            }
                        }
                    }
                    
                    found = true;
                    break;
                }
            }
            if (!found && referenceClass.getVisibility() == Scope.PUBLIC) {
                reporter.report(referenceClass, null, DifferenceType.PACKAGE_REMOVED_CLASS, referenceClass);
            }
        }
    }
}
