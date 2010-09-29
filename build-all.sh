#!/bin/bash
mvn clean install && ant deploy-blm && growlnotify -s -m "Build complete" || growlnotify -s -m "Build failed"
