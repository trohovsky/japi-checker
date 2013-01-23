package com.googlecode.japi.checker.rules;

import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Rule;
import com.googlecode.japi.checker.Scope;
import com.googlecode.japi.checker.Reporter.Level;
import com.googlecode.japi.checker.Reporter.Report;
import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.JavaItem;
import com.googlecode.japi.checker.model.MethodData;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
//CLASS
public class CheckAddedMethod implements Rule {

	@Override
	public void checkBackwardCompatibility(Reporter reporter, JavaItem reference, JavaItem newItem) {
     
		ClassData referenceClass = (ClassData)reference;
		ClassData newClass = (ClassData)newItem;
		
		if (!referenceClass.isInterface() && !newClass.isInterface()) {
			for (MethodData newMethod: newClass.getMethods()) {
				if (newMethod.getVisibility().isMoreVisibleThan(Scope.PACKAGE)) {
				
					boolean found = false;
					for (MethodData oldMethod : referenceClass.getMethods()) {
						if (oldMethod.isSame(newMethod) && oldMethod.getVisibility().isMoreVisibleThan(Scope.PACKAGE)) {
							found = true;
							break;
						}
					}
					
					if (!found) {
						// class is subclassable, TODO maybe check public constructor
						if (!newClass.isFinal()) {

							if (newMethod.isAbstract()) {
								reporter.report(new Report(Level.ERROR, "Added abstract " 
										+ newMethod.getType() + " "
										+ newMethod.getName() + ".",
										reference, newItem));
							} else if (newMethod.isStatic()) {
								reporter.report(new Report(Level.WARNING, "Added static "
										+ newMethod.getType() + " "
										+ newMethod.getName() + ".",
										reference, newItem));
							} else {
								reporter.report(new Report(Level.WARNING, "Added non-abstract and non-static "
										+ newMethod.getType() + " "
										+ newMethod.getName() + ".",
										reference, newItem));
							}
						}
					}
				}
			}
		}
	}
	
 }    
