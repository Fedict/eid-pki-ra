#!/bin/bash
/opt/maven/apache-maven-2.2.1/bin/mvn clean install && growlnotify -s -m "Build complete" || growlnotify -s -m "Build failed"
