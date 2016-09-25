#!/bin/sh

# Copyright (c) Philipp Wagner. All rights reserved.
# Licensed under the MIT license. See LICENSE file in the project root for full license information.

# Path to the Executables:
MVN_EXECUTABLE="/Users/bytefish/Development/maven-3.3.9/bin/mvn"
GPG_EXECUTABLE="/usr/local/bin/gpg"

# GPG Key ID used for signing:
GPG_KEY_ID=E4B54CD3

# Logs to be used:
STDOUT=stdout.log
STDERR=stderr.log

# POM File to use for building the project:
POM_FILE=..\pom.xml

# Prompt for Sonatype:
read -p "Sonatype User: " SONATYPE_USER
read -p "Sonatype Password: " SONATYPE_PASSWORD

# Prompt GPG Passphrase:
read -p "GPG Signing Passphrase: " GPG_PASSPHRASE

$MVN_EXECUTABLE clean deploy -Prelease,docs-and-source --settings deploysettings.xml -DskipTests -Dgpg.keyname=$GPG_KEY_ID -Dgpg.executable=$GPG_EXECUTABLE -Dgpg.passphrase=$GPG_PASSPHRASE -DretryFailedDeploymentCount=3 -f $POM_FILE

pause
