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
	private String[] args;
	private final String source;
	
	protected Difference() {
		this.differenceType = null;
		this.referenceItem = null;
		this.newItem = null;
		this.source = null;
	}
	
	public Difference(JavaItem referenceItem, JavaItem newItem, DifferenceType differenceType) {
		this.referenceItem = referenceItem;
		this.newItem = newItem;
		this.differenceType = differenceType;
		this.source = (referenceItem.getOwner() == null ? ((ClassData)referenceItem).getFilename() : referenceItem.getOwner().getFilename());
		this.args = null;
	}
		
	public Difference(JavaItem referenceItem, JavaItem newItem, DifferenceType differenceType, String... args) {
		this(referenceItem, newItem, differenceType);
		this.args = args;
	}
	
	public Difference(JavaItem referenceItem, JavaItem newItem, DifferenceType differenceType, Object...args) {
		this(referenceItem, newItem, differenceType);
		// objects to string
		String[] stringArgs = new String[args.length];
		for (int i = 0; i < args.length; i++) {
			stringArgs[i] = args[i].toString();
		}
		this.args = stringArgs;
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
		return String.format(differenceType.getMessagePattern(), (Object[])args);
	}
	
	public String getEffect() {
		return String.format(differenceType.getMessagePattern(), (Object[])args);
	}

}
