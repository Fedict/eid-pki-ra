mvn clean install -Pproduction,oracle
mvn clean install -Pproduction,postgres

cd eid-pki-ra-release
mvn clean install -Pfedict-baseline-oracle
cp target/eid-pki-ra-release-*.zip ..
mvn clean install -f eid-pki-ra-release/pom.xml -Pfedict-baseline-postgres
cp target/eid-pki-ra-release-*.zip ..
