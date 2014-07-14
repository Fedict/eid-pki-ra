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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.UUID;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;
import be.fedict.eid.pkira.generated.contracts.ResultType;

import static org.testng.Assert.assertEquals;

public class ConnectedIntegrationTest {

    private static final String KEYSTORE_PASSWORD = "changeit";
    private static final String ENTRY_PASSWORD = "changeit";

    private static final String PKIRA_SERVICE_URL = "http://linux-test.aca-it.be:8080/eid-pki-ra/webservice/EIDPKIRAService";
    private static final String CSR =
            "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIICXTCCAUUCAQAwGDEWMBQGA1UEAwwNd3d3Lmdvb2dsZS5iZTCCASIwDQYJKoZI\n" +
            "hvcNAQEBBQADggEPADCCAQoCggEBAMH2EMKaWVWsi8M9fZHEf4H7o2eVBKBUkgXp\n" +
            "4e3Nf7wEkMtO9+yV+3ABgMv94niaVHO8J7Euit792eSGdzxLMkKXY0CwoLqs4I2C\n" +
            "2pjc6OVka1beRfz4O4DFxcxkFowbxFGAHvcYNVDcJ3nEw9+BzgfN0KI6Gt9imuf7\n" +
            "qUAh5/ribKfUoZH4mY3JlZjmxG40bfOFuYzXeCt49B83P14EFDiRorCxVatkw+P6\n" +
            "nVrhrgHasCksHk2LcgMm6MKDEM7VQfu01f5nxIVdpcLi5HlIZDiqIed5Pd9oxglb\n" +
            "kKUDu2C7OcfYo2Lbidg0cxivWef6ME0eaKY/u/zWenuaNL/CSnUCAwEAAaAAMA0G\n" +
            "CSqGSIb3DQEBBQUAA4IBAQCgYaJKmUly25qXkRcpDpn2TpvflcPI4g0lI2VUDl9h\n" +
            "M+wzXU9JZ1ysNy+BOZqY5KSHIu+hXghLOPkbVH5w6YTjCdSdzfVwA1ssn6Vm9kn3\n" +
            "sOBmKJb74Oj7wazqyN4rigS4Pv7gnjFnmyAY1CY0yR+esRk/Dzu4PN6GL6wqEl45\n" +
            "Y2uIgINSuh8kVf+m29WAmZmo6UpMdq6BzBsi4IMukjZ/iyjn1Ia76z1pNCuvh3bf\n" +
            "GS8ZSv8bmdkaek2TvJrN5eZL+41mKXlkqjptAsCcq7ojROmc7+u7HYJfHmBdFDde\n" +
            "d0k17Q9spWJowJPlednOaCpehGdyoDgip+s0stptQVZi\n" +
            "-----END CERTIFICATE REQUEST-----\n";
    private static final String CERTIFICATE = "-----BEGIN CERTIFICATE-----\n"
            + "MIIBvzCCAWkCBgE2EIuX/TANBgkqhkiG9w0BAQUFADBoMQswCQYDVQQGEwJCRTEQ\n"
            + "MA4GA1UECAwHTGltYnVyZzEQMA4GA1UEBwwHSGFzc2VsdDEZMBcGA1UECgwQQUNB\n"
            + "IElULVNvbHV0aW9uczEaMBgGA1UEAwwRSmFuIFZhbiBkZW4gQmVyZ2gwHhcNMTIw\n"
            + "MzE0MDkzNTQ0WhcNMTMwNjE0MDkzNTQ0WjBoMQswCQYDVQQGEwJCRTEQMA4GA1UE\n"
            + "CAwHTGltYnVyZzEQMA4GA1UEBwwHSGFzc2VsdDEZMBcGA1UECgwQQUNBIElULVNv\n"
            + "bHV0aW9uczEaMBgGA1UEAwwRSmFuIFZhbiBkZW4gQmVyZ2gwXDANBgkqhkiG9w0B\n"
            + "AQEFAANLADBIAkEAr6WGZ9YBXg8ywAEvlC2JbY+psXsx3PG2qNgUze2YrqKQC1Dk\n"
            + "bqzX+tOHNNOc+yhvBmmRtD5S9U7rJ8z1IrBvTwIDAQABMA0GCSqGSIb3DQEBBQUA\n"
            + "A0EAUjv9OsA2Ecd ei   jUrO+cld/vk85YQTQHXDYzK1q7hw4rE1wrnWubUqSsOT/CWmIy0\n"
            + "p/OOFjmIarDg+1GaChLg2RPx2Q==\n"
            + "-----END CERTIFICATE-----";
    private static final String OPERATOR_NAME = "Name";
    private static final String OPERATOR_FUNCTION = "Function";
    private static final String OPERATOR_PHONE = "+323123456";
    private static final int VALIDITY_PERIOD_MONTHS = 15;
    private static final String DESCRIPTION = "Description";
    private static final String TEST_LEGAL_NOTICE = "Test legal notice";

    private KeyStore.PrivateKeyEntry privateKeyEntry;
    private PKIRAClient pkiraClient;

    @BeforeMethod
    public void loadKeyStore() throws GeneralSecurityException, IOException {
        // Extract the private key entry used to sign contracts
        KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(ConnectedIntegrationTest.class.getResourceAsStream("/test.jks"), KEYSTORE_PASSWORD.toCharArray());
        KeyStore.PasswordProtection passwordProtection = new KeyStore.PasswordProtection(ENTRY_PASSWORD.toCharArray());
        privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("test", passwordProtection);
    }

    @BeforeMethod
    public void setupClient() {
        pkiraClient = new PKIRAClientImpl();
        pkiraClient.setServiceUrl(PKIRA_SERVICE_URL); // URL of the service (to be provided by FedICT).
    }

    @Test(enabled = false)
    public void canSendCertificateSigningRequest() throws PKIRAClientException {
        // Create the unsigned contract
        String unsignedContract = pkiraClient.createCertificateSigningRequestContract(
                generateRequestId(),
                CSR,                            // CSR to be signed
                CertificateTypeType.CLIENT,     // Type of certificate
                OPERATOR_NAME,                  // Name of the operator (person requesting the certificate)
                OPERATOR_FUNCTION,              // Function of the operator
                OPERATOR_PHONE,                 // Phone number of the operator
                VALIDITY_PERIOD_MONTHS,         // Validity period for the certificate (15 or 27 months)
                DESCRIPTION,                    // Custom description to put in the contract
                TEST_LEGAL_NOTICE);             // Legal notice (provided by FedICT)

        // Sign the request contract
        String signedContract = pkiraClient.signRequestContract(unsignedContract, privateKeyEntry);

        // Send the contract to the backend
        CertificateSigningResponseType signingResponse = pkiraClient.sendCertificateSigningRequest(signedContract);
        System.out.println(signingResponse.getResult());
        System.out.println(signingResponse.getResultMessage());

        assertEquals(signingResponse.getResult(), ResultType.SUCCESS);
    }

    @Test(enabled = false)
    public void canSendCertificateRevocationRequest() throws PKIRAClientException {
        // Create the unsigned contract
        String unsignedContract = pkiraClient.createCertificateRevocationRequestContract(
                generateRequestId(),
                CERTIFICATE,                    // Certificate to revoke
                OPERATOR_NAME,                  // Name of the operator (person requesting the certificate)
                OPERATOR_FUNCTION,              // Function of the operator
                OPERATOR_PHONE,                 // Phone number of the operator
                DESCRIPTION,                    // Custom description to put in the contract
                TEST_LEGAL_NOTICE);             // Legal notice (provided by FedICT)

        // Sign the request contract
        String signedContract = pkiraClient.signRequestContract(unsignedContract, privateKeyEntry);

        // Send the contract to the backend
        CertificateRevocationResponseType revocationResponse = pkiraClient.sendCertificateRevocationRequest(signedContract);
        System.out.println(revocationResponse.getResult());
        System.out.println(revocationResponse.getResultMessage());

        assertEquals(revocationResponse.getResult(), ResultType.SUCCESS);
    }

    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }

}
