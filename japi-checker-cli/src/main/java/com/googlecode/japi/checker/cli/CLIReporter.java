package com.googlecode.japi.checker.cli;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.japi.checker.Difference;
import com.googlecode.japi.checker.DifferenceType;
import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Severity;
import com.googlecode.japi.checker.model.JavaItem;

public class CLIReporter implements Reporter {
    private List<Difference> differences;
    private boolean reportSourceIncompatibilities;
    
    public CLIReporter(boolean reportSourceIncompatibilities) {
    	this.differences = new ArrayList<Difference>();
    	this.reportSourceIncompatibilities = reportSourceIncompatibilities;
    }

    public void report(Difference difference) {
        if ((difference.getSeverity() == Severity.ERROR || difference
                .getSeverity() == Severity.WARNING)) {
            if (reportSourceIncompatibilities
                    || !difference.isSourceIncompatible()) {
                System.out.println(difference.getSeverity()
                        + ": "
                        + difference.getSource()
                        + (difference.getLine() != null ? "("
                                + difference.getLine() + ")" : "") + ": "
                        + difference.getMessage());
            }
        }
        differences.add(difference);
    }
    
	@Override
	public void report(JavaItem referenceItem, JavaItem newItem,
			DifferenceType differenceType, Object... args) {
		Difference difference = new Difference(referenceItem, newItem, differenceType, args);
		report(difference);
	}
    
    public List<Difference> getDifferences() {
        return differences;
    }

    public int getCount(Severity severity) {
        int count = 0;
        for (Difference difference : differences) {
            if (difference.getSeverity() == severity) {
            	if (reportSourceIncompatibilities || !difference.isSourceIncompatible()) {
            		count++;
            	}
            }
        }
        return count;
    }
}
