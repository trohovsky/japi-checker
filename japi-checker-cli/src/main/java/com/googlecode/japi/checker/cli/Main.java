package com.googlecode.japi.checker.cli;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.googlecode.japi.checker.BCChecker;
import com.googlecode.japi.checker.ClassDataLoader;
import com.googlecode.japi.checker.ClassDataLoaderFactory;
import com.googlecode.japi.checker.DefaultClassDataLoaderFactory;
import com.googlecode.japi.checker.Severity;
import com.googlecode.japi.checker.Utils;
import com.googlecode.japi.checker.model.ClassData;


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
		
		// proceeding of arguments
		/*List<File> referenceClasspath = new ArrayList<File>();
		List<File> newArtifactClasspath = new ArrayList<File>();
		List<AntPatternMatcher> includes = new ArrayList<AntPatternMatcher>();
		List<AntPatternMatcher> excludes = new ArrayList<AntPatternMatcher>();*/
		String referencePath = args[0];
		String newPath = args[1];		
		File reference = new File(referencePath);
		File newArtifact = new File(newPath);
		if (!reference.isDirectory() && !Utils.isArchive(reference)) {
			throw new IllegalArgumentException(
					"reference must be either a directory"
							+ " or a jar (or a zip kind of archive) file");
		}
		if (!newArtifact.isDirectory() && !Utils.isArchive(newArtifact)) {
			throw new IllegalArgumentException(
					"new artifact must be either a directory"
							+ " or a jar (or a zip kind of archive) file");
		}
		boolean reportSourceAffectingDifferences = true;		
		if (args.length > 2) {
			reportSourceAffectingDifferences = (args[2] == null ? true : (args[2].equals("-bin") ? false : true));
		}

		// reading of classes
		ClassDataLoaderFactory<ClassData> classDataLoaderFactory = new DefaultClassDataLoaderFactory();
		ClassDataLoader<ClassData> referenceDataLoader = classDataLoaderFactory.createClassDataLoader();
		try {
			referenceDataLoader.read(reference.toURI());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// TODO classpath + includes, excludes
		/*for (File file : this.referenceClasspath) {
			referenceDataLoader.read(file.toURI());
		}*/
		List<ClassData> referenceClasses = referenceDataLoader.getClasses(reference.toURI());//, includes, excludes);
		ClassDataLoader<ClassData> newArtifactDataLoader = classDataLoaderFactory.createClassDataLoader();
		try {
			newArtifactDataLoader.read(newArtifact.toURI());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// TODO classpath + includes, excludes
		/*for (File file : this.newArtifactClasspath) {
			newArtifactDataLoader.read(file.toURI());
		}*/
		List<ClassData> newClasses = newArtifactDataLoader.getClasses(newArtifact.toURI());//, includes, excludes);

		// checker initialization
		BCChecker checker = new BCChecker();
		CLIReporter reporter = new CLIReporter(reportSourceAffectingDifferences);

		// checking
		checker.checkBackwardCompatibility(reporter, referenceClasses, newClasses);
		System.out.println("Error count: " + reporter.getCount(Severity.ERROR));
		System.out.println("Warning count: " + reporter.getCount(Severity.WARNING));
	}

}