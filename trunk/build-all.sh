#!/bin/bash
mvn clean install && growlnotify -s -m "Build complete" || growlnotify -s -m "Build failed"
