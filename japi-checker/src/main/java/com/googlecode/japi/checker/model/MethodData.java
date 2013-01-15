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

import org.objectweb.asm.Type;

import com.googlecode.japi.checker.ClassDataLoader;
import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Rule;

public class MethodData extends JavaItem {
    private final String signature;
    private final String descriptor;
    private final Type[] argumentTypes;
    private final Type returnType;
	private List<String> exceptions = new ArrayList<String>();
    private int line;
    
    public MethodData(ClassDataLoader loader, ClassData owner, int access, String name, String descriptor, String signature, String[] exceptions) {
        super(loader, owner, access, name);
        this.signature = signature;
        this.descriptor = descriptor;
        this.argumentTypes = Type.getArgumentTypes(descriptor);
        this.returnType = Type.getReturnType(descriptor);
        if (exceptions != null) {
            Collections.addAll(this.exceptions, exceptions);
        }
        this.exceptions = Collections.unmodifiableList(this.exceptions);
    }

    public void checkBackwardCompatibility(Reporter reporter, MethodData method, List<Rule> rules) {
    }

    public boolean isSame(MethodData method) {
        if (method == null) {
            return false;
        }
        return this.getName().equals(method.getName()) && this.getDescriptor().equals(method.getDescriptor());
    }
    
    @Override
    public String getType() {
        return "method";
    }

    /**
     * @return the signature
     */
    public String getSignature() {
        return signature;
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
    public Type[] getArgumentTypes() {
		return argumentTypes;
	}

    /**
     * @return return type
     */
	public Type getReturnType() {
		return returnType;
	}

    /**
     * @return the exceptions
     */
    public List<String> getExceptions() {
        return exceptions;
    }

    public void setLineNumber(int line) {
        this.line = line;
    }
    
    public int getLineNumber() {
        return line;
    }
}
