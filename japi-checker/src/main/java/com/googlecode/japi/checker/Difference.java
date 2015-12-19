package com.googlecode.japi.checker;

import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.JavaItem;
import com.googlecode.japi.checker.model.MethodData;

/**
 * @author Tomas Rohovsky
 */
public class Difference {

	private DifferenceType differenceType;
	private JavaItem referenceItem;
	private JavaItem newItem;
	private String[] args;
	private String source;

	protected Difference() {
	}

	public Difference(JavaItem referenceItem, JavaItem newItem, DifferenceType differenceType) {
		this.referenceItem = referenceItem;
		this.newItem = newItem;
		this.differenceType = differenceType;
		this.source = (referenceItem.getOwner() == null ? ((ClassData) referenceItem).getFilename() : referenceItem.getOwner().getFilename());
	}

	public Difference(JavaItem referenceItem, JavaItem newItem, DifferenceType differenceType, Object... args) {
		this(referenceItem, newItem, differenceType);
		// objects to strings
		String[] stringArgs = new String[args.length];
		for (int i = 0; i < args.length; i++) {
			// TODO quickly fixed to prevent null exception, which occur when field's value is changed to null
			if (args[i] != null) {
				stringArgs[i] = args[i].toString();
			} else {
				stringArgs[i] = "null";
			}
		}
		this.args = stringArgs;
	}

	public JavaItem getReferenceItem() {
		return referenceItem;
	}

	public JavaItem getNewItem() {
		return newItem;
	}

	public String[] getArgs() {
		return args;
	}

	public String getSource() {
		return source;
	}
	
    /* Methods encapsulating difference type */

	public Severity getSeverity() {
		return differenceType.getSeverity();
	}

	public boolean isSourceIncompatible() {
		return differenceType.isSourceIncompatible();
	}

	public String getMessage() {
		return String.format(differenceType.getMessagePattern(), (Object[]) args);
	}

	public String getEffect() {
		return String.format(differenceType.getMessagePattern(), (Object[]) args);
	}
	
	/* Other methods */

	public Integer getLine() {
		if (newItem instanceof MethodData) {
			Integer lineNumber = ((MethodData) newItem).getLineNumber();
			return lineNumber;
		}
		return null;
	}

}
