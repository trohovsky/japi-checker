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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.JavaItem;
import com.googlecode.japi.checker.model.MethodData;
import com.googlecode.japi.checker.rules.ChangeKindOfAPIType;
import com.googlecode.japi.checker.rules.CheckChangeOfScope;
import com.googlecode.japi.checker.rules.CheckFieldChangeOfType;
import com.googlecode.japi.checker.rules.CheckFieldChangeToStatic;
import com.googlecode.japi.checker.rules.CheckFieldChangeToTransient;
import com.googlecode.japi.checker.rules.CheckInheritanceChanges;
import com.googlecode.japi.checker.rules.CheckMethodChangedToFinal;
import com.googlecode.japi.checker.rules.CheckMethodChangedToStatic;
import com.googlecode.japi.checker.rules.CheckMethodExceptions;
import com.googlecode.japi.checker.rules.CheckRemovedMethod;
import com.googlecode.japi.checker.rules.CheckSerialVersionUIDField;
import com.googlecode.japi.checker.rules.ClassChangedToAbstract;
import com.googlecode.japi.checker.rules.ClassChangedToFinal;
import com.googlecode.japi.checker.utils.AntPatternMatcher;

public class TestBCChecker {
    
    private File reference;
    private File newVersion;
    private Handler handler = new Handler() {

        @Override
        public void close() throws SecurityException {
        }

        @Override
        public void flush() {
        }

        @Override
        public void publish(LogRecord record) {
            System.out.println(record.getMessage());
        }
        
    };
    
    @Before
    public void setUp() {
        Logger.getLogger(ClassDumper.class.getName()).setLevel(java.util.logging.Level.ALL);
        Logger.getLogger(ClassDumper.class.getName()).addHandler(handler);
        System.out.println("==================================");
        for (String file : System.getProperty("java.class.path").split(File.pathSeparator)) {
            if (file.contains("reference-test-jar")) {
                reference = new File(file);
                System.out.println(reference);
            } else if (file.contains("new-test-jar")) {
                newVersion = new File(file);
                System.out.println(newVersion);
            }
        }
        assertNotNull("The reference library is not found.", reference);
        assertNotNull("The newVersion library is not found.", newVersion);
    }
    
    @After
    public void tearDown() {
        Logger.getLogger(ClassDumper.class.getName()).removeHandler(handler);
    }

    @Test
    public void testBCCheckerInclude() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(null, "**/Nothing*.class");
        assertEquals(0, reporter.getDifferences().size());
    }

    
    @Test
    public void testCheckerClassRemoved() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(null, "**/RemovedClass.class");
        assertEquals(1, reporter.count(Severity.ERROR));
        reporter.assertContains(Severity.ERROR, "The class com.googlecode.japi.checker.tests.RemovedClass has been removed");
    }

    @Test
    public void testClassToInterface() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(ChangeKindOfAPIType.class, "**/ClassToInterface.class");
        assertEquals(3, reporter.count(Severity.ERROR));
        reporter.assertContains(Severity.ERROR, "The class com.googlecode.japi.checker.tests.ClassToInterface has been changed into interface");
        reporter.assertContains(Severity.ERROR, "The constructor <init>() has been removed");
        reporter.assertContains(Severity.ERROR, "The class com.googlecode.japi.checker.tests.ClassToInterface has been made abstract");
    }

    @Test
    public void testClassToAbstract() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(ClassChangedToAbstract.class, "**/ClassToAbstract.class");
        reporter.assertContains(Severity.ERROR, "The class com.googlecode.japi.checker.tests.ClassToAbstract has been made abstract");
    }

    @Test
    public void testCheckChangeOfScope() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckChangeOfScope.class, "**/PublicClassToProtected.class");
        reporter.assertContains(Severity.ERROR, "The visibility of the constructor <init>() has been changed from public to (package)");
        reporter.assertContains(Severity.ERROR, "The visibility of the class com.googlecode.japi.checker.tests.PublicClassToProtected has been changed from public to (package)");
        assertEquals(2, reporter.count(Severity.ERROR));
    }

    @Test
    public void testCheckChangeOfScopeForFieldPublicScope() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckChangeOfScope.class, "**/PublicScopeFieldTestCases.class");
        reporter.assertContains(Severity.ERROR, "The visibility of the field testChangeOfScopeFromPublicToProtected has been changed from public to protected");
        reporter.assertContains(Severity.ERROR, "The visibility of the field testChangeOfScopeFromPublicToPrivate has been changed from public to private");
        reporter.assertContains(Severity.ERROR, "The visibility of the field testChangeOfScopeFromProtectedToPrivate has been changed from protected to private");
        reporter.assertContains(Severity.INFO, "The visibility of the field testChangeOfScopeFromProtectedToPublic has been changed from protected to public");
        reporter.assertContains(Severity.INFO, "The visibility of the field testChangeOfScopeFromPrivateToPublic has been changed from private to public");
        reporter.assertContains(Severity.INFO, "The visibility of the field testChangeOfScopeFromPrivateToProtected has been changed from private to protected");
        assertEquals(14, reporter.count(Severity.ERROR)); // 3-14
        assertEquals(3, reporter.count(Severity.INFO));
    }
    
    @Test
    public void testCheckChangeOfScopeForFieldPackageScope() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckChangeOfScope.class, "**/PackageScopeFieldTestCases.class");
        assertEquals(0, reporter.count(Severity.ERROR));
        assertEquals(0, reporter.count(Severity.WARNING));
    }

    @Test
    public void testCheckFieldChangeOfTypePublicClass() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckFieldChangeOfType.class, "**/PublicScopeFieldTestCases.class");
        reporter.assertContains(Severity.ERROR, "The type of the field testChangeOfTypePublic has been modified from java.lang.String to java.lang.Boolean");
        reporter.assertContains(Severity.ERROR, "The type of the field testChangeOfTypeProtected has been modified from java.lang.String to java.lang.Boolean");
        assertEquals(14, reporter.count(Severity.ERROR)); // 2->14
    }

    @Test
    public void testCheckFieldChangeOfTypePackageClass() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckFieldChangeOfType.class, "**/PackageScopeFieldTestCases.class");
        assertEquals(0, reporter.count(Severity.ERROR));
    }

    @Test
    public void testCheckFieldChangeToStaticPublicScope() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckFieldChangeToStatic.class, "**/PublicScopeFieldTestCases.class");
        reporter.assertContains(Severity.ERROR, "The field testPublicChangeToStatic has been made static");
        reporter.assertContains(Severity.ERROR, "The field testProtectedChangeToStatic has been made static");
        reporter.assertContains(Severity.ERROR, "The field testPublicChangeFromStatic has been made non-static");
        reporter.assertContains(Severity.ERROR, "The field testProtectedChangeFromStatic has been made non-static");
        assertEquals(14, reporter.count(Severity.ERROR)); // 4->14
    }

    @Test
    public void testCheckFieldChangeToTransientPublicScope() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckFieldChangeToTransient.class, "**/PublicScopeFieldTestCases.class");
        reporter.assertContains(Severity.WARNING, "The field publicNotTransientToTransient has been made transient");
        reporter.assertContains(Severity.WARNING, "The field protectedNotTransientToTransient has been made transient");
        reporter.assertContains(Severity.WARNING, "The field privateNotTransientToTransient has been made transient");
        reporter.assertContains(Severity.ERROR, "The field publicTransientToNoTransient has been made non-transient");
        reporter.assertContains(Severity.ERROR, "The field protectedTransientToNoTransient has been made non-transient");
        reporter.assertContains(Severity.ERROR, "The field privateTransientToNoTransient has been made non-transient");
        assertEquals(3, reporter.count(Severity.WARNING));
        assertEquals(14, reporter.count(Severity.ERROR)); // 3->14
    }

    
    @Test
    public void testCheckFieldChangeToStaticPackageScope() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckFieldChangeToStatic.class, "**/PackageScopeFieldTestCases.class");
        assertEquals(0, reporter.count(Severity.ERROR));
    }

    @Test
    public void testClassChangedToFinal() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(ClassChangedToFinal.class, "**/PublicClassToFinal.class");
        reporter.assertContains(Severity.ERROR, "The class com.googlecode.japi.checker.tests.PublicClassToFinal has been made final");
        assertEquals(1, reporter.count(Severity.ERROR));
    }
 
    @Test
    public void testCheckInheritanceChanges() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckInheritanceChanges.class, "**/CheckInheritanceChanges.class");
        reporter.assertContains(Severity.ERROR, "The superclass set of the class com.googlecode.japi.checker.tests.CheckInheritanceChanges has been contracted for java.util.Vector");
        reporter.assertContains(Severity.ERROR, "The superinterface set of the class com.googlecode.japi.checker.tests.CheckInheritanceChanges has been contracted for java.io.Serializable");
        assertEquals(2, reporter.count(Severity.ERROR));
    }
    
    @Test
    public void testCheckRemovedMethod() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckRemovedMethod.class, "**/CheckRemovedMethod.class");
        reporter.assertContains(Severity.ERROR, "The method publicMethodRemoved() has been removed");
        reporter.assertContains(Severity.ERROR, "The method protectedMethodRemoved() has been removed");
        assertEquals(4, reporter.count(Severity.ERROR)); // 2->4
    }
    
    @Test
    public void testCheckMethodException() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckMethodExceptions.class, "**/CheckMethodException.class");
        reporter.assertContains(Severity.ERROR, "publicAddedException() is now throwing java.lang.Exception");
        reporter.assertContains(Severity.ERROR, "protectedAddedException() is now throwing java.lang.Exception");
        reporter.assertContains(Severity.ERROR, "publicRemovedException() is not throwing java.lang.Exception anymore");
        reporter.assertContains(Severity.ERROR, "protectedRemovedException() is not throwing java.lang.Exception anymore");
        assertEquals(4, reporter.count(Severity.ERROR));
    }

    @Test
    public void testCheckMethodExceptionInheritance() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckMethodExceptions.class, "**/CheckMethodExceptionInheritance.class");
        assertEquals(0, reporter.count(Severity.ERROR));
    }

    
    @Test
    public void testCheckerInnerClassRemoved() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(null, "**/InnerClassRemoved*.class");
        assertEquals(4, reporter.count(Severity.ERROR));
        //reporter.assertContains(Severity.ERROR, "Public class com/googlecode/japi/checker/tests/RemovedClass has been removed.");
    }

    @Test
    public void testCheckMethodChangedToFinal() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckMethodChangedToFinal.class, "**/CheckMethodAccess.class");
        reporter.assertContains(Severity.ERROR, "The method publicToFinal() has been made final");
        reporter.assertContains(Severity.ERROR, "The method protectedToFinal() has been made final");
        assertEquals(6, reporter.count(Severity.ERROR));// 2->6
    }

    @Test
    public void testCheckMethodChangedToStatic() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckMethodChangedToStatic.class, "**/CheckMethodAccess.class");
        reporter.assertContains(Severity.ERROR, "The method publicToStatic() has been made static");
        reporter.assertContains(Severity.ERROR, "The method protectedToStatic() has been made static");
        reporter.assertContains(Severity.ERROR, "The method publicFromStatic() has been made non-static");
        reporter.assertContains(Severity.ERROR, "The method protectedFromStatic() has been made non-static");
        assertEquals(6, reporter.count(Severity.ERROR));// 4->6
    }

    @Test
    public void testCheckInterfaceChangedToClass() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(ChangeKindOfAPIType.class, "**/InterfaceToClass.class");
        assertEquals(1, reporter.count(Severity.ERROR));
        reporter.assertContains(Severity.ERROR, "The interface com.googlecode.japi.checker.tests.InterfaceToClass has been changed into class");
    }

    //@Test
    //public void testCheckClassBaseClassChangedBaseClassRemoved() throws InstantiationException, IllegalAccessException, IOException {
    //    BasicReporter reporter = check(CheckSuperClass.class, "**/inheritance/removebaseclass/A.class");
    //    assertEquals(1, reporter.count(Severity.ERROR));
    //    reporter.assertContains(Severity.ERROR, "The class com/googlecode/japi/checker/tests/inheritance/removebaseclass/A does not inherit from com/googlecode/japi/checker/tests/inheritance/removebaseclass/B anymore.");
    //}

    //@Test
    //public void testCheckClassBaseClassChangedBaseClassChangedWithoutBreakingTheInheritance() throws InstantiationException, IllegalAccessException, IOException {
    //    BasicReporter reporter = check(CheckSuperClass.class, "**/inheritance/changetree/A.class");
    //    assertEquals(0, reporter.count(Severity.ERROR));
    //    reporter.assertContains(Severity.ERROR, "The class com/googlecode/japi/checker/tests/inheritance/removebaseclass/A does not inherit from com/googlecode/japi/checker/tests/inheritance/removebaseclass/B anymore.");
    //}

    //@Test
    //public void testCheckClassBaseClassChangedBaseClassWithSameClass() throws InstantiationException, IllegalAccessException, IOException {
    //    BasicReporter reporter = check(CheckSuperClass.class, "**/inheritance/changetree/B.class");
    //    assertEquals(0, reporter.count(Severity.ERROR));
    //    reporter.assertContains(Severity.ERROR, "The class com/googlecode/japi/checker/tests/inheritance/removebaseclass/A does not inherit from com/googlecode/japi/checker/tests/inheritance/removebaseclass/B anymore.");
    //}

    
    @Test
    public void testCheckSerialVersionUIDFieldSameUID() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckSerialVersionUIDField.class, "**/SameUID.class");
        assertEquals(0, reporter.count(Severity.ERROR));
    }

    @Test
    public void testCheckSerialVersionUIDFieldDifferentUID() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckSerialVersionUIDField.class, "**/DifferentUID.class");
        assertEquals(1, reporter.count(Severity.ERROR));
        reporter.assertContains(Severity.ERROR, "The value of the serialVersionUID field has changed from 0xa64662a9226655f7 to 0xa64662a9226655ad");
    }

    @Test
    public void testCheckSerialVersionUIDFieldInvalidTypeUID() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckSerialVersionUIDField.class, "**/InvalidTypeUID.class");
        assertEquals(1, reporter.count(Severity.ERROR));
        reporter.assertContains(Severity.ERROR, "The type for field serialVersionUID is invalid, it must be a long");
    }
    
    @Test
    public void testCheckSerialVersionUIDFieldInvalidTypeInNewUID() throws InstantiationException, IllegalAccessException, IOException {
        BasicReporter reporter = check(CheckSerialVersionUIDField.class, "**/InvalidTypeInNewUID.class");
        assertEquals(1, reporter.count(Severity.ERROR));
        reporter.assertContains(Severity.ERROR, "The type for field serialVersionUID is invalid, it must be a long");
    }

    // TODO The parameter clazz is currently unused. It caused problem in in assertEquals, because every rule is done above input class.
    public BasicReporter check(Class<? extends Rule> clazz, String ... includes) throws InstantiationException, IllegalAccessException, IOException {
    	List<AntPatternMatcher> includesList = new ArrayList<AntPatternMatcher>();
        List<AntPatternMatcher> excludesList = new ArrayList<AntPatternMatcher>();
    	
    	for (String include : includes) {
    		includesList.add(new AntPatternMatcher(include));
        }   	
    	
    	// reading of classes
    	ClassDataLoaderFactory<ClassData> classDataLoaderFactory = new DefaultClassDataLoaderFactory();
    	
    	ClassDataLoader<ClassData> referenceDataLoader = classDataLoaderFactory.createClassDataLoader();
    	try {
			referenceDataLoader.read(reference.toURI());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        List<ClassData> referenceClasses = referenceDataLoader.getClasses(reference.toURI(), includesList, excludesList);
        
        ClassDataLoader<ClassData> newArtifactDataLoader = classDataLoaderFactory.createClassDataLoader();
        try {
			newArtifactDataLoader.read(newVersion.toURI());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        List<ClassData> newClasses = newArtifactDataLoader.getClasses(newVersion.toURI(), includesList, excludesList);
    	
    	BCChecker checker = new BCChecker();
        BasicReporter reporter = new BasicReporter();

        checker.checkBackwardCompatibility(reporter, referenceClasses, newClasses);
        return reporter;
    }
    
    public static class BasicReporter implements Reporter {
        List<Difference> differences = new ArrayList<Difference>();
        
		@Override
		public void report(Difference difference) {
			System.out.println(difference.getDifferenceType().getServerity() + ": " + difference.getSource() + getLine(difference) + ": " + difference.getMessage());
            differences.add(difference);
		}
		
		@Override
		public void report(JavaItem referenceItem, JavaItem newItem,
				DifferenceType differenceType, Object... args) {
			Difference difference = new Difference(referenceItem, newItem, differenceType, args);
			report(difference);
		}
        
        private String getLine(Difference difference) {
            if (difference.getNewItem() instanceof MethodData) {
            	Integer lineNumber = ((MethodData)difference.getNewItem()).getLineNumber();
            	if (lineNumber != null) {
            		return "(" + lineNumber + ")";
            	} else {
            		return "";
            	}
            }
            return "";
        }
        
        public List<Difference> getDifferences() {
            return differences;
        }

        public int count(Severity severity) {
            int count = 0;
            for (Difference difference : differences) {
                if (difference.getDifferenceType().getServerity() == severity) {
                    count++;
                }
            }
            return count;
        }
        
        public void assertContains(Severity severity, String str) {
            for (Difference difference : differences) {
                if (difference.getDifferenceType().getServerity() == severity && difference.getMessage().contains(str)) {
                    return;
                }
            }
            fail("Could not find message containing: " + str);
        }

    }

}
