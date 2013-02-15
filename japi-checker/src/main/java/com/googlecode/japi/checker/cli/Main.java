package com.googlecode.japi.checker.cli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.japi.checker.BCChecker;
import com.googlecode.japi.checker.Difference;
import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Severity;
import com.googlecode.japi.checker.model.MethodData;

/**
 * 
 * @author Tomas Rohovsky
 *
 */
public class Main {
	    
	// TODO source and binary compatibility parameters
	// output format parameters
	public static void main(String[] args) {
		
		String oldVersionPath = args[0];
		String newVersionPath = args[1];
		
		File oldVersion = new File(oldVersionPath);
	    File newVersion = new File(newVersionPath);
	    
		BCChecker checker = new BCChecker(oldVersion, newVersion);
		BasicReporter reporter = new BasicReporter();
        try {
			checker.checkBackwardCompatibility(reporter);
			System.out.println("Error count: " + reporter.getCount(Severity.ERROR));
			System.out.println("Warning count: " + reporter.getCount(Severity.WARNING));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static class BasicReporter implements Reporter {
        List<Difference> differences = new ArrayList<Difference>();
        
		@Override
		public void report(Report report) {
			// TODO Auto-generated method stub
			
		}
        
        @Override
        public void report(Difference difference) {
        	if (difference.getDifferenceType().getServerity() == Severity.ERROR || 
        			difference.getDifferenceType().getServerity() == Severity.WARNING) {
        		System.out.println(difference.getDifferenceType().getServerity() + ": " + difference.getSource() + getLine(difference) + ": " + difference.getMessage());
        		differences.add(difference);
        	}
        }
        
        private static String getLine(Difference difference) {
            if (difference.getNewItem() instanceof MethodData) {
                return "(" + ((MethodData)difference.getNewItem()).getLineNumber() + ")";
            }
            return "";
        }
        
        public List<Difference> getDifferences() {
            return differences;
        }

        public int getCount(Severity severity) {
            int count = 0;
            for (Difference difference : differences) {
                if (difference.getDifferenceType().getServerity() == severity) {
                    count++;
                }
            }
            return count;
        }
    }

}