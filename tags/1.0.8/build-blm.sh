#!/bin/bash
mvn clean install -f pom-blm.xml && ant deploy-blm && growlnotify -s -m "Build complete" || growlnotify -s -m "Build failed"
