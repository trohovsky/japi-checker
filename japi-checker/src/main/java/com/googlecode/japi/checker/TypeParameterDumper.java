package com.googlecode.japi.checker;

import com.googlecode.japi.checker.model.Parametrized;
import com.googlecode.japi.checker.model.TypeParameterData;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Tomas Rohovsky
 */
public class TypeParameterDumper extends SignatureVisitor {

	private final Parametrized item;
	private TypeParameterData typeParameter;
	private boolean bounded;
	private Constructor<? extends TypeParameterData> typeParameterConstructor;

	public TypeParameterDumper(Parametrized item, Constructor<? extends TypeParameterData> typeParameterConstructor) {
		super(Opcodes.ASM4);
		this.item = item;
		this.typeParameterConstructor = typeParameterConstructor;
	}

	@Override
	public void visitFormalTypeParameter(String name) {
		//System.out.println("  visitFormalTypeParameter(" + name + ")");
		//typeParameter = new TypeParameterData(name);
		try {
			typeParameter = typeParameterConstructor.newInstance(name);
			item.add(typeParameter);
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
