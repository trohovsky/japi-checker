package com.googlecode.japi.checker;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
public class DifferenceType {
	
	private final Severity serverity;
	private final boolean affectingSourceCompatibility;
	private final String message;
	private final String effect;
	
	private DifferenceType(Severity serverity, boolean affectingSourceCompatibility, String message, String effect) {
		this.serverity = serverity;
		this.affectingSourceCompatibility = affectingSourceCompatibility;
		this.message = message;
		this.effect = effect;
	}
		
	public final static DifferenceType DIFERENCE_CHANGE_KIND_OF_API_TYPE = new DifferenceType(Severity.ERROR, false, "%s has been changed into %s ", "no description");
	
	public Severity getServerity() {
		return serverity;
	}

	public boolean isAffectingSourceCompatibility() {
		return affectingSourceCompatibility;
	}

	public String getMessage() {
		return message;
	}

	public String getEffect() {
		return effect;
	}

}
