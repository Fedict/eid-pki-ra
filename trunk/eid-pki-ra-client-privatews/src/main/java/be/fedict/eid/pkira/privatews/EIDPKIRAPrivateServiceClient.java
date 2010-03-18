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
package be.fedict.eid.pkira.privatews;

import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.generated.privatews.CreateCertificateDomainRequest;
import be.fedict.eid.pkira.generated.privatews.CreateCertificateDomainResponse;
import be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType;
import be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivateService;
import be.fedict.eid.pkira.generated.privatews.FindCertificateRequest;
import be.fedict.eid.pkira.generated.privatews.FindCertificateResponse;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesRequest;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesResponse;
import be.fedict.eid.pkira.generated.privatews.ObjectFactory;

/**
 * Client to access the private eID PKI RA web service. This hides the
 * difficulties introduced by the JAX-B API.
 * 
 * @author Jan Van den Bergh
 */
public class EIDPKIRAPrivateServiceClient {

	public static final String NAME = "eidPKIRAPrivateServiceClient";

	private static final String WSDL_LOCATION = "/wsdl/eid-pki-ra-private.wsdl";

	private final ObjectFactory factory;

	private String serviceUrl;
	private EIDPKIRAPrivatePortType port;

	public EIDPKIRAPrivateServiceClient() {
		this.factory = new ObjectFactory();
	}

	/**
	 * Lists all the certificates that are available to the user.
	 * 
	 * @param userRRN
	 *            the "rijksregisternummer" (national number) of the user.
	 * @return the list of certificates available to him.
	 */
	public List<CertificateWS> listCertificates(String userRRN) {
		ListCertificatesRequest request = factory.createListCertificatesRequest();
		request.setUserRRN(userRRN);
		ListCertificatesResponse response = getWebservicePort().listCertificates(request);

		return response.getCertificates();
	}

	public CertificateWS findCertificate(String userRRN, String serialNumber) {
		FindCertificateRequest request = factory.createFindCertificateRequest();
		request.setUserRRN(userRRN);
		request.setSerialNumber(serialNumber);
		FindCertificateResponse response = getWebservicePort().findCertificate(request);
		return response.getCertificate();
	}

	/**
	 * Creates a certificate domain.
	 * 
	 * @param name
	 *            name of the domain.
	 * @param dnExpression
	 *            dn filter expression.
	 * @param clientCertificate
	 *            used for client certicates?
	 * @param serverCertificate
	 *            used for server certificates?
	 * @param codeCertificate
	 *            used for code signing certificates?
	 * @return the response, indicating the result and on success the id of the
	 *         newly created domain.
	 */
	public CreateCertificateDomainResponse createCertificateDomain(String name, String dnExpression,
			boolean clientCertificate, boolean serverCertificate, boolean codeCertificate) {
		CreateCertificateDomainRequest request = factory.createCreateCertificateDomainRequest();
		request.setCaId(null); // TODO Add CA ID
		request.setName(name);
		request.setDnExpression(dnExpression);
		if (clientCertificate) {
			request.getCertificateTypes().add(CertificateTypeWS.CLIENT);
		}
		if (serverCertificate) {
			request.getCertificateTypes().add(CertificateTypeWS.SERVER);
		}
		if (codeCertificate) {
			request.getCertificateTypes().add(CertificateTypeWS.CODE);
		}

		return getWebservicePort().createCertificateDomain(request);
	}

	/**
	 * Creates the web service port.
	 */
	private synchronized EIDPKIRAPrivatePortType getWebservicePort() {
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
		QName serviceName = new QName("urn:be:fedict:eid:pkira:privatews", "EIDPKIRAPrivateService");
		EIDPKIRAPrivateService service = new EIDPKIRAPrivateService(wsdlLocation, serviceName);
		port = service.getEIDPKIRAPrivatePort();

		// Change the endpoint address
		if (serviceUrl != null) {
			BindingProvider bp = (BindingProvider) port;
			Map<String, Object> context = bp.getRequestContext();
			context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);
		}

		return port;
	}

	/**
	 * Injects the port (used for unit testing).
	 */
	void setWebservicePort(EIDPKIRAPrivatePortType port) {
		this.port = port;
	}

	/**
	 * Sets the URL of the web service.
	 */
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

}
