package com.googlecode.japi.checker.model;

import java.util.List;

/**
 * @author Tomas Rohovsky
 */
public interface Parametrized {
	public void add(TypeParameterData typeParameter);

	public List<TypeParameterData> getTypeParameters();
}
