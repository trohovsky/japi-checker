package com.googlecode.japi.checker.cli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.japi.checker.BCChecker;
import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Rule;
import com.googlecode.japi.checker.model.MethodData;
import com.googlecode.japi.checker.rules.CheckChangeOfScope;
import com.googlecode.japi.checker.rules.CheckClassVersion;
import com.googlecode.japi.checker.rules.CheckFieldChangeOfType;
import com.googlecode.japi.checker.rules.CheckFieldChangeToFinal;
import com.googlecode.japi.checker.rules.CheckFieldChangeToStatic;
import com.googlecode.japi.checker.rules.CheckFieldChangeToTransient;
import com.googlecode.japi.checker.rules.CheckInheritanceChanges;
import com.googlecode.japi.checker.rules.CheckMethodChangedToAbstract;
import com.googlecode.japi.checker.rules.CheckMethodChangedToFinal;
import com.googlecode.japi.checker.rules.CheckMethodChangedToStatic;
import com.googlecode.japi.checker.rules.CheckMethodExceptions;
import com.googlecode.japi.checker.rules.CheckRemovedField;
import com.googlecode.japi.checker.rules.CheckRemovedMethod;
import com.googlecode.japi.checker.rules.CheckSerialVersionUIDField;
import com.googlecode.japi.checker.rules.CheckSuperClass;
import com.googlecode.japi.checker.rules.ClassChangedToAbstract;
import com.googlecode.japi.checker.rules.ClassChangedToFinal;
import com.googlecode.japi.checker.rules.ClassChangedToInterface;
import com.googlecode.japi.checker.rules.InterfaceChangedToClass;

public class Main {
	    
	public static void main(String[] args) {
		
		File reference = new File("/home/tomas/APICompatibility/test-jars/testProject1.jar");
	    File newVersion = new File("/home/tomas/APICompatibility/test-jars/testProject2.jar");
	    
		BCChecker checker = new BCChecker(reference, newVersion);
		BasicReporter reporter = new BasicReporter();
        List<Rule> rules = new ArrayList<Rule>();

        rules.add(new CheckChangeOfScope());
        rules.add(new CheckClassVersion());
        rules.add(new CheckFieldChangeOfType());
        rules.add(new CheckFieldChangeToFinal()); // new
        rules.add(new CheckFieldChangeToStatic());
        rules.add(new CheckFieldChangeToTransient()); // was not added
        rules.add(new CheckInheritanceChanges());
        rules.add(new CheckMethodChangedToAbstract()); // new
        rules.add(new CheckMethodChangedToFinal()); // was not added
        rules.add(new CheckMethodChangedToStatic()); // was not added
        rules.add(new CheckMethodExceptions());
        rules.add(new CheckRemovedField());
        rules.add(new CheckRemovedMethod());
        rules.add(new CheckSerialVersionUIDField());
        rules.add(new CheckSuperClass());
        rules.add(new ClassChangedToAbstract());
        rules.add(new ClassChangedToFinal());
        rules.add(new ClassChangedToInterface());
        rules.add(new InterfaceChangedToClass());
		
		try {
			checker.checkBacwardCompatibility(reporter, rules);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static class BasicReporter implements Reporter {
        List<Report> messages = new ArrayList<Report>();
        
        @Override
        public void report(Report report) {
        	if (report.level == Level.ERROR || report.level == Level.WARNING) {
        		System.out.println(report.level.toString() + ": " + report.source + getLine(report) + ": " + report.message);
        		messages.add(report);
        	}
        }
        
        private static String getLine(Report report) {
            if (report.newItem instanceof MethodData) {
                return "(" + ((MethodData)report.newItem).getLineNumber() + ")";
            }
            return "";
        }
        
        public List<Report> getMessages() {
            return messages;
        }

        public int count(Level level) {
            int count = 0;
            for (Report message : messages) {
                if (message.level == level) {
                    count++;
                }
            }
            return count;
        }
    }

}