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

import org.objectweb.asm.Type;


public class FieldData extends JavaItem {
	
    private final String descriptor;
    private final String value;

    protected FieldData() {
    	this.descriptor = null;
    	this.value = null;
    }
    
    public FieldData(ClassData owner, int access, String name, String descriptor, String value) { // String signature,
        super(owner, access, name);
        this.descriptor = descriptor;
        this.value = value;
    }

    /**
     * @return the description
     */
    public String getDescriptor() {
        return descriptor;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
    /**
     * @return the type
     */
    public Type getType() {
    	return Type.getType(getDescriptor());
    }
    
    /**
     * Finds out whether the field is compile-time constant.
     * @return
     */
    public boolean isCompileTimeConstant() {
    	return this.isFinal() && this.getValue() != null;
    }
    
    public boolean isSame(FieldData reference) {
        return this.getName().equals(reference.getName());
    }

    @Override
    public String getItemType() {
        return "field";
    }
    
}
