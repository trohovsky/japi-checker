package com.googlecode.japi.checker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
public class TypeParameterData {
	
	private final String name;
	private final List<String> bounds; // TODO structured parametrized type <T:Object;Comparable<Int>>
	
	protected TypeParameterData() {
		this.name = null;
		this.bounds = null;
	}
	
	public TypeParameterData(String name) {
		this.name = name;
		this.bounds = new ArrayList<String>();
	}
	
	public String getName() {
		return name;
	}
	
	public void addBound(String bound) {
		this.bounds.add(bound);
	}
	
	public List<String> getBounds() {
		return bounds;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TypeParameterData)) {
			return false;
		}
		TypeParameterData typeParameter = (TypeParameterData)obj;
		if (this.getBounds().size() != typeParameter.getBounds().size()) {
			return false;
		}
		for (int i = 0; i < this.getBounds().size(); i++) {
			if (!this.getBounds().get(i).equals(typeParameter.getBounds().get(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append(name);
		if (!bounds.isEmpty()) {
			s.append(" extends");
			boolean first = true;
			for (String bound : bounds) {
				if (first) {
					first = false;
					s.append(" ");
				} else {
					s.append(" & ");
				}
				s.append(bound);
			}	
		}
		return s.toString();
	}
}
