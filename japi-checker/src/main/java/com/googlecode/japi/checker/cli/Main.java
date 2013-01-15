package com.googlecode.japi.checker.cli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.japi.checker.BCChecker;
import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.model.MethodData;

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