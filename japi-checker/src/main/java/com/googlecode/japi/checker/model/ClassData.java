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
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.objectweb.asm.Opcodes;

import com.googlecode.japi.checker.ClassDataLoader;
import com.googlecode.japi.checker.Utils;

public class ClassData extends JavaItem implements Parametrized {

    private final String superName;
    private final List<String> interfaces = new ArrayList<String>();
    private final int version;
    private final List<MethodData> methods = new ArrayList<MethodData>();
    private final List<FieldData> fields = new ArrayList<FieldData>();
    private final List<AttributeData> attributes = new ArrayList<AttributeData>();
    private final List<InnerClassData> innerClasses = new ArrayList<InnerClassData>();
    private final List<TypeParameterData> typeParameters = new ArrayList<TypeParameterData>();
	private String source;
	
	protected ClassData() {
		this.superName = null;
		this.version = 0;
	}

    public ClassData(ClassDataLoader loader, ClassData owner, int access, String name, String superName, String[] interfaces, int version) { 
        super(loader, owner, access, name);
        this.superName = superName;
        Collections.addAll(this.interfaces, interfaces);
        this.version = version;
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
    	typeParameters.add(typeParameter);
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
     * @return the superName
     */
    public String getSuperName() {
    	return superName;
    }
    
    /**
     * @return the names of all super classes
     */
    public List<String> getSuperClasses() {
    	List<String> superClasses = new ArrayList<String>();
    	superClasses.add(this.getSuperName());
    	
    	ClassData superClass = this.getClassDataLoader().fromName(this.getSuperName());
    	while (superClass != null) {
    		superClasses.add(superClass.getSuperName());
    		superClass = this.getClassDataLoader().fromName(superClass.getSuperName());
    	}
    	
    	return superClasses;
    }

    /**
     * @return the interfaces
     */
    public List<String> getInterfaces() {
        return interfaces;
    }
    
    /**
     * @return the names of alll interfaces 
     */
    public Set<String> getAllInterfaces() {
    	// SortedSet provide immutable order of interfaces in the output
    	SortedSet<String> allInterfaces = new TreeSet<String>();
    	
    	// my interfaces
    	allInterfaces.addAll(this.getInterfaces());
    	
    	// interfaces implemented by super classes
    	if (!this.isInterface()) {
			List<String> superClassNames = this.getSuperClasses();
			for (String superClassName : superClassNames) {
				ClassData superClass = this.getClassDataLoader().fromName(
						superClassName);
				if (superClass != null) {
					allInterfaces.addAll(superClass.getInterfaces());
				}
			}
		}
    	
    	// interfaces extended by interfaces (my and super classes')
    	SortedSet<String> inputInterfaces = new TreeSet<String>(allInterfaces);
    	SortedSet<String> outputInterfaces = new TreeSet<String>();
    	while (inputInterfaces.size() != 0) {
    		for (String interfaceName : inputInterfaces) {
    			ClassData iface = this.getClassDataLoader().fromName(interfaceName);
    			if (iface != null) {
    				outputInterfaces.addAll(iface.getInterfaces());
    			}
    		}
    		// preparation for the next iteration
    		// remove all interfaces, which have been already collected
    		outputInterfaces.removeAll(allInterfaces);
    		inputInterfaces.clear();
    		inputInterfaces.addAll(outputInterfaces);
    		allInterfaces.addAll(outputInterfaces);
    		outputInterfaces.clear();
    	}
    	
    	return allInterfaces;
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
    	return typeParameters;
    }

    public boolean isInterface() {
		return (this.getAccess() & Opcodes.ACC_INTERFACE) == Opcodes.ACC_INTERFACE;
	}
	
	public boolean isEnum() {
		return (this.getAccess() & Opcodes.ACC_ENUM) == Opcodes.ACC_ENUM;
	}
	
	public boolean isAnnotation() {
		return (this.getAccess() & Opcodes.ACC_ANNOTATION) == Opcodes.ACC_ANNOTATION;
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
        if (this.getName().lastIndexOf('.') != -1) {
            return Utils.toSlashedClassName(this.getName().substring(0, this.getName().lastIndexOf('.') + 1)) + getSource();
        }
        return this.getSource();
    }
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getItemType());
		sb.append(" ");
		sb.append(this.getName());
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
		return sb.toString();
	}
}
