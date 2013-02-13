package com.googlecode.japi.checker.rules;

import java.util.List;

import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Rule;
import com.googlecode.japi.checker.Reporter.Level;
import com.googlecode.japi.checker.model.JavaItem;
import com.googlecode.japi.checker.model.Parametrized;
import com.googlecode.japi.checker.model.TypeParameterData;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
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
					reporter.report(new Reporter.Report(Level.ERROR,
							"The type parameter [" + newTypeParameters.get(i)
									+ "] has been added to the "
									+ reference
									+ ".", reference, newItem));
				}
			}
		}
		
		// removed type parameter
		if (referenceTypeParameters.size() > newTypeParameters.size()) {
			for (int i = newTypeParameters.size(); i < referenceTypeParameters.size(); i++) {
				reporter.report(new Reporter.Report(Level.ERROR,
						"The type parameter [" + referenceTypeParameters.get(i)
								+ "] has been removed from the "
								+ reference
								+ ".", reference, newItem));
			}
		}
		
		// type bounds changed
		int minSize = Math.min(referenceTypeParameters.size(), newTypeParameters.size());
		for (int i = 0; i < minSize; i++) {
			if (!referenceTypeParameters.get(i).equals(newTypeParameters.get(i))) {
				reporter.report(new Reporter.Report(Level.ERROR,
						"The bounds of the type parameter have been changed from ["
								+ referenceTypeParameters.get(i) + "] to ["
								+ newTypeParameters.get(i) + "] in the "
								+ reference
								+ ".", reference, newItem));
			}
		}

	}

}
