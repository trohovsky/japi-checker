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

import com.googlecode.japi.checker.DifferenceType;
import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.FieldData;
import com.googlecode.japi.checker.model.JavaItem;
import com.googlecode.japi.checker.model.Scope;

// CLASS
public class CheckRemovedField implements Rule {

	@Override
	public void checkBackwardCompatibility(Reporter reporter, JavaItem reference, JavaItem newItem) {

		ClassData referenceClass = (ClassData) reference;
		ClassData newClass = (ClassData) newItem;

		for (FieldData oldField : referenceClass.getFields()) {
			boolean found = false;
			for (FieldData newField : newClass.getFields()) {
				if (oldField.isSame(newField)) {
					found = true;
					break;
				}
			}
			if (!found && oldField.getVisibility().isMoreVisibleThan(Scope.PACKAGE)) {
				reporter.report(reference, newItem,
						DifferenceType.CLASS_REMOVED_FIELD, oldField);
			}
		}
	}


}
