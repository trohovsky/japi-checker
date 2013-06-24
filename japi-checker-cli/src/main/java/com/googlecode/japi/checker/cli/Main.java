package com.googlecode.japi.checker.cli;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.googlecode.japi.checker.BCChecker;
import com.googlecode.japi.checker.ClassDataLoader;
import com.googlecode.japi.checker.ClassDataLoaderFactory;
import com.googlecode.japi.checker.DefaultClassDataLoaderFactory;
import com.googlecode.japi.checker.Severity;
import com.googlecode.japi.checker.Utils;
import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.Scope;

/**
 * 
 * @author Tomas Rohovsky
 * 
 */
public class Main {

    @Argument(index = 0, metaVar = "REFERENCE", usage = "reference version of library (JAR archive or directory containing classes)", required = true)
    private File referenceArtifact;
    @Argument(index = 1, metaVar = "NEW", usage = "new version of library (JAR archive or directory containing classes)", required = true)
    private File newArtifact;
    @Option(name = "-b", aliases = "-bin", usage = "report binary incompatibilities only")
    private boolean reportBinaryIncmpatibilitiesOnly = false;

    @Option(name = "-a", aliases = "-api", usage = "check API members only")
    private void setVisibilityLimit(boolean checkAPIMembers) {
        visibilityLimit = Scope.PROTECTED;
    }

    private Scope visibilityLimit = Scope.PRIVATE;

    public static void main(String[] args) {
        Main bean = new Main();
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            parser.parseArgument(args);
            bean.run();
        } catch (CmdLineException e) {
            // handling of wrong arguments
            System.err.println(e.getMessage() + "\n");
            // print usage
            System.err.println("Usage: java -jar japi-checker-cli REFERENCE NEW [options...]");
            System.err.println("Checks API and ABI compatiblity of Java libraries.\n");
            parser.printUsage(System.err);
        }
    }

    public void run() {

        /*
        List<File> referenceClasspath = new ArrayList<File>(); List<File>
        newArtifactClasspath = new ArrayList<File>(); List<AntPatternMatcher>
        includes = new ArrayList<AntPatternMatcher>();
        List<AntPatternMatcher> excludes = new ArrayList<AntPatternMatcher>();
        */

        if (!referenceArtifact.isDirectory()
                && !Utils.isArchive(referenceArtifact)) {
            System.err.println("referenceArtifact library "
                            + referenceArtifact.getName()
                            + " is not a directory or a JAR (or a ZIP kind of archive) file.");
            System.exit(2);
        }
        if (!newArtifact.isDirectory() && !Utils.isArchive(newArtifact)) {
            System.err.println("New library "
                            + newArtifact.getName()
                            + " is not a directory or a JAR (or a ZIP kind of archive) file.");
            System.exit(2);
        }

        // reading of classes
        ClassDataLoaderFactory<ClassData> classDataLoaderFactory = new DefaultClassDataLoaderFactory();

        ClassDataLoader<ClassData> referenceDataLoader = classDataLoaderFactory.createClassDataLoader(visibilityLimit);
        try {
            referenceDataLoader.read(referenceArtifact.toURI());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        // TODO classpath + includes, excludes
        /*
        for (File file : this.referenceClasspath) {
            referenceDataLoader.read(file.toURI()); 
        }
        */
        List<ClassData> referenceClasses = referenceDataLoader.getClasses(referenceArtifact.toURI());// , includes, excludes);

        ClassDataLoader<ClassData> newArtifactDataLoader = classDataLoaderFactory.createClassDataLoader(visibilityLimit);
        try {
            newArtifactDataLoader.read(newArtifact.toURI());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        // TODO classpath + includes, excludes
        /*
        for (File file : this.newArtifactClasspath) {
            newArtifactDataLoader.read(file.toURI());
        }
        */
        List<ClassData> newClasses = newArtifactDataLoader.getClasses(newArtifact.toURI());// , includes, excludes);

        // checker initialization
        BCChecker checker = new BCChecker();
        CLIReporter reporter = new CLIReporter(!reportBinaryIncmpatibilitiesOnly);

        // checking
        checker.checkBackwardCompatibility(reporter, referenceClasses,
                newClasses);
        System.out.println("Error count: " + reporter.getCount(Severity.ERROR));
        System.out.println("Warning count: " + reporter.getCount(Severity.WARNING));

        if (reporter.getCount(Severity.ERROR) > 0) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }

}