#!/bin/sh

for i in `find . -name pom.xml`
do
  sed "s/<version>${1}<\/version>/<version>$2<\/version>/" < $i > bla
  mv bla $i
done

sed "s/${1}/$2/" < eid-pki-ra-dev-guide/src/main/docbook/eid-pki-ra-dev-guide.xml > bla
mv bla eid-pki-ra-dev-guide/src/main/docbook/eid-pki-ra-dev-guide.xml
