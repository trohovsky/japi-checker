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

import org.objectweb.asm.Opcodes;

import com.googlecode.japi.checker.ClassDataLoader;
import com.googlecode.japi.checker.Scope;

public abstract class JavaItem {

	private String name;
	private int access;
	private Scope visibility;
    private ClassData owner;
    private ClassDataLoader classDataLoader;
    
	protected JavaItem() {
    	
    }

    protected JavaItem(ClassDataLoader loader, ClassData owner, int access, String name) {
    	this.setAccess(access);
        this.setOwner(owner);
        this.setName(name);
        this.setClassDataLoader(loader);
    }

    public Scope getVisibility() {
        return visibility;
    }
    
    private Scope toScope(int access) {
        if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
            return Scope.PRIVATE;
        } else if ((access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED) {
            return Scope.PROTECTED;
        } if ((access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC) {
            return Scope.PUBLIC;
        } else {
            return Scope.PACKAGE;
        }
    }

    /**
     * @return the isAbstract
     */
    public boolean isAbstract() {
        return (access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT;
    }

    /**
     * @return the isFinal
     */
    public boolean isFinal() {
        return (access & Opcodes.ACC_FINAL) == Opcodes.ACC_FINAL;
    }

    /**
     * @return the isStatic
     */
    public boolean isStatic() {
        return (access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC;
    }

    /**
     * @return the isTransient
     */
    public boolean isTransient() {
        return (access & Opcodes.ACC_TRANSIENT) == Opcodes.ACC_TRANSIENT;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    protected void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the owner
     */
    public ClassData getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    protected void setOwner(ClassData owner) {
        this.owner = owner;
    }

    public abstract String getItemType();
    
    /**
     * Display the item name.
     * {@inheritDoc}
     */
    public String toString() {
        return this.getItemType() + " " + name;
    }

    /**
     * Get the ClassDataLoader associated with this instance.
     * {@inheritDoc}
     */
    public ClassDataLoader getClassDataLoader() {
        return classDataLoader;
    }

    /**
     * Get the ClassDataLoader associated with this instance.
     * {@inheritDoc}
     */
    protected void setClassDataLoader(ClassDataLoader loader) {
        classDataLoader = loader;
    }
    
    public int getAccess() {
		return access;
	}

	protected void setAccess(int access) {
		this.access = access;
		this.visibility = toScope(access);
	}
}
