:: Copyright (c) Philipp Wagner. All rights reserved.
:: Licensed under the MIT license. See LICENSE file in the project root for full license information.

@echo off

echo ---------------------------------------------------
echo - Bundling Artifacts for Manual Repository Upload -
echo ---------------------------------------------------

:: Define the Executables, so we don't have to rely on pathes:
set JAR_EXECUTABLE="C:\Program Files\Java\jdk1.8.0_71\bin\jar.exe"
set GPG_EXECUTABLE="C:\Program Files (x86)\GNU\GnuPG\pub\gpg.exe"

:: Logs to be used:
set STDOUT=stdout.log
set STDERR=stderr.log

:: Set the Target Bundle file:
set TARGET_BUNDLE=bundle.jar

set FILENAME=jtinycsvparser-1.0

:: Define the Artifacts to be signed. Simply use an absolute path here:
set JAR_DIR=D:\github\JTinyCsvParser\JTinyCsvParser\out\artifacts\JTinyCsvParser_1_0
set JAR_FILE=jtinycsvparser-1.0.jar
set JAR_SOURCES_FILE=jtinycsvparser-1.0-sources.jar
set JAR_JAVADOC_FILE=jtinycsvparser-1.0-javadoc.jar

set POM_DIR=D:\github\JTinyCsvParser\JTinyCsvParser
set POM_FILE=pom.xml

set JAR_FILE_ASC=%JAR_FILE%.asc
set POM_FILE_ASC=%POM_FILE%.asc
set JAR_SOURCES_FILE_ASC=%JAR_SOURCES_FILE%.asc
set JAR_JAVADOC_FILE_ASC=%JAR_JAVADOC_FILE%.asc

:: Ask for GPG Signing Passphrase:

set /p PASSPHRASE="Signing Passphrase: "

1>%STDOUT% 2>%STDERR% (
    
    :: Create Fake:
    %JAR_EXECUTABLE% -cf %JAR_SOURCES_FILE% README.txt
    %JAR_EXECUTABLE% -cf %JAR_JAVADOC_FILE% README.txt
    
    :: Sign JAR and POM Files:
    echo %PASSPHRASE%|%GPG_EXECUTABLE% --batch --yes --passphrase-fd 0  -b -a -s "%JAR_DIR%\%JAR_FILE%"  
    echo %PASSPHRASE%|%GPG_EXECUTABLE% --batch --yes --passphrase-fd 0  -b -a -s "%POM_DIR%\%POM_FILE%"
    echo %PASSPHRASE%|%GPG_EXECUTABLE% --batch --yes --passphrase-fd 0  -b -a -s "%JAR_SOURCES_FILE%"
    echo %PASSPHRASE%|%GPG_EXECUTABLE% --batch --yes --passphrase-fd 0  -b -a -s "%JAR_JAVADOC_FILE%"

    %JAR_EXECUTABLE% -cf "%TARGET_BUNDLE%" -C "%JAR_DIR%" "%JAR_FILE%" -C "%JAR_DIR%" "%JAR_FILE_ASC%" -C "%POM_DIR%" "%POM_FILE%" -C "%POM_DIR%" "%POM_FILE_ASC%" -C . %JAR_SOURCES_FILE% %JAR_JAVADOC_FILE% %JAR_SOURCES_FILE_ASC% %JAR_JAVADOC_FILE_ASC% 
    
)

pause