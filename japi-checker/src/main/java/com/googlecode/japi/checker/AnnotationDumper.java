package com.googlecode.japi.checker;

import com.googlecode.japi.checker.model.MethodData;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

/**
 * AnnotationDumper inspects annotation of the method.
 *
 * @author Tomas Rohovsky
 */
public class AnnotationDumper extends AnnotationVisitor {

	private final MethodData method;

	public AnnotationDumper(MethodData method) {
		super(Opcodes.ASM6);
		this.method = method;
	}

	@Override
	public void visit(String name, Object value) {
		method.setDefaultValue(value.toString());
	}

}
