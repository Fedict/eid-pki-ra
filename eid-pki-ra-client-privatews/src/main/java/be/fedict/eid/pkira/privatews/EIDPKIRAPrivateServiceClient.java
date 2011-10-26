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

import be.fedict.eid.pkira.generated.privatews.CertificateDomainWS;
import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.generated.privatews.ConfigurationEntryKeyWS;
import be.fedict.eid.pkira.generated.privatews.ConfigurationEntryWS;
import be.fedict.eid.pkira.generated.privatews.ContractWS;
import be.fedict.eid.pkira.generated.privatews.CreateOrUpdateRegistrationRequest;
import be.fedict.eid.pkira.generated.privatews.CreateOrUpdateRegistrationResponse;
import be.fedict.eid.pkira.generated.privatews.CreateRegistrationForUserRequest;
import be.fedict.eid.pkira.generated.privatews.CreateRegistrationForUserResponse;
import be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType;
import be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivateService;
import be.fedict.eid.pkira.generated.privatews.FindCertificateDomainRequest;
import be.fedict.eid.pkira.generated.privatews.FindCertificateDomainResponse;
import be.fedict.eid.pkira.generated.privatews.FindCertificateRequest;
import be.fedict.eid.pkira.generated.privatews.FindCertificateResponse;
import be.fedict.eid.pkira.generated.privatews.FindConfigurationEntryRequest;
import be.fedict.eid.pkira.generated.privatews.FindConfigurationEntryResponse;
import be.fedict.eid.pkira.generated.privatews.FindContractDocumentRequest;
import be.fedict.eid.pkira.generated.privatews.FindContractDocumentResponse;
import be.fedict.eid.pkira.generated.privatews.FindContractsRequest;
import be.fedict.eid.pkira.generated.privatews.FindContractsResponse;
import be.fedict.eid.pkira.generated.privatews.FindRegisteredCertificateDomainsForUserRequest;
import be.fedict.eid.pkira.generated.privatews.FindRegisteredCertificateDomainsForUserResponse;
import be.fedict.eid.pkira.generated.privatews.FindRegistrationByIdRequest;
import be.fedict.eid.pkira.generated.privatews.FindRegistrationByIdResponse;
import be.fedict.eid.pkira.generated.privatews.FindRegistrationsByUserRRNRequest;
import be.fedict.eid.pkira.generated.privatews.FindRegistrationsByUserRRNResponse;
import be.fedict.eid.pkira.generated.privatews.FindRemainingCertificateDomainsForUserRequest;
import be.fedict.eid.pkira.generated.privatews.FindRemainingCertificateDomainsForUserResponse;
import be.fedict.eid.pkira.generated.privatews.FindUserRequest;
import be.fedict.eid.pkira.generated.privatews.FindUserResponse;
import be.fedict.eid.pkira.generated.privatews.GetLegalNoticeRequest;
import be.fedict.eid.pkira.generated.privatews.GetLegalNoticeResponse;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesRequest;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesResponse;
import be.fedict.eid.pkira.generated.privatews.ObjectFactory;
import be.fedict.eid.pkira.generated.privatews.RegistrationWS;
import be.fedict.eid.pkira.generated.privatews.UserWS;

/**
 * Client to access the private eID PKI RA web service. This hides the
 * difficulties introduced by the JAX-B API.
 * 
 * @author Jan Van den Bergh
 */
public class EIDPKIRAPrivateServiceClient {

	public static final String NAME = "be.fedict.eid.pkira.wsclient.eidPKIRAPrivateServiceClient";

	private static final String WSDL_LOCATION = "/wsdl/eid-pki-ra-private.wsdl";

	private final ObjectFactory factory;

	private String serviceUrl;
	private EIDPKIRAPrivatePortType port;

	public EIDPKIRAPrivateServiceClient() {
		this.factory = new ObjectFactory();
	}

	public List<CertificateWS> listCertificates(String userRRN, String certificateDomainId) {
		ListCertificatesRequest request = factory.createListCertificatesRequest();
		request.setUserRRN(userRRN);
		request.setCertificateDomainId(certificateDomainId);
		ListCertificatesResponse response = getWebservicePort().listCertificates(request);

		return response.getCertificates();
	}

	public CertificateWS findCertificate(Integer certificateId) {
		FindCertificateRequest request = factory.createFindCertificateRequest();
		request.setCertificateId(certificateId);
		FindCertificateResponse response = getWebservicePort().findCertificate(request);
		return response.getCertificate();
	}
	
	public UserWS findUser(String userRRN) {
		FindUserRequest request = factory.createFindUserRequest();
		request.setUserRRN(userRRN);
		FindUserResponse response = getWebservicePort().findUser(request);
		return response.getUser();
	}
	
	public List<CertificateDomainWS> findRemainingCertificateDomainsForUser(String userRRN) {
		FindRemainingCertificateDomainsForUserRequest request = factory.createFindRemainingCertificateDomainsForUserRequest();
		request.setUserRRN(userRRN);
		
		FindRemainingCertificateDomainsForUserResponse response = getWebservicePort().findRemainingCertificateDomainsForUser(request);
		return response.getCertificateDomains();
	}
	
	public List<CertificateDomainWS> findRegisteredCertificateDomainsForUser(String userRRN){
		FindRegisteredCertificateDomainsForUserRequest request = factory.createFindRegisteredCertificateDomainsForUserRequest();
		request.setUserRRN(userRRN);
		
		FindRegisteredCertificateDomainsForUserResponse response = getWebservicePort().findRegisteredCertificateDomainsForUser(request);
		return response.getCertificateDomains();
	}
	
	public boolean createRegistrationForUser(String userRRN, String userLastName, String userFirstName, String domainId, String userEmail) {
		CreateRegistrationForUserRequest request = factory.createCreateRegistrationForUserRequest();
		request.setCertificateDomainId(domainId);
		request.setUserRRN(userRRN);
		request.setUserLastName(userLastName);
		request.setUserFirstName(userFirstName);
		request.setUserEmail(userEmail);
		
		CreateRegistrationForUserResponse response = getWebservicePort().createRegistrationForUser(request);
		return response.isSuccess();
	}
	
	public List<RegistrationWS> findRegistrationsByUserRRN(String userRRN) {
		FindRegistrationsByUserRRNRequest request = factory.createFindRegistrationsByUserRRNRequest();
		request.setUserRRN(userRRN);
		FindRegistrationsByUserRRNResponse response = getWebservicePort().findRegistrationsByUserRRN(request);
		return response.getRegistration();
	}

	public RegistrationWS findRegistrationById(String id) {
		FindRegistrationByIdRequest request = factory.createFindRegistrationByIdRequest();
		request.setRegistrationId(id);
		FindRegistrationByIdResponse response = getWebservicePort().findRegistrationById(request);
		return response.getRegistration();
	}
	
	public boolean createOrUpdateRegistration(RegistrationWS registrationWS) {
		CreateOrUpdateRegistrationRequest request = factory.createCreateOrUpdateRegistrationRequest();
		request.setRegistration(registrationWS);
		CreateOrUpdateRegistrationResponse response = getWebservicePort().createOrUpdateRegistration(request);
		return response.isSuccess();
	}
	
	public ConfigurationEntryWS findConfigurationEntry(ConfigurationEntryKeyWS configurationEntryKey) {
		FindConfigurationEntryRequest request = factory.createFindConfigurationEntryRequest();
		request.setEntryKey(configurationEntryKey);
		
		FindConfigurationEntryResponse response = getWebservicePort().findConfigurationEntry(request);
		return response.getConfigurationEntry();
	}
	
	public List<ContractWS> findContracts(Integer certificateDomainId, String userRrn) {
		FindContractsRequest request = factory.createFindContractsRequest();
		request.setCertificateDomainId(certificateDomainId);
		request.setUserRrn(userRrn);
		FindContractsResponse response = getWebservicePort().findContracts(request);
		return response.getContracts();
	}
	
	public String getLegalNoticeByDN(String certificateDN, List<String> alternativeNames, CertificateTypeWS certificateType, String userRRN) {
		GetLegalNoticeRequest request = factory.createGetLegalNoticeRequest();
		request.setByDN(factory.createGetLegalNoticeRequestByDN());
		request.getByDN().getAlternativeName().addAll(alternativeNames);
		request.getByDN().setCertificateDN(certificateDN);
		request.getByDN().setCertificateType(certificateType);
		request.getByDN().setUserRRN(userRRN);
		
		GetLegalNoticeResponse response = getWebservicePort().getLegalNotice(request);
		return response.getLegalNotice();
	}
	
	public String getLegalNoticeForCertificate(String issuer, String serialNumber) {
		GetLegalNoticeRequest request = factory.createGetLegalNoticeRequest();
		request.setByCertificate(factory.createGetLegalNoticeRequestByCertificate());
		
		request.getByCertificate().setIssuer(issuer);
		request.getByCertificate().setSerialNumber(serialNumber);
		
		GetLegalNoticeResponse response = getWebservicePort().getLegalNotice(request);
		return response.getLegalNotice();
	}
	
	public CertificateDomainWS findCertificateDomain(Integer certificateDomainId) {
		FindCertificateDomainRequest request = factory.createFindCertificateDomainRequest();
		request.setCertificateDomainId(certificateDomainId);
		FindCertificateDomainResponse response = getWebservicePort().findCertificateDomain(request);
		return response.getCertificateDomain();
	}
	
	public String findContractDocument(Integer contractId) {
		FindContractDocumentRequest request = factory.createFindContractDocumentRequest();
		request.setContractId(contractId);
		FindContractDocumentResponse response = getWebservicePort().findContractDocument(request);
		return response.getContractDocument();
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
