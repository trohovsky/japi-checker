package com.googlecode.japi.checker.rules;

import java.util.List;

import com.googlecode.japi.checker.DifferenceType;
import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Rule;
import com.googlecode.japi.checker.model.JavaItem;
import com.googlecode.japi.checker.model.Parametrized;
import com.googlecode.japi.checker.model.TypeParameterData;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
// CLASS, METHOD
public class CheckTypeParameters implements Rule {

	@Override
	public void checkBackwardCompatibility(Reporter reporter, JavaItem reference, JavaItem newItem) {
		
		List<TypeParameterData> referenceTypeParameters = ((Parametrized)reference).getTypeParameters();
		List<TypeParameterData> newTypeParameters = ((Parametrized)newItem).getTypeParameters();
		
		// added type parameter
		// element has type parameters
		if (!referenceTypeParameters.isEmpty()) {
			if (referenceTypeParameters.size() < newTypeParameters.size()) {
				for (int i = referenceTypeParameters.size(); i < newTypeParameters.size(); i++) {
					reporter.report(reference, newItem,
							DifferenceType.PARAMETRIZED_ADDED_TYPE_PARAMETER,
							newTypeParameters.get(i), reference);
				}
			}
		}
		
		// removed type parameter
		if (referenceTypeParameters.size() > newTypeParameters.size()) {
			for (int i = newTypeParameters.size(); i < referenceTypeParameters.size(); i++) {
				reporter.report(reference, newItem,
						DifferenceType.PARAMETRIZED_REMOVED_TYPE_PARAMETER,
						referenceTypeParameters.get(i), reference);
			}
		}
		
		// type bounds changed
		int minSize = Math.min(referenceTypeParameters.size(), newTypeParameters.size());
		for (int i = 0; i < minSize; i++) {
			if (!referenceTypeParameters.get(i).equals(newTypeParameters.get(i))) {
				reporter.report(reference, newItem,
						DifferenceType.PARAMETRIZED_CHANGED_BOUNDS,
						referenceTypeParameters.get(i), 
						newTypeParameters.get(i), 
						reference);
			}
		}

	}

}
