#!/bin/sh

for i in `find . -name pom.xml` 
do
  sed "s/<version>${1}<\/version>/<version>$2<\/version>/" < $i > bla
  mv bla $i
done
