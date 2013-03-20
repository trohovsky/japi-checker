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
package com.googlecode.japi.checker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Logger;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;

import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.FieldData;
import com.googlecode.japi.checker.model.InnerClassData;
import com.googlecode.japi.checker.model.MethodData;
import com.googlecode.japi.checker.model.TypeParameterData;

class ClassDumper extends ClassVisitor {

	private ClassDataLoader<?> loader;
	private Logger logger = Logger.getLogger(ClassDumper.class.getName());
	private ClassData clazz; // current main class being parsed.
	private ClassData returnClass;
	private Constructor<ClassData> classConstructor;
	private Constructor<FieldData> fieldConstructor;
	private Constructor<MethodData> methodConstructor;
	private Constructor<TypeParameterData> typeParameterConstructor;
        
    /**
     * Create a new visitor instance.
     * @param loader the ClassDataLoader to which the model are associated.
     */
    public ClassDumper(ClassDataLoader<?> loader) {
        this(loader, ClassData.class, FieldData.class, MethodData.class, TypeParameterData.class);
    }
    
    public ClassDumper(ClassDataLoader<?> loader, 
    		Class<ClassData> classClass, 
    		Class<FieldData> fieldClass, 
    		Class<MethodData> methodClass, 
    		Class<TypeParameterData> typeParameterClass) {
        super(Opcodes.ASM4);
        this.loader = loader;
        try {
			this.classConstructor = classClass.getConstructor(ClassDataLoader.class, ClassData.class, int.class, String.class, String.class, String[].class, int.class);
			this.fieldConstructor = fieldClass.getConstructor(ClassDataLoader.class, ClassData.class, int.class, String.class, String.class, String.class);
			this.methodConstructor = methodClass.getConstructor(ClassDataLoader.class, ClassData.class, int.class, String.class, String.class, String[].class);
			this.typeParameterConstructor = typeParameterClass.getConstructor(String.class);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * {@inheritDoc}
     */
    public void visit(int version, int access, String name, String signature,
            String superName, String[] interfaces) {
        String dottedName = Utils.toDottedClassName(name);
        String dottedSuperName = Utils.toDottedClassName(superName);
        String[] dottedInterfaces = Utils.toDottedClassNames(interfaces);
        logger.fine("class " + dottedName + " extends " + dottedSuperName + " {");
        //clazz = new ClassData(loader, null, access, dottedName, dottedSuperName, dottedInterfaces, version);
        try { 
			clazz = classConstructor.newInstance(loader, null, access, dottedName, dottedSuperName, dottedInterfaces, version);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (signature != null) {
            new SignatureReader(signature).accept(new TypeParameterDumper(clazz, typeParameterConstructor));
        }
    }

    /**
     * {@inheritDoc}
     */
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }

    public void visitAttribute(Attribute attribute) {
    }

    public void visitEnd() {
        logger.fine("}");
        returnClass = clazz;
        clazz = null;
    }

    public FieldVisitor visitField(int access, String name, String desc,
            String signature, Object value) {
        logger.fine("    -(field) " + name + " " + signature + " " + desc);
        //clazz.add(new FieldData(loader, clazz, access, name, desc, value));
		try {
			String stringValue = null;
			if (value != null) {
				stringValue = value.toString();
			}
			FieldData field = fieldConstructor.newInstance(loader, clazz, access, name, desc, stringValue);
			clazz.add(field);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    	String dottedName = Utils.toDottedClassName(name);
    	String dottedOuterName = Utils.toDottedClassName(outerName);
    	String dottedInnerName = Utils.toDottedClassName(innerName);
        logger.fine("    +(ic) " + dottedName + " " + dottedOuterName + " " + dottedInnerName + " " + access);
        //clazz = new ClassData(access, name, innerName);
        clazz.add(new InnerClassData(loader, clazz, access, dottedName, dottedOuterName, dottedInnerName));
    }

    public MethodVisitor visitMethod(int access, String name, String descriptor,
            String signature, String[] exceptions) {
        logger.fine("    +(m) " + name + " " + descriptor + " " + signature + " " + Arrays.toString(exceptions));
        // the method is not a static initializer
        if (!name.equals("<clinit>")) {
        	String[] dottedExceptions = Utils.toDottedClassNames(exceptions);
        	//MethodData method = new MethodData(loader, clazz, access, name, descriptor, dottedExceptions);
			try {
				MethodData method = methodConstructor.newInstance(loader,
						clazz, access, name, descriptor, dottedExceptions);
				if (signature != null) {
					new SignatureReader(signature).accept(new TypeParameterDumper(method, typeParameterConstructor));
				}
				clazz.add(method);
				return new MethodDumper(method);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return null;
    }

    public void visitOuterClass(String owner, String name, String desc) {
        logger.fine("    *(oc) " + name + " " + desc);
    }

    public void visitSource(String source, String debug) {
        logger.fine(" - source: " + source);
        logger.fine(" - debug: " + debug);
        clazz.setSource(source);
    }

    public ClassData getClazz() {
        return returnClass;
    }

}
