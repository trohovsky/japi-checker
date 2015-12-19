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
import com.googlecode.japi.checker.model.JavaItem;
import com.googlecode.japi.checker.model.Scope;

// GENERAL
public class CheckChangeOfScope implements Rule {

	@Override
	public void checkBackwardCompatibility(Reporter reporter, JavaItem reference, JavaItem newItem) {

		// API type or public or protected member
		if ((reference.getOwner() == null && newItem.getOwner() == null) ||
				(reference.getOwner().getVisibility().isMoreVisibleThan(Scope.PACKAGE) &&
						reference.getOwner().getVisibility().isMoreVisibleThan(Scope.PACKAGE))) {

			if (newItem.getVisibility().isLessVisibleThan(reference.getVisibility())) {
				// lower visibility
				reporter.report(reference, newItem,
						DifferenceType.GENERAL_DECREASED_VISIBILITY, reference,
						reference.getVisibility(), newItem.getVisibility());
			} else if (newItem.getVisibility().isMoreVisibleThan(
					reference.getVisibility())) {
				// higher visibility
				reporter.report(reference, newItem,
						DifferenceType.GENERAL_INCREASED_VISIBILITY, reference,
						reference.getVisibility(), newItem.getVisibility());
			}
		}
	}
}
