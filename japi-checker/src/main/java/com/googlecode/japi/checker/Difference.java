package com.googlecode.japi.checker;

import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.JavaItem;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
public class Difference {

	private final DifferenceType differenceType;
	private final JavaItem referenceItem;
	private final JavaItem newItem;
	private final String[] args;
	private final String source;
	
	public Difference(JavaItem referenceItem, JavaItem newItem, DifferenceType differenceType, String... args) {
		this.referenceItem = referenceItem;
		this.newItem = newItem;
		this.differenceType = differenceType;
		this.args = args;
		this.source = (referenceItem.getOwner() == null ? ((ClassData)referenceItem).getFilename() : referenceItem.getOwner().getFilename());
	}

	public DifferenceType getDifferenceType() {
		return differenceType;
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
	
	public String getMessage() {
		return String.format(differenceType.getMessage(), (Object[])args);
	}
	
	public String getEffect() {
		return String.format(differenceType.getMessage(), (Object[])args);
	}

}
