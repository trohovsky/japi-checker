package com.googlecode.japi.checker.cli;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.japi.checker.Difference;
import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Severity;
import com.googlecode.japi.checker.model.MethodData;

public class CLIReporter implements Reporter {
	private List<Difference> differences;
    private boolean reportSourceAffectingDifferences;
    
    public CLIReporter(boolean source) {
    	this.differences = new ArrayList<Difference>();
    	this.reportSourceAffectingDifferences = source;
    }
            
	public void report(Report report) {
		// TODO Auto-generated method stub
		
	}
    
    public void report(Difference difference) {
    	if ((difference.getDifferenceType().getServerity() == Severity.ERROR || 
    			difference.getDifferenceType().getServerity() == Severity.WARNING)) {
    		if (reportSourceAffectingDifferences || !difference.getDifferenceType().isSource()) {
    			System.out.println(difference.getDifferenceType().getServerity() + ": " + difference.getSource() + getLine(difference) + ": " + difference.getMessage());
    		}
    	}
    	differences.add(difference);
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

    public int getCount(Severity severity) {
        int count = 0;
        for (Difference difference : differences) {
            if (difference.getDifferenceType().getServerity() == severity) {
            	if (reportSourceAffectingDifferences || !difference.getDifferenceType().isSource()) {
            		count++;
            	}
            }
        }
        return count;
    }
}
