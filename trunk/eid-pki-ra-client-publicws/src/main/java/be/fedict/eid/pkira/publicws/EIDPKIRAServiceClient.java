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
package be.fedict.eid.pkira.publicws;

import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import be.fedict.eid.pkira.generated.publicws.EIDPKIRAPortType;
import be.fedict.eid.pkira.generated.publicws.EIDPKIRAService;

/**
 * Client for the public eID PKI RA web service. This client allows you to
 * request and revoke certificates.
 * 
 * @author Jan Van den Bergh
 */
public class EIDPKIRAServiceClient {

	private static final String WSDL_LOCATION = "/wsdl/eid-pki-ra.wsdl";

	private String serviceUrl;

	private EIDPKIRAPortType port;

	/**
	 * Creates a service client using the default URL in the WSDL.
	 */
	public EIDPKIRAServiceClient() {
		serviceUrl = null;
	}

	/**
	 * Creates a service client using the specified URL.
	 * 
	 * @param serviceUrl
	 *            URL to access the web service.
	 */
	public EIDPKIRAServiceClient(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	/**
	 * Sends a certificate request contract to the back-end web service.
	 * 
	 * @param contract
	 *            the contract (base64 encoded XML).
	 * @return the result value (base64 encoded XML).
	 */
	public String signCertificate(String contract) {
		return getWebservicePort().signCertificate(contract);
	}

	/**
	 * Sends a certificate revocation contract to the back-end web service.
	 * 
	 * @param contract
	 *            the contract (base64 encoded XML).
	 * @return the result value (base64 encoded XML).
	 */
	public String revokeCertificate(String contract) {
		return getWebservicePort().revokeCertificate(contract);
	}

	private synchronized EIDPKIRAPortType getWebservicePort() {
		// Use the cache port if it is available
		if (port != null) {
			return port;
		}

		// Get the WSDL
		URL wsdlLocation = getClass().getResource(WSDL_LOCATION);
		if (wsdlLocation == null) {
			throw new RuntimeException("Cannot find wsdl for service at " + WSDL_LOCATION);
		}

		// Create web service port
		QName serviceName = new QName("urn:be:fedict:eid:dss:ws", "EIDPKIRAPrivateService");
		EIDPKIRAService service = new EIDPKIRAService(wsdlLocation, serviceName);
		port = service.getEIDPKIRAPort();

		// Change the endpoint address
		if (serviceUrl != null) {
			BindingProvider bp = (BindingProvider) port;
			Map<String, Object> context = bp.getRequestContext();
			context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);
		}

		return port;
	}

	/**
	 * Injects the port (used in unit tests)
	 */
	void setWebservicePort(EIDPKIRAPortType port) {
		this.port = port;
	}
}
