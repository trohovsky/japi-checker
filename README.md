# japi-checker

A tool for checking API compatibility of Java libraries.

This is a fork of [japi-checker](http://code.google.com/p/japi-checker/source/checkout).

## Building

It requires Java 7 for building.

    mvn package

## Execution

    java -jar japi-checker/target/japi-checker-${VERSION}.jar REFERENCE_LIBRARY NEW_LIBRARY [-bin]

        REFERENCE_LIBRARY - a reference library, JAR archive or directory containing classes
        NEW_LIBRARY - a new library, JAR archive or directory containing classes

        -bin    check only binary compatibility (default - source and binary compatibility)

## Notes

Module japi-checker-maven-plugin is not maintained.

