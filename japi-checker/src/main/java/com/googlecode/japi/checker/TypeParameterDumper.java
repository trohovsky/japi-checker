package com.googlecode.japi.checker;

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
	
	public TypeParameterDumper(Parametrized item) {
		super(Opcodes.ASM4);
		this.item = item;
	}
	
	@Override
	public void visitFormalTypeParameter(String name) {
		//System.out.println("  visitFormalTypeParameter(" + name + ")");
		typeParameter = new TypeParameterData(name);
		item.add(typeParameter);
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
			typeParameter.addBound(name);
			bounded = false;
		}
	}
	
	@Override
	public void visitEnd() {
		//System.out.println("  visitEnd()");
	}

}
