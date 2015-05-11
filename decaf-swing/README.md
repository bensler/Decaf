#Decaf Swing

Build
=====
Test code of this project depends on JGoodies libs. You may install them into your local repository using the following Maven commands:

    mvn install:install-file -Dfile=./lib/jgoodies-common-1.9.0/jgoodies-common-1.9.0.jar -DpomFile=./lib/jgoodies-common-1.9.0/pom.xml -Dsources=./lib/jgoodies-common-1.9.0/jgoodies-common-1.9.0-sources.jar

    mvn install:install-file -Dfile=./lib/jgoodies-forms-1.10/jgoodies-forms-1.10.jar -DpomFile=./lib/jgoodies-forms-1.10/pom.xml -Dsources=./lib/jgoodies-forms-1.10/jgoodies-forms-1.10-sources.jar

    mvn install:install-file -Dfile=./lib/jgoodies-looks-2.8.0/jgoodies-looks-2.8.0.jar -DpomFile=./lib/jgoodies-looks-2.8.0/pom.xml -Dsources=./lib/jgoodies-looks-2.8.0/jgoodies-looks-2.8.0-sources.jar

