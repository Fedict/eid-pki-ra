-- TODO remove this file later

-- Configuration
INSERT INTO CONFIGURATION(ENTRY_KEY, ENTRY_VALUE) VALUES('MAIL_SERVER', 'mail.aca-it.be'); -- Dev
--INSERT INTO CONFIGURATION(ENTRY_KEY, ENTRY_VALUE) VALUES('MAIL_SERVER', 'localhost');      -- Test
INSERT INTO CONFIGURATION(ENTRY_KEY, ENTRY_VALUE) VALUES('MAIL_PORT', '25');
INSERT INTO CONFIGURATION(ENTRY_KEY, ENTRY_VALUE) VALUES('MAIL_USER', '');
INSERT INTO CONFIGURATION(ENTRY_KEY, ENTRY_VALUE) VALUES('MAIL_PASSWORD', '');
INSERT INTO CONFIGURATION(ENTRY_KEY, ENTRY_VALUE) VALUES('NOTIFICATION_MAIL_MINUTES', '-1');
INSERT INTO CONFIGURATION(ENTRY_KEY, ENTRY_VALUE) VALUES('VALIDITY_PERIODS', '10,15,30,45');
--INSERT INTO CONFIGURATION(ENTRY_KEY, ENTRY_VALUE) VALUES('DSS_SERVLET', 'https://www.e-contract.be/eid-dss/protocol/simple');
--INSERT INTO CONFIGURATION(ENTRY_KEY, ENTRY_VALUE) VALUES('DSS_WS_CLIENT', 'http://www.e-contract.be/eid-dss-ws/dss');
--INSERT INTO CONFIGURATION(ENTRY_KEY, ENTRY_VALUE) VALUES('IDP_DESTINATION', 'https://www.e-contract.be/eid-idp/protocol/saml2-auth-ident');

-- Certificate authorities
INSERT INTO CA(CA_ID, NAME, XKMS_URL, LEGAL_NOTICE) VALUES (5001, 'Test CA', 'http://localhost:8080/xkms/xkms', 'Test Legal Notice');
INSERT INTO CA_PARAMETERS(CA_CA_ID, mapkey, element) VALUES (5001, 'buc.client', '8047651269');
INSERT INTO CA_PARAMETERS(CA_CA_ID, mapkey, element) VALUES (5001, 'buc.code', '8047651269');
INSERT INTO CA_PARAMETERS(CA_CA_ID, mapkey, element) VALUES (5001, 'buc.server', '8047651269');
INSERT INTO CA_PARAMETERS(CA_CA_ID, mapkey, element) VALUES (5001, 'buc.persons', '8047651269');
INSERT INTO CA_PARAMETERS(CA_CA_ID, mapkey, element) VALUES (5001, 'signing.provider', 'be.fedict.eid.pkira.xkmsws.keyinfo.KeyStoreKeyProvider');
INSERT INTO CA_PARAMETERS(CA_CA_ID, mapkey, element) VALUES (5001, 'signing.keystore.type', 'JKS');
INSERT INTO CA_PARAMETERS(CA_CA_ID, mapkey, element) VALUES (5001, 'signing.keystore.url', 'file:/Users/hans/aca-it/projects/Fedict/eid-pki-ra/dev/workspace/eid-pki-ra/eid-pki-ra-client-xkms/target/test-classes/test.jks'); -- Dev
--INSERT INTO CA_PARAMETERS(CA_CA_ID, mapkey, element) VALUES (5001, 'signing.keystore.url', 'file:/opt/jboss/current/test.jks'); -- Test
INSERT INTO CA_PARAMETERS(CA_CA_ID, mapkey, element) VALUES (5001, 'signing.keystore.entry', 'test');
INSERT INTO CA_PARAMETERS(CA_CA_ID, mapkey, element) VALUES (5001, 'signing.keystore.password', 'changeit');
INSERT INTO CA_PARAMETERS(CA_CA_ID, mapkey, element) VALUES (5001, 'signing.keystore.entry.password', 'changeit');

-- Certificate domains
INSERT INTO CERTIFICATE_DOMAIN (CERTIFICATE_DOMAIN_ID, CERTIFICATE_DOMAIN_NAME, DN_EXPRESSION, CLIENTCERT, SERVERCERT, CODECERT, PERSONSCERT, CA_ID) VALUES (6001, 'Test certificate domain', 'C=BE,ST=*,L=*,O=*,CN=*', 1, 1, 1, 1, 5001);

-- Users
--INSERT INTO PKIRAUSER (USER_ID, LAST_NAME, FIRST_NAME, NATIONAL_REGISTER_NUMBER, IS_ADMIN) VALUES(7001, 'Van den Bergh', 'Jan', '74081447338', 1);
INSERT INTO PKIRAUSER (USER_ID, LAST_NAME, FIRST_NAME, NATIONAL_REGISTER_NUMBER, IS_ADMIN) VALUES(7002, 'Vandenbroeck', 'Hans', '82010631174', 1);
--INSERT INTO PKIRAUSER (LAST_NAME, FIRST_NAME, NATIONAL_REGISTER_NUMBER, IS_ADMIN) VALUES('Gistelinck', 'Katrien', '75050706079', 1);
--INSERT INTO PKIRAUSER (LAST_NAME, FIRST_NAME, NATIONAL_REGISTER_NUMBER, IS_ADMIN) VALUES('Beyl', 'Bert', '73111237304', 1);
--INSERT INTO PKIRAUSER (LAST_NAME, FIRST_NAME, NATIONAL_REGISTER_NUMBER, IS_ADMIN) VALUES('Van Wilder', 'Kevin', '86011736367', 1);
--INSERT INTO PKIRAUSER (LAST_NAME, FIRST_NAME, NATIONAL_REGISTER_NUMBER, IS_ADMIN) VALUES('Van Den Eynde', 'Sam', '77102825708', 1);
--INSERT INTO PKIRAUSER (LAST_NAME, FIRST_NAME, NATIONAL_REGISTER_NUMBER, IS_ADMIN) VALUES('Cornelis', 'Frank', '79102520991', 1);
--INSERT INTO PKIRAUSER (LAST_NAME, FIRST_NAME, NATIONAL_REGISTER_NUMBER, IS_ADMIN) VALUES(7001, 'Test', 'User', '99999999999', 1);

-- Registrations
--INSERT INTO REGISTRATION(REGISTRATION_STATUS, EMAIL, FK_REQUESTER_ID, FK_CERTIFICATE_DOMAIN_ID) VALUES('APPROVED', 'j.vandenbergh@aca-it.be', 7001, 6001);
INSERT INTO REGISTRATION(REGISTRATION_STATUS, EMAIL, FK_REQUESTER_ID, FK_CERTIFICATE_DOMAIN_ID) VALUES('APPROVED', 'h.vandenbroeck@aca-it.be', 7002, 6001);
--INSERT INTO REGISTRATION(REGISTRATION_STATUS, EMAIL, FK_REQUESTER_ID, FK_CERTIFICATE_DOMAIN_ID) VALUES('APPROVED', 'j.vandenbergh@aca-it.be', 1, 2);
--INSERT INTO REGISTRATION(REGISTRATION_STATUS, EMAIL, FK_REQUESTER_ID, FK_CERTIFICATE_DOMAIN_ID) VALUES('APPROVED', 'j.vandenbergh@aca-it.be', 1, 4);
