package com.googlecode.japi.checker;

import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.Scope;


public class DefaultClassDataLoaderFactory implements ClassDataLoaderFactory<ClassData> {

	@Override
	public ClassDataLoader<ClassData> createClassDataLoader(Scope maxVisibility) {
		return new DefaultClassDataLoader(maxVisibility);
	}

}
