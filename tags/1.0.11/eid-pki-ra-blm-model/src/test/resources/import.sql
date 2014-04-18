-- Users
INSERT INTO PKIRAUSER (USER_ID, LAST_NAME, FIRST_NAME, NATIONAL_REGISTER_NUMBER, IS_ADMIN, LOCALE) VALUES(2001, 'Puk', 'Pietje', '99123129212', 0, 'en');
INSERT INTO PKIRAUSER (USER_ID, LAST_NAME, FIRST_NAME, NATIONAL_REGISTER_NUMBER, IS_ADMIN, LOCALE) VALUES (2002, 'Baeyens', 'Bram', '75033022781', 1, 'en');
	
-- Certificate authorities
INSERT INTO CA(CA_ID, NAME, XKMS_URL, LEGAL_NOTICE) VALUES (5001, 'CertiPost CA', 'http://xkms-url.be/xkms', 'Test Legal Notice');

-- Certificate domains
INSERT INTO CERTIFICATE_DOMAIN (CERTIFICATE_DOMAIN_ID, CERTIFICATE_DOMAIN_NAME, DN_EXPRESSION, CLIENTCERT, SERVERCERT, CODECERT, PERSONSCERT, CA_ID) VALUES (1001, 'eHealth Client Certificates', 'c=be,ou=eHealth,uid=*', 1, 0, 0, 0, 5001);
INSERT INTO CERTIFICATE_DOMAIN (CERTIFICATE_DOMAIN_ID, CERTIFICATE_DOMAIN_NAME, DN_EXPRESSION, CLIENTCERT, SERVERCERT, CODECERT, PERSONSCERT, CA_ID) VALUES (1002, 'eHealth Server Certificates', 'c=be,ou=eHealth,cn=*', 0, 1, 0, 0, 5001);
INSERT INTO CERTIFICATE_DOMAIN (CERTIFICATE_DOMAIN_ID, CERTIFICATE_DOMAIN_NAME, DN_EXPRESSION, CLIENTCERT, SERVERCERT, CODECERT, PERSONSCERT, CA_ID) VALUES (1003, 'eHealth Code Signing Certificates', 'c=be,ou=eHealth,uid=*', 0, 0, 1, 0, 5001);
INSERT INTO CERTIFICATE_DOMAIN (CERTIFICATE_DOMAIN_ID, CERTIFICATE_DOMAIN_NAME, DN_EXPRESSION, CLIENTCERT, SERVERCERT, CODECERT, PERSONSCERT, CA_ID) VALUES (1005, 'eHealth Persons Certificates', 'c=be,ou=eHealth,uid=*', 0, 0, 0, 1, 5001);
INSERT INTO CERTIFICATE_DOMAIN (CERTIFICATE_DOMAIN_ID, CERTIFICATE_DOMAIN_NAME, DN_EXPRESSION, CLIENTCERT, SERVERCERT, CODECERT, PERSONSCERT, CA_ID) VALUES (1004, 'Test', 'O=ACA,C=BE', 1, 1, 1, 1, 5001);

-- Registrations
INSERT INTO REGISTRATION(REGISTRATION_ID, REGISTRATION_STATUS, EMAIL, FK_REQUESTER_ID, FK_CERTIFICATE_DOMAIN_ID) VALUES(3001, 'NEW', 'p.puk@aca-it.be', 2001, 1001);
INSERT INTO REGISTRATION(REGISTRATION_ID, REGISTRATION_STATUS, EMAIL, FK_REQUESTER_ID, FK_CERTIFICATE_DOMAIN_ID) VALUES(3002, 'APPROVED', 'p.puk@aca-it.be', 2001, 1002);

-- Configuration
INSERT INTO CONFIGURATION(ENTRY_ID, ENTRY_KEY, ENTRY_VALUE) VALUES(2001, 'MAIL_SERVER', 'test');
INSERT INTO CONFIGURATION(ENTRY_ID, ENTRY_KEY, ENTRY_VALUE) VALUES(2002, 'MAIL_PORT', '1111');
INSERT INTO CONFIGURATION(ENTRY_ID, ENTRY_KEY, ENTRY_VALUE) VALUES(2003, 'NOTIFICATION_MAIL_MINUTES', '2');
INSERT INTO CONFIGURATION(ENTRY_ID, ENTRY_KEY, ENTRY_VALUE) VALUES(2004, 'VALIDITY_PERIODS', '6,12,15,24,36,120');
INSERT INTO CONFIGURATION(ENTRY_ID, ENTRY_KEY, ENTRY_VALUE) VALUES(2005, 'DSS_WS_LOCATION', 'http://localhost:8080/dds');

-- Contracts
INSERT INTO CONTRACT(CONTRACT_ID, TYPE, CONTRACT_DOCUMENT, SUBJECT, REQUESTER, CERTIFICATE_DOMAIN_ID, CREATION_DATE, RESULT, CERTIFICATE_TYPE) VALUES(9001, 'Sign', '<signing/>', 'c=be,o=fedict', 'Pietje Puk', 1001, '2011-12-30 20:55:00', 'SUCCESS', 'CLIENT');
INSERT INTO CONTRACT(CONTRACT_ID, TYPE, CONTRACT_DOCUMENT, SUBJECT, REQUESTER, CERTIFICATE_DOMAIN_ID, CREATION_DATE, RESULT, CERTIFICATE_TYPE) VALUES(9002, 'Sign', '<signing/>', 'c=be,o=fedict', 'Pietje Puk', 1001, '2011-10-17 21:55:00', 'INVALID_MESSAGE', null);
INSERT INTO CONTRACT(CONTRACT_ID, TYPE, CONTRACT_DOCUMENT, SUBJECT, REQUESTER, CERTIFICATE_DOMAIN_ID, CREATION_DATE, RESULT, CERTIFICATE_TYPE) VALUES(9003, 'Sign', '<signing/>', 'c=be,o=fedict', 'Pietje Puk', 1002, '2012-01-01 21:55:00', 'SUCCESS', 'CLIENT');