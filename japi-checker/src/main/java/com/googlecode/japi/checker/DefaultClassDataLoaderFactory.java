package com.googlecode.japi.checker;

import com.googlecode.japi.checker.model.ClassData;


class DefaultClassDataLoaderFactory implements ClassDataLoaderFactory<ClassData> {

    @Override
    public ClassDataLoader<ClassData> createClassDataLoader() {
        return new DefaultClassDataLoader();
    }

}
