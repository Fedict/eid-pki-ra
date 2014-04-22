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

import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;

import static be.fedict.eid.pkira.client.TestConstants.*;

public class ConnectedIntegrationTest {

	public static void main(String[] args) throws PKIRAClientException {
		PKIRAClient client = new PKIRAClientImpl();
		client.setServiceUrl("http://localhost:8080/eid-pki-ra/webservice/EIDPKIRAService");

		// certificate request
		String contract1 = client.createCertificateSigningRequestContract(REQUEST_ID, CSR, CERTIFICATE_TYPE,
				OPERATOR_NAME, OPERATOR_FUNCTION, OPERATOR_PHONE, VALIDITY_PERIOD, DESCRIPTION, LEGAL_NOTICE);		
		CertificateSigningResponseType response1 = client.sendCertificateSigningRequest(contract1);
		System.out.println(client.responseContainsErrors(response1));
		
		// certificate revocation		
		String contract2 = client.createCertificateRevocationRequestContract(REQUEST_ID, CERTIFICATE, OPERATOR_NAME, OPERATOR_FUNCTION, OPERATOR_PHONE, DESCRIPTION, LEGAL_NOTICE);
		CertificateRevocationResponseType response2 = client.sendCertificateRevocationRequest(contract2);
		System.out.println(client.responseContainsErrors(response2));
	}

}
