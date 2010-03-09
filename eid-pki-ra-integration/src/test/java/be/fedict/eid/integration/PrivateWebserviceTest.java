/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */
package be.fedict.eid.integration;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

/**
 * Test of the public web service.
 * 
 * @author Jan Van den Bergh
 */
public class PrivateWebserviceTest {

	private static final String SERVICE_URL = "http://localhost:8080/eid-pki-ra/webservice/EIDPKIRAPrivateService";
	private  EIDPKIRAPrivateServiceClient privateWebserviceClient;
	
	@BeforeMethod
	public void setup() {		
		privateWebserviceClient = new EIDPKIRAPrivateServiceClient();
		privateWebserviceClient.setServiceUrl(SERVICE_URL);
	}
	
	@Test
	public void getCertificateList() {
		findCertificates("");
	}

	private void findCertificates(String userRRN) {
		// Sign the message
		List<CertificateWS> responseMsg = privateWebserviceClient.listCertificates(userRRN);
		
		Assert.assertNotNull(responseMsg);
	}

}
