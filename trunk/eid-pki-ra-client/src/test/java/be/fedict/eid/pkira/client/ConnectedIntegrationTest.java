package be.fedict.eid.pkira.client;

import static be.fedict.eid.pkira.client.TestConstants.CERTIFICATE;
import static be.fedict.eid.pkira.client.TestConstants.CERTIFICATE_TYPE;
import static be.fedict.eid.pkira.client.TestConstants.CSR;
import static be.fedict.eid.pkira.client.TestConstants.DESCRIPTION;
import static be.fedict.eid.pkira.client.TestConstants.LEGAL_NOTICE;
import static be.fedict.eid.pkira.client.TestConstants.OPERATOR_FUNCTION;
import static be.fedict.eid.pkira.client.TestConstants.OPERATOR_NAME;
import static be.fedict.eid.pkira.client.TestConstants.OPERATOR_PHONE;
import static be.fedict.eid.pkira.client.TestConstants.REQUEST_ID;
import static be.fedict.eid.pkira.client.TestConstants.VALIDITY_PERIOD;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;

public class ConnectedIntegrationTest {

	public static void main(String[] args) throws PKIRAClientException {
		PKIRAClient client = new PKIRAClient();
		client.setServiceUrl("http://localhost:8080/eid-pki-ra/webservice/EIDPKIRAService");

		String contract = client.createCertificateSigningRequestContract(REQUEST_ID, CSR, CERTIFICATE_TYPE, OPERATOR_NAME, OPERATOR_FUNCTION, OPERATOR_PHONE, VALIDITY_PERIOD, DESCRIPTION, LEGAL_NOTICE);
		CertificateSigningResponseType response = client.sendCertificateSigningRequest(contract);
		
		String contract2 = client.createCertificateRevocationRequestContract(REQUEST_ID, CERTIFICATE, OPERATOR_NAME, OPERATOR_FUNCTION, OPERATOR_PHONE, DESCRIPTION, LEGAL_NOTICE);
		CertificateRevocationResponseType response2 = client.sendCertificateRevocationRequest(contract2);
	}

}
