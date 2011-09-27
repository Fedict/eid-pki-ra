/opt/maven/apache-maven-2.2.1/bin/mvn clean install -Pproduction,oracle
/opt/maven/apache-maven-2.2.1/bin/mvn clean install -Pproduction,postgres

cd eid-pki-ra-release
/opt/maven/apache-maven-2.2.1/bin/mvn clean install -Poracle
cp target/eid-pki-ra-release-*.zip ..
/opt/maven/apache-maven-2.2.1/bin/mvn clean install -Ppostgres
cp target/eid-pki-ra-release-*.zip ..
