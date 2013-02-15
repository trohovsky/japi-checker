package com.googlecode.japi.checker.cli;

import java.io.File;
import java.io.IOException;

import com.googlecode.japi.checker.BCChecker;
import com.googlecode.japi.checker.Severity;


/**
 * 
 * @author Tomas Rohovsky
 *
 */
public class Main {
		    
	public static void main(String[] args) {
		
		if (args.length < 2) {
			System.out.println("Usage: japi-checker-cli REFERENCE_LIBRARY NEW_LIBRARY [-bin]");
			System.out.println("Check API and ABI compatiblity of Java libraries.");
			System.out.println();
			System.out.println("  REFERENCE_LIBRARY - reference library, JAR archive or directory containing classes");
			System.out.println("  NEW_LIBRARY - new library, JAR archive or directory containing classes");
			System.out.println("  -bin    check only binary compatibility (default - source and binary compatibility)");
			return;
		}
		
		String oldVersionPath = args[0];
		String newVersionPath = args[1];
		
		boolean reportSourceAffectingDifferences = true;		
		if (args.length > 2) {
			reportSourceAffectingDifferences = (args[2] == null ? true : (args[2].equals("-bin") ? false : true));
		}
		
		File oldVersion = new File(oldVersionPath);
	    File newVersion = new File(newVersionPath);
	    
		BCChecker checker = new BCChecker(oldVersion, newVersion);
		CLIReporter reporter = new CLIReporter(reportSourceAffectingDifferences);
		
        try {
			checker.checkBackwardCompatibility(reporter);
			System.out.println("Error count: " + reporter.getCount(Severity.ERROR));
			System.out.println("Warning count: " + reporter.getCount(Severity.WARNING));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}