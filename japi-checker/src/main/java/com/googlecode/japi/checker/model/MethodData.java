/*
 * Copyright 2011 William Bernardet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.japi.checker.model;

import com.googlecode.japi.checker.Utils;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MethodData extends JavaItem implements Parametrized {

	private final String descriptor;
	private List<String> exceptions = new ArrayList<String>();
	private Integer line;
	private String defaultValue;
	private List<TypeParameterData> typeParameters = new ArrayList<TypeParameterData>();

	protected MethodData() {
		this.descriptor = null;
	}

	public MethodData(ClassData owner, int access, String name, String descriptor, String[] exceptions) { // String signature,
		super(owner, access, name);
		this.descriptor = descriptor;
		if (exceptions != null) {
			Collections.addAll(this.exceptions, exceptions);
		}
	}

	public boolean isSame(MethodData method) {
		return this.getName().equals(method.getName()) && this.getDescriptor().equals(method.getDescriptor());
	}

	@Override
	public String getItemType() {
		return this.isConstructor() ? "constructor" : "method";
	}

	/**
	 * @return the descriptor
	 */
	public String getDescriptor() {
		return descriptor;
	}

	/**
	 * @return types of the arguments
	 */
	public Type[] getParameterTypes() {
		return Type.getArgumentTypes(descriptor);
	}

	/**
	 * @return return type
	 */
	public Type getReturnType() {
		return Type.getReturnType(descriptor);
	}

	/**
	 * @return the exceptions
	 */
	public List<String> getExceptions() {
		return exceptions;
	}

	/**
	 * @return the isVariableArity
	 */
	public boolean isVariableArity() {
		return (this.getAccess() & Opcodes.ACC_VARARGS) == Opcodes.ACC_VARARGS;
	}

	/**
	 * @return the line number of appearance in the source file.
	 */
	public Integer getLineNumber() {
		return line;
	}

	/**
	 * Set the line number of appearance in the source file.
	 */
	public void setLineNumber(Integer line) {
		this.line = line;
	}

	/**
	 * @return the default value for the annotation member represented by this method.
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Set the default value for the annotation member represented by this method.
	 */
	public void setDefaultValue(String value) {
		this.defaultValue = value;
	}

	/**
	 * Add the type parameter.
	 */
	@Override
	public void add(TypeParameterData typeParameter) {
		typeParameters.add(typeParameter);
	}

	/**
	 * @return the type parameters
	 */
	@Override
	public List<TypeParameterData> getTypeParameters() {
		return typeParameters;
	}

	/**
	 * @return true if this method is constructor; false otherwise.
	 */
	public boolean isConstructor() {
		return this.getName().equals("<init>");
	}

	/**
	 * @return the parameter types as a string.
	 */
	private String getParameterTypesString() {
		StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for (Type parameterType : this.getParameterTypes()) {
			if (first) {
				first = false;
			} else {
				buffer.append(", ");
			}
			buffer.append(parameterType.getClassName());
		}
		return buffer.toString();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getItemType());
		sb.append(" ");
		if (!this.getTypeParameters().isEmpty()) {
			sb.append("<");
			sb.append(Utils.join(this.getTypeParameters(), ", "));
			sb.append("> ");
		}
		sb.append(this.getName());
		sb.append("(");
		sb.append(getParameterTypesString());
		sb.append(")");
		return sb.toString();
	}

}
