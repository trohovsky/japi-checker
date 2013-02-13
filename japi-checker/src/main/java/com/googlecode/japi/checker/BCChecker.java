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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import com.googlecode.japi.checker.Reporter.Level;
import com.googlecode.japi.checker.Reporter.Report;
import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.FieldData;
import com.googlecode.japi.checker.model.MethodData;
import com.googlecode.japi.checker.rules.ClassRules;
import com.googlecode.japi.checker.rules.FieldRules;
import com.googlecode.japi.checker.rules.MethodRules;
import com.googlecode.japi.checker.utils.AntPatternMatcher;

public class BCChecker {
    private File reference;
    private File newArtifact;
    private List<File> referenceClasspath = new ArrayList<File>();
    private List<File> newArtifactClasspath = new ArrayList<File>();
    private List<AntPatternMatcher> includes = new ArrayList<AntPatternMatcher>();
    private List<AntPatternMatcher> excludes = new ArrayList<AntPatternMatcher>();
    private ClassDataLoaderFactory classDataLoaderFactory = new DefaultClassDataLoaderFactory();
    
    public BCChecker(File reference, File newArtifact) {
        if (reference == null) {
            throw new IllegalArgumentException("The reference parameter cannot be null.");
        }
        if (newArtifact == null) {
            throw new IllegalArgumentException("The newArtifact parameter cannot be null.");
        }
        if (!reference.isDirectory() && !isArchive(reference)) {
            throw new IllegalArgumentException("reference must be either a directory" +
                    " or a jar (or a zip kind of archive) file");
        }
        if (!newArtifact.isDirectory() && !isArchive(newArtifact)) {
            throw new IllegalArgumentException("new artifact must be either a directory" + 
                    " or a jar (or a zip kind of archive) file");
        }
        this.reference = reference;
        this.newArtifact = newArtifact;
    }
 
    public void addToReferenceClasspath(File path) {
        this.referenceClasspath.add(path);
    }

    public void addToNewArtifactClasspath(File path) {
        this.newArtifactClasspath.add(path);
    }
    
    public void addInclude(String include) {
        includes.add(new AntPatternMatcher(include));
    }

    public void addExclude(String exclude) {
        excludes.add(new AntPatternMatcher(exclude));
    }
    
    public void checkBackwardCompatibility(Reporter reporter) throws IOException {
    	ClassDataLoader referenceDataLoader = classDataLoaderFactory.createClassDataLoader();
        referenceDataLoader.read(reference.toURI());
        for (File file : this.referenceClasspath) {
            referenceDataLoader.read(file.toURI());
        }
        List<ClassData> referenceData = referenceDataLoader.getClasses(reference.toURI(), includes, excludes);
        ClassDataLoader newArtifactDataLoader = classDataLoaderFactory.createClassDataLoader();
        newArtifactDataLoader.read(newArtifact.toURI());
        for (File file : this.newArtifactClasspath) {
            newArtifactDataLoader.read(file.toURI());
        }
        List<ClassData> newData = newArtifactDataLoader.getClasses(newArtifact.toURI(), includes, excludes);
        
        ClassRules rules = new ClassRules();
    	FieldRules fieldRules = new FieldRules();
        MethodRules methodRules = new MethodRules();
        for (ClassData referenceClass : referenceData) {
            boolean found = false;
            for (ClassData newClass : newData) {
                if (referenceClass.isSame(newClass)) {
                	// checking class rules
                    rules.checkBackwardCompatibility(reporter, referenceClass, newClass);
                    
                    // checking field rules
                    for (FieldData referenceField : referenceClass.getFields()) {
                        for (FieldData newField: newClass.getFields()) {
                            if (referenceField.isSame(newField)) {
                            	fieldRules.checkBackwardCompatibility(reporter, referenceField, newField);
                            }
                        }
                    }
                    
                    // checking method rules
                    for (MethodData referenceMethod : referenceClass.getMethods()) {
                        for (MethodData newMethod: newClass.getMethods()) {
                            if (referenceMethod.isSame(newMethod)) {
                            	methodRules.checkBackwardCompatibility(reporter, referenceMethod, newMethod);
                            }
                        }
                    }
                    
                    found = true;
                    break;
                }
            }
            if (!found && referenceClass.getVisibility() == Scope.PUBLIC) {
                reporter.report(new Report(Level.ERROR, "Public " + referenceClass + " has been removed.", referenceClass, null));
            }
        }
    }
    
    private boolean isArchive(File file) {
        ZipFile zf = null;
        try {
            zf = new ZipFile(file);
            zf.entries(); // forcing to do something with the file.
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException e) {
                    // swallow the exception...
                }
            }
        }
    }

}
