package com.googlecode.japi.checker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import com.googlecode.japi.checker.model.Parametrized;
import com.googlecode.japi.checker.model.TypeParameterData;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
public class TypeParameterDumper extends SignatureVisitor {

	private final Parametrized item;
	private TypeParameterData typeParameter;
	private boolean bounded;
	private Class<TypeParameterData> typeParameterClass;
	
	public TypeParameterDumper(Parametrized item) {
		this(item, TypeParameterData.class);
	}
	
	public TypeParameterDumper(Parametrized item, Class<TypeParameterData> typeParameterClass) {
		super(Opcodes.ASM4);
		this.item = item;
		this.typeParameterClass = typeParameterClass;
	}
	
	@Override
	public void visitFormalTypeParameter(String name) {
		//System.out.println("  visitFormalTypeParameter(" + name + ")");
		//typeParameter = new TypeParameterData(name);
		Constructor<TypeParameterData> constructor;
		try {
			constructor = typeParameterClass.getConstructor(String.class);
			typeParameter = constructor.newInstance(name);
			item.add(typeParameter);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	@Override
	public SignatureVisitor visitClassBound() {
		//System.out.println("  visitClassBound()");
		bounded = true;
		return this;
	}
	
	@Override
	public SignatureVisitor visitInterfaceBound() {
		//System.out.println("  visitInterfaceBound()");
		bounded = true;
		return this;
	}
	
	@Override
	public void visitClassType(String name) {
		//System.out.println("    visitClassType(" + name + ")");
		if (bounded) {
			String dottedName = Utils.toDottedClassName(name);
			typeParameter.addBound(dottedName);
			bounded = false;
		}
	}
	
	@Override
	public void visitEnd() {
		//System.out.println("  visitEnd()");
	}

}
