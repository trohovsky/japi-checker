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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.objectweb.asm.Opcodes;

import com.googlecode.japi.checker.ClassDataLoader;

public class ClassData extends JavaItem implements Parametrized {
    private final String signature;
    private final String superName;
    private final List<String> interfaces = new ArrayList<String>();
    private final int version;
    private final List<MethodData> methods = new ArrayList<MethodData>();
    private final List<FieldData> fields = new ArrayList<FieldData>();
    private final List<AttributeData> attributes = new ArrayList<AttributeData>();
    private final List<InnerClassData> innerClasses = new ArrayList<InnerClassData>();
    private final List<TypeParameterData> typeParameteres = new ArrayList<TypeParameterData>();
    private final boolean isInterface;
    private final boolean isEnum;
	private final boolean isAnnotation;
	private String source;

    public ClassData(ClassDataLoader loader, ClassData owner, int access, String name, String signature, String superName, String[] interfaces, int version) {
        super(loader, owner, access, name);
        this.signature = signature;
        this.superName = superName;
        Collections.addAll(this.interfaces, interfaces);
        this.version = version;
        this.isInterface = (access & Opcodes.ACC_INTERFACE) == Opcodes.ACC_INTERFACE;
        this.isEnum = (access & Opcodes.ACC_ENUM) == Opcodes.ACC_ENUM;
        this.isAnnotation = (access & Opcodes.ACC_ANNOTATION) == Opcodes.ACC_ANNOTATION;
    }
    
    public void add(MethodData method) {
        methods.add(method);
    }
    
    public void add(FieldData field) {
        fields.add(field);
    }
    
    public void add(AttributeData attribute) {
        attributes.add(attribute);
    }
    
    public void add(InnerClassData clazz) {
        innerClasses.add(clazz);
    }
    
    @Override
    public void add(TypeParameterData typeParameter) {
    	typeParameteres.add(typeParameter);
    }
    
    public boolean isSame(ClassData newClazz) {
        return this.getName().equals(newClazz.getName());
    }
    
    @Override
    public String getItemType() {
        if (this.isInterface()) {
        	return "interface";
        } else if (this.isEnum()) {
        	return "enum";
        } else if (this.isAnnotation()) {
        	return "annotation";
        } else {
        	return "class";
        }
    }

    /**
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }
    
    /**
     * @return the superName
     */
    public String getSuperName() {
    	return superName;
    }

    /**
     * @return the interfaces
     */
    public List<String> getInterfaces() {
        return interfaces;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @return the methods
     */
    public List<MethodData> getMethods() {
        return methods;
    }

    /**
     * @return the fields
     */
    public List<FieldData> getFields() {
        return fields;
    }

    /**
     * @return the attributes
     */
    public List<AttributeData> getAttributes() {
        return attributes;
    }
    
    /**
     * @return the inner classes
     */
    public List<InnerClassData> getInnerClasses() {
    	return innerClasses;
    }
    
    /**
     * @return the type parameters
     */
    public List<TypeParameterData> getTypeParameters() {
    	return typeParameteres;
    }

    public boolean isInterface() {
		return isInterface;
	}
	
	public boolean isEnum() {
		return isEnum;
	}
	
	public boolean isAnnotation() {
		return isAnnotation;
	}
	
    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

	public String getFilename() {
        if (this.getName().lastIndexOf('/') != -1) {
            return this.getName().substring(0, this.getName().lastIndexOf('/') + 1) + getSource();
        }
        return this.getSource();
    }
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (!this.getTypeParameters().isEmpty()) {
			sb.append("<");
			boolean first = true;
			for (TypeParameterData typeParameter : this.getTypeParameters()) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}	
				sb.append(typeParameter);
			}
			sb.append(">");
		}
		return this.getName() + sb.toString();
	}
}
