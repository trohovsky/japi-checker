package com.googlecode.japi.checker;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
public enum DifferenceType {
	
	// general differences
	GENERAL_DECREASED_VISIBILITY(Severity.ERROR, false, "The visibility of the %s has been changed from %s to %s", ""),
	GENERAL_INCREASED_VISIBILITY(Severity.INFO, false, "The visibility of the %s has been changed from %s to %s", ""),
	
	// package differences
	PACKAGE_REMOVED_CLASS(Severity.ERROR, false, "The %s has been removed", ""),
	
	// class, interface, annotation and enum differences
	CLASS_CHANGED_KIND_OF_API_TYPE(Severity.ERROR, false, "The %s has been changed into %s", ""),
	CLASS_ADDED_FIELD(Severity.ERROR, false, "The %s has been added", ""),
	CLASS_ADDED_ABSTRACT_METHOD(Severity.ERROR, false, "The abstract %s has been added", ""),
	CLASS_ADDED_STATIC_METHOD(Severity.WARNING, false, "The static %s has been added", ""),
	CLASS_ADDED_NON_ABSTRACT_NON_STATIC_METHOD(Severity.WARNING, false, "The non-abstract and non-static %s has been added", ""),
	ANNOTATION_ADDED_METHOD_WITH_NO_DEFAULT_VALUE(Severity.ERROR, true, "The %s without a default value has been added", ""),
	INTERFACE_ADDED_METHOD(Severity.ERROR, false, "The %s has been added", ""),
	CLASS_CHANGED_CLASS_VERSION(Severity.ERROR, false, "The Java version has changed from %s to %s, check you compiler target", ""),
	CLASS_CONTRACTED_SUPERCLASS_SET(Severity.ERROR, false, "The superclass set of the %s has been contracted for %s", ""),
	CLASS_CONTRACTED_SUPERINTERFACE_SET(Severity.ERROR, false, "The superinterface set of the %s has been contracted for %s", ""),
	CLASS_REMOVED_FIELD(Severity.ERROR, false, "The %s has been removed", ""),
	CLASS_REMOVED_METHOD(Severity.ERROR, false, "The %s has been removed", ""),
	CLASS_CHANGED_SUPERCLASS(Severity.ERROR, false, "The %s does not inherit from %s anymore", ""),
	CLASS_CHANGED_TO_ABSTRACT(Severity.ERROR, false, "The %s has been made abstract", ""),
	CLASS_CHANGED_TO_FINAL(Severity.ERROR, false, "The %s has been made final", ""),
		
	// serial version UID
	CLASS_INVALID_SERIAL_VERSION_TYPE(Severity.ERROR, false, "The type for field serialVersionUID is invalid, it must be a long", ""),
	CLASS_CHANGED_SERIAL_VERSION_VALUE(Severity.ERROR, false, "The value of the serialVersionUID field has changed from %s to %s", ""),
		
	// field differences
	FIELD_CHANGED_TYPE(Severity.ERROR, false, "The type of the %s has been modified from %s to %s", ""),
	FIELD_CHANGED_TO_FINAL(Severity.ERROR, false, "The %s has been made final", ""),
	FIELD_CHANGED_TO_NON_FINAL(Severity.ERROR, false, "The %s has been made non-final (it has compile-time constant value)", ""),
	FIELD_CHANGED_TO_STATIC(Severity.ERROR, false, "The %s has been made static", ""),
	FIELD_CHANGED_TO_NON_STATIC(Severity.ERROR, false, "The %s has been made non-static", ""),
	FIELD_CHANGED_TO_TRANSIENT(Severity.WARNING, false, "The %s has been made transient", ""),
	FIELD_CHANGED_TO_NON_TRANSIENT(Severity.ERROR, false, "The %s has been made non-transient", ""),
	FIELD_CHANGED_VALUE(Severity.ERROR, false, "The value of the %s has been changed from %s to %s", ""),
		
	// method differences
	METHOD_CHANGED_TO_ABSTRACT(Severity.ERROR, false, "The %s has been made abstract", ""),
	METHOD_CHANGED_TO_FINAL(Severity.ERROR, false, "The %s has been made final", ""),
	METHOD_CHANGED_TO_STATIC(Severity.ERROR, false, "The %s has been made static", ""),
	METHOD_CHANGED_TO_NON_STATIC(Severity.ERROR, false, "The %s has been made non-static", ""),
	METHOD_REMOVED_DEFAULT_VALUE(Severity.ERROR, false, "The default value of the %s has been removed", ""),
	METHOD_ADDED_EXCEPTION(Severity.ERROR, true, "The %s is now throwing %s", ""),
	METHOD_REMOVED_EXCEPTION(Severity.ERROR, true, "The %s is not throwing %s anymore", ""),
	METHOD_VARARG_PARAM_TO_ARRAY(Severity.ERROR, true, "The parameter of the %s has been changed from variable arity to array", ""),

	// type parameter differences
	PARAMETRIZED_ADDED_TYPE_PARAMETER(Severity.ERROR, true, "The type parameter [%s] has been added to the %s", ""),
	PARAMETRIZED_REMOVED_TYPE_PARAMETER(Severity.ERROR, true, "The type parameter [%s] has been removed from the %s", ""),
	PARAMETRIZED_CHANGED_BOUNDS(Severity.ERROR, true, "The bounds of the type parameter have been changed from [%s] to [%s] in the %s", "");
	
	private final Severity severity;
	private final boolean sourceIncompatible; // true if affects only source compatibility, false otherwise
	private final String messagePattern;
	private final String effectPattern;
	
	private DifferenceType(Severity serverity, boolean sourceIncompatible, String messagePattern, String effectPattern) {
		this.severity = serverity;
		this.sourceIncompatible = sourceIncompatible;
		this.messagePattern = messagePattern;
		this.effectPattern = effectPattern;
	}
			
	public Severity getSeverity() {
		return severity;
	}

	public boolean isSourceIncompatible() {
		return sourceIncompatible;
	}

	public String getMessagePattern() {
		return messagePattern;
	}

	public String getEffectPattern() {
		return effectPattern;
	}

}
