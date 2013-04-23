package com.googlecode.japi.checker.rules;

import com.googlecode.japi.checker.DifferenceType;
import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.JavaItem;
import com.googlecode.japi.checker.model.MethodData;
import com.googlecode.japi.checker.model.Scope;

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
				if (newMethod.getVisibility().isMoreVisibleThan(Scope.PACKAGE) && !newMethod.isConstructor()) {
				
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
								reporter.report(reference,
										newItem,
										DifferenceType.CLASS_ADDED_ABSTRACT_METHOD,
										newMethod);
							} else if (newMethod.isStatic()) {
								reporter.report(reference,
										newItem,
										DifferenceType.CLASS_ADDED_STATIC_METHOD,
										newMethod);
							} else {
								reporter.report(
										reference,
										newItem,
										DifferenceType.CLASS_ADDED_NON_ABSTRACT_NON_STATIC_METHOD,
										newMethod);
							}
						}
					}
				}
			}
		} else if (referenceClass.isInterface() && newClass.isInterface()) {
			for (MethodData newMethod: newClass.getMethods()) {
				// all interface methods are implicitly public and abstract - no need to check if are API
				boolean found = false;
				
				for (MethodData oldMethod : referenceClass.getMethods()) {
					if (oldMethod.isSame(newMethod) && oldMethod.getVisibility().isMoreVisibleThan(Scope.PACKAGE)) {
						found = true;
						break;
					}
				}
				
				if (!found) {
					if (referenceClass.isAnnotation() && newClass.isAnnotation()) {
						if (newMethod.getDefaultValue() == null) {
							reporter.report(
									reference,
									newItem,
									DifferenceType.ANNOTATION_ADDED_METHOD_WITH_NO_DEFAULT_VALUE,
									newMethod);
						}
					} else {
						reporter.report(reference, newItem,
								DifferenceType.INTERFACE_ADDED_METHOD,
								newMethod);
					}
				}
			}
			
		}
	}
	
 }    
