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
package be.fedict.eid.integration.ws;

import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;
import be.fedict.eid.pkira.publicws.EIDPKIRAServiceClient;


/**
 * @author Jan Van den Bergh
 *
 */
public class WebServiceFactory {
	
	private static final String PRIVATE_SERVICE_URL = "http://localhost:8080/eid-pki-ra/webservice/EIDPKIRAPrivateService";
	private static final EIDPKIRAPrivateServiceClient privateWebServiceClient;
	
	private static final String PUBLIC_SERVICE_URL = "http://localhost:8080/eid-pki-ra/webservice/EIDPKIRAService";

	private static final EIDPKIRAServiceClient publicWebServiceClient;
	
	static {
		publicWebServiceClient = new EIDPKIRAServiceClient();
		publicWebServiceClient.setServiceUrl(PUBLIC_SERVICE_URL);
		privateWebServiceClient = new EIDPKIRAPrivateServiceClient();
		privateWebServiceClient.setServiceUrl(PRIVATE_SERVICE_URL);
	}
	
	public static EIDPKIRAPrivateServiceClient getPrivateWebServiceClient() {
		return privateWebServiceClient;
	}

	public static EIDPKIRAServiceClient getPublicWebServiceClient() {
		return publicWebServiceClient;
	}
}
