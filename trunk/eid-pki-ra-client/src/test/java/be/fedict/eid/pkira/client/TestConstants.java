/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */

package be.fedict.eid.pkira.client;

import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;


public class TestConstants {
	public static final String CSR = "-----BEGIN CERTIFICATE REQUEST-----\n"
			+ "MIIBqDCCARECAQAwaDELMAkGA1UEBhMCQkUxEDAOBgNVBAgTB0xpbWJ1cmcxEDAO\n"
			+ "BgNVBAcTB0hhc3NlbHQxGTAXBgNVBAoTEEFDQSBJVC1Tb2x1dGlvbnMxGjAYBgNV\n"
			+ "BAMTEUphbiBWYW4gZGVuIEJlcmdoMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKB\n"
			+ "gQCl9kIB3oD4AO4RBICIW8jkXXc/alX0cRHb+BrorI8g95eaN15VV4WTXsaw+Kx6\n"
			+ "7xn8QkjgrI76z2dTaBhgaTFdzxYNO4Pqt+ajKGV4BP0Lquh7qyO1k/IT/KyyAdlH\n"
			+ "bJq8VD+Od/AnknnQminCw1TEPZfFck+HGGCvRrSeflRaXwIDAQABoAAwDQYJKoZI\n"
			+ "hvcNAQEFBQADgYEAJTlkGaB4CQIiZ+RP6NFYqe23ZpkU2GpSbUNJRGd7+a7RHQxo\n"
			+ "dGQjZWpctP14R3C0iBHi6G8C7E5zhHWRyFtzxT3MVBFH33kuHxcRkAlDKZNAWyu8\n"
			+ "/pH4Z3DIUVriyhH2pUweqSXaaAaqmgocOH7vJzXLQ3WpNR1UsC/69kNpmpE=\n" + "-----END CERTIFICATE REQUEST-----\n";

	public static final String OPERATOR = "<Operator><Name>Name</Name><Function>Function</Function><Phone>+32 2 1234567</Phone></Operator>";

	public static final String CERTIFICATE_SIGNING_CONTRACT = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>"
			+ "<CertificateSigningRequest Id='request' xmlns:ns2='http://www.w3.org/2000/09/xmldsig#' xmlns='urn:be:fedict:eid:pkira:contracts'>"
			+ "<RequestId>requestId</RequestId>"
			+ "<Description>Description</Description>"
			+ OPERATOR
			+ "<LegalNotice>Test legal notice</LegalNotice>"
			+ "<DistinguishedName>C=BE,ST=Limburg,L=Hasselt,O=ACA IT-Solutions,CN=Jan Van den Bergh</DistinguishedName>"
			+ "<CertificateType>Client</CertificateType>"
			+ "<ValidityPeriodMonths>15</ValidityPeriodMonths>"
			+ "<CSR>"
			+ CSR + "</CSR></CertificateSigningRequest>";
	
	public static final String CERTIFICATE_SIGNING_RESPONSE = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>" +
			"<ns2:CertificateSigningResponse xmlns:ns2='urn:be:fedict:eid:pkira:contracts' xmlns='http://www.w3.org/2000/09/xmldsig#'>" +
			"<ns2:RequestId>requestId</ns2:RequestId>" +
			"<ns2:ResponseId>655d1cce-3130-432c-8814-ee6bc972cf86</ns2:ResponseId>" +
			"<ns2:Result>Invalid Message</ns2:Result>" +
			"<ns2:ResultMessage>signature is missing</ns2:ResultMessage>" +
			"</ns2:CertificateSigningResponse>";

	public static final String CERTIFICATE = "-----BEGIN CERTIFICATE-----\n"
			+ "MIIBvzCCAWkCBgE2EIuX/TANBgkqhkiG9w0BAQUFADBoMQswCQYDVQQGEwJCRTEQ\n"
			+ "MA4GA1UECAwHTGltYnVyZzEQMA4GA1UEBwwHSGFzc2VsdDEZMBcGA1UECgwQQUNB\n"
			+ "IElULVNvbHV0aW9uczEaMBgGA1UEAwwRSmFuIFZhbiBkZW4gQmVyZ2gwHhcNMTIw\n"
			+ "MzE0MDkzNTQ0WhcNMTMwNjE0MDkzNTQ0WjBoMQswCQYDVQQGEwJCRTEQMA4GA1UE\n"
			+ "CAwHTGltYnVyZzEQMA4GA1UEBwwHSGFzc2VsdDEZMBcGA1UECgwQQUNBIElULVNv\n"
			+ "bHV0aW9uczEaMBgGA1UEAwwRSmFuIFZhbiBkZW4gQmVyZ2gwXDANBgkqhkiG9w0B\n"
			+ "AQEFAANLADBIAkEAr6WGZ9YBXg8ywAEvlC2JbY+psXsx3PG2qNgUze2YrqKQC1Dk\n"
			+ "bqzX+tOHNNOc+yhvBmmRtD5S9U7rJ8z1IrBvTwIDAQABMA0GCSqGSIb3DQEBBQUA\n"
			+ "A0EAUjv9OsA2EjUrO+cld/vk85YQTQHXDYzK1q7hw4rE1wrnWubUqSsOT/CWmIy0\n" + "p/OOFjmIarDg+1GaChLg2RPx2Q==\n"
			+ "-----END CERTIFICATE-----";

	public static final String CERTIFICATE_REVOCATION_CONTRACT = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>"
			+ "<CertificateRevocationRequest Id='request' xmlns:ns2='http://www.w3.org/2000/09/xmldsig#' xmlns='urn:be:fedict:eid:pkira:contracts'>"
			+ "<RequestId>requestId</RequestId>"
			+ "<Description>Description</Description>"
			+ OPERATOR
			+ "<LegalNotice>Test legal notice</LegalNotice>"
			+ "<DistinguishedName>C=BE,ST=Limburg,L=Hasselt,O=ACA IT-Solutions,CN=Jan Van den Bergh</DistinguishedName>"
			+ "<ValidityStart>2012-03-14T09:35:44.000Z</ValidityStart>"
			+ "<ValidityEnd>2013-06-14T09:35:44.000Z</ValidityEnd>"
			+ "<Certificate>"
			+ CERTIFICATE
			+ "</Certificate>"
			+ "</CertificateRevocationRequest>";
	
	public static final String CERTIFICATE_REVOCATION_RESPONSE = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>" +
			"<ns2:CertificateRevocationResponse xmlns:ns2='urn:be:fedict:eid:pkira:contracts' xmlns='http://www.w3.org/2000/09/xmldsig#'>" +
			"<ns2:RequestId>requestId</ns2:RequestId>" +
			"<ns2:ResponseId>1262fb1a-67d6-4105-9770-a6b68d323e3f</ns2:ResponseId>" +
			"<ns2:Result>Invalid Message</ns2:Result>" +
			"<ns2:ResultMessage>signature is missing</ns2:ResultMessage>" +
			"</ns2:CertificateRevocationResponse>";

	public static final String REQUEST_ID = "requestId";
	public static final CertificateTypeType CERTIFICATE_TYPE = CertificateTypeType.CLIENT;
	public static final String OPERATOR_NAME = "Name";
	public static final String OPERATOR_FUNCTION = "Function";
	public static final String OPERATOR_PHONE = "+32 2 1234567";
	public static final int VALIDITY_PERIOD = 15;
	public static final String DESCRIPTION = "Description";
	public static final String LEGAL_NOTICE = "Test legal notice";
}
