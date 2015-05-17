# Decaf

Java decaffeinated

[decaf-util](decaf-util) My playground project for practicing Test Driven Development. 

[decaf-testutil](decaf-testutil) Some classes for performing integration tests on Java Swing applications the brute force way. 

[decaf-swing](decaf-swing) Some GUI layer visualizing a Hierarchy instance from decaf-util being tested by Bender from decaf-testutil.

## Build
Test code of [decaf-testutil](decaf-testutil) and [decaf-swing](decaf-swing) depends on JGoodies libs which are not available in a public maven repo. You need to install them into your local repository using the following maven commands:

    mvn install:install-file -Dfile=./lib/jgoodies-common-1.9.0/jgoodies-common-1.9.0.jar -DpomFile=./lib/jgoodies-common-1.9.0/pom.xml -Dsources=./lib/jgoodies-common-1.9.0/jgoodies-common-1.9.0-sources.jar

    mvn install:install-file -Dfile=./lib/jgoodies-forms-1.10/jgoodies-forms-1.10.jar -DpomFile=./lib/jgoodies-forms-1.10/pom.xml -Dsources=./lib/jgoodies-forms-1.10/jgoodies-forms-1.10-sources.jar

    mvn install:install-file -Dfile=./lib/jgoodies-looks-2.8.0/jgoodies-looks-2.8.0.jar -DpomFile=./lib/jgoodies-looks-2.8.0/pom.xml -Dsources=./lib/jgoodies-looks-2.8.0/jgoodies-looks-2.8.0-sources.jar

