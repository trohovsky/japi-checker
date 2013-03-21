/*
 * Copyright 2012 William Bernardet
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;

import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.FieldData;
import com.googlecode.japi.checker.model.MethodData;
import com.googlecode.japi.checker.model.TypeParameterData;


public class DirectoryReader<C extends ClassData> extends AbstractClassReader<C> {

    private File path;
    private ClassDumper<C> dumper;
    
    public DirectoryReader(File path, ClassDataLoader<C> loader) {
        this.path = path;
        this.dumper = new ClassDumper<C>(loader);
    }
    
    public DirectoryReader(File path, ClassDataLoader<C> loader,
    		Class<C> classClass, 
    		Class<FieldData> fieldClass, 
    		Class<MethodData> methodClass, 
    		Class<TypeParameterData> typeParameterClass) {
    	this.path = path;
    	this.dumper = new ClassDumper<C>(loader, classClass, fieldClass, methodClass, typeParameterClass);
    }

    @Override
    public void read() throws IOException {
        clear();
        scanDir(this.path, null);
    }

    private void scanDir(File dir, String path) throws IOException {
        byte buffer[] = new byte[2048]; 
        if (path == null) {
            path = "";
        }
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scanDir(file, path + file.getName() + "/");
            } else if (file.getName().endsWith(".class")) {
                ByteArrayOutputStream os =  new ByteArrayOutputStream();
                InputStream is = null;
                try {
                    is = new FileInputStream(file);
                    int count = 0;
                    while ((count = is.read(buffer)) != -1) {
                        os.write(buffer, 0, count);
                    }
                    ClassReader cr = new ClassReader(os.toByteArray());
                    cr.accept(dumper, 0);
                    this.put(path + file.getName(), dumper.getClazz());
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            }
        }
    }

}
