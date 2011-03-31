mvn clean install -Pproduction,oracle
mvn clean install -Pproduction,postgres

cd eid-pki-ra-release
mvn clean install -Poracle
cp target/eid-pki-ra-release-*.zip ..
mvn clean install -Ppostgres
cp target/eid-pki-ra-release-*.zip ..
