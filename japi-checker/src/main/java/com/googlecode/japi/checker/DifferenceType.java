package com.googlecode.japi.checker;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
public class DifferenceType {
	
	private final Severity serverity;
	private final boolean source; // affects only source compatiblity
	private final String message;
	private final String effect;
	
	private DifferenceType(Severity serverity, boolean source, String message, String effect) {
		this.serverity = serverity;
		this.source = source;
		this.message = message;
		this.effect = effect;
	}
	
	// general differences
	public final static DifferenceType GENERAL_DECREASED_VISIBILITY = new DifferenceType(Severity.ERROR, false, "The visibility of the %s has been changed from %s to %s", "");
	public final static DifferenceType GENERAL_INCREASED_VISIBILITY = new DifferenceType(Severity.INFO, false, "The visibility of the %s has been changed from %s to %s", "");
	
	// package differences
	public final static DifferenceType PACKAGE_REMOVED_CLASS = new DifferenceType(Severity.ERROR, false, "The %s has been removed", "");
	
	// class, interface, annotation and enum differences
	public final static DifferenceType CLASS_CHANGED_KIND_OF_API_TYPE = new DifferenceType(Severity.ERROR, false, "The %s has been changed into %s", "");
	public final static DifferenceType CLASS_ADDED_FIELD = new DifferenceType(Severity.ERROR, false, "The %s has been added", "");
	public final static DifferenceType CLASS_ADDED_ABSTRACT_METHOD = new DifferenceType(Severity.ERROR, false, "The abstract %s has been added", "");
	public final static DifferenceType CLASS_ADDED_STATIC_METHOD = new DifferenceType(Severity.WARNING, false, "The static %s has been added", "");
	public final static DifferenceType CLASS_ADDED_NON_ABSTRACT_NON_STATIC_METHOD = new DifferenceType(Severity.WARNING, false, "The non-abstract and non-static %s has been added", "");
	public final static DifferenceType ANNOTATION_ADDED_METHOD_WITH_NO_DEFAULT_VALUE = new DifferenceType(Severity.ERROR, true, "The %s without a default value has been added", "");
	public final static DifferenceType INTERFACE_ADDED_METHOD = new DifferenceType(Severity.ERROR, false, "The %s has been added", "");
	public final static DifferenceType CLASS_CHANGED_CLASS_VERSION = new DifferenceType(Severity.ERROR, false, "The Java version has changed from %s to %s, check you compiler target", "");
	public final static DifferenceType CLASS_CONTRACTED_SUPERCLASS_SET2 = new DifferenceType(Severity.ERROR, false, "The %s extends %s and not %s anymore", "");
	public final static DifferenceType CLASS_CONTRACTED_SUPERCLASS_SET = new DifferenceType(Severity.ERROR, false, "The superclass set of the %s has been contracted for %s", "");
	public final static DifferenceType CLASS_CONTRACTED_SUPERINTERFACE_SET = new DifferenceType(Severity.ERROR, false, "The superinterface set of the %s has been contracted for %s", "");
	public final static DifferenceType CLASS_CONTRACTED_SUPERINTERFACE_SET2 = new DifferenceType(Severity.ERROR, false, "The %s is not implementing %s anymore", "");
	public final static DifferenceType CLASS_REMOVED_FIELD = new DifferenceType(Severity.ERROR, false, "The %s has been removed", "");
	public final static DifferenceType CLASS_REMOVED_METHOD = new DifferenceType(Severity.ERROR, false, "The %s has been removed", "");
	public final static DifferenceType CLASS_CHANGED_SUPERCLASS = new DifferenceType(Severity.ERROR, false, "The %s does not inherit from %s anymore", "");
	public final static DifferenceType CLASS_CHANGED_TO_ABSTRACT = new DifferenceType(Severity.ERROR, false, "The %s has been made abstract", "");
	public final static DifferenceType CLASS_CHANGED_TO_FINAL = new DifferenceType(Severity.ERROR, false, "The %s has been made final", "");
	
	// serial version UID
	public final static DifferenceType CLASS_INVALID_SERIAL_VERSION_TYPE = new DifferenceType(Severity.ERROR, false, "The type for field serialVersionUID is invalid, it must be a long", "");
	public final static DifferenceType CLASS_CHANGED_SERIAL_VERSION_VALUE = new DifferenceType(Severity.ERROR, false, "The value of the serialVersionUID field has changed from %s to %s", "");
	
	// field differences
	public final static DifferenceType FIELD_CHANGED_TYPE = new DifferenceType(Severity.ERROR, false, "The type of the %s has been modified from %s to %s", "");
	public final static DifferenceType FIELD_CHANGED_TO_FINAL = new DifferenceType(Severity.ERROR, false, "The %s has been made final", "");
	public final static DifferenceType FIELD_CHANGED_TO_NON_FINAL = new DifferenceType(Severity.ERROR, false, "The %s has been made non-final (it has compile-time constant value)", "");
	public final static DifferenceType FIELD_CHANGED_TO_STATIC = new DifferenceType(Severity.ERROR, false, "The %s has been made static", "");
	public final static DifferenceType FIELD_CHANGED_TO_NON_STATIC = new DifferenceType(Severity.ERROR, false, "The %s has been made not-static", "");
	public final static DifferenceType FIELD_CHANGED_TO_TRANSIENT = new DifferenceType(Severity.WARNING, false, "The %s has been made transient.", "");
	public final static DifferenceType FIELD_CHANGED_TO_NON_TRANSIENT = new DifferenceType(Severity.ERROR, false, "The %s has been made non-transient", "");
	public final static DifferenceType FIELD_CHANGED_VALUE = new DifferenceType(Severity.ERROR, false, "The value of the %s has been changed from %s to %s", "");
	
	// method differences
	public final static DifferenceType METHOD_CHANGED_TO_ABSTRACT = new DifferenceType(Severity.ERROR, false, "The %s has been made abstract", "");
	public final static DifferenceType METHOD_CHANGED_TO_FINAL = new DifferenceType(Severity.ERROR, false, "The %s has been made final", "");
	public final static DifferenceType METHOD_CHANGED_TO_STATIC = new DifferenceType(Severity.ERROR, false, "The %s has been made static", "");
	public final static DifferenceType METHOD_CHANGED_TO_NON_STATIC = new DifferenceType(Severity.ERROR, false, "The %s has been made non-static", "");
	public final static DifferenceType METHOD_REMOVED_DEFAULT_VALUE = new DifferenceType(Severity.ERROR, false, "The default value of the %s has been removed", "");
	public final static DifferenceType METHOD_ADDED_EXCEPTION = new DifferenceType(Severity.ERROR, true, "The %s is now throwing %s", "");
	public final static DifferenceType METHOD_REMOVED_EXCEPTION = new DifferenceType(Severity.ERROR, true, "The %s is not throwing %s anymore", "");
	public final static DifferenceType METHOD_VARARG_PARAM_TO_ARRAY = new DifferenceType(Severity.ERROR, true, "The parameter of the %s has been changed from variable arity to array", "");
	
	// type parameter differences
	public final static DifferenceType PARAMETRIZED_ADDED_TYPE_PARAMETER = new DifferenceType(Severity.ERROR, true, "The type parameter [%s] has been added to the %s", "");
	public final static DifferenceType PARAMETRIZED_REMOVED_TYPE_PARAMETER = new DifferenceType(Severity.ERROR, true, "The type parameter [%s] has been removed from the %s", "");
	public final static DifferenceType PARAMETRIZED_CHANGED_BOUNDS = new DifferenceType(Severity.ERROR, true, "The bounds of the type parameter have been changed from [%s] to [%s] in the %s", "");
	
	public Severity getServerity() {
		return serverity;
	}

	public boolean isSource() {
		return source;
	}

	public String getMessage() {
		return message;
	}

	public String getEffect() {
		return effect;
	}

}
