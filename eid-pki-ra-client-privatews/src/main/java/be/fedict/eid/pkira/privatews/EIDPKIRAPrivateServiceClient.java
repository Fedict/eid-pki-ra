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
package be.fedict.eid.pkira.privatews;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import be.fedict.eid.pkira.generated.privatews.*;

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

	public List<CertificateWS> findCertificates(String userRrn, CertificatesFilterWS certificatesFilter, OrderingWS ordering, PagingWS paging) {
        FindCertificatesRequest request = factory.createFindCertificatesRequest();
        request.setUserRrn(userRrn);
        request.setCertificatesFilter(certificatesFilter);
        request.setOrdering(ordering);
        request.setPaging(paging);

        FindCertificatesResponse response = getWebservicePort().findCertificates(request);
        return response.getCertificates();
    }

    public int countCertificates(String userRrn, CertificatesFilterWS certificatesFilter) {
        CountCertificatesRequest request = factory.createCountCertificatesRequest();
        request.setUserRrn(userRrn);
        request.setCertificatesFilter(certificatesFilter);

        CountCertificatesResponse response = getWebservicePort().countCertificates(request);
        return response.getSize();
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
	
	public boolean createRegistrationForUser(String userRRN, String userLastName, String userFirstName, String domainId, String userEmail, String locale) {
		CreateRegistrationForUserRequest request = factory.createCreateRegistrationForUserRequest();
		request.setCertificateDomainId(domainId);
		request.setUserRRN(userRRN);
		request.setUserLastName(userLastName);
		request.setUserFirstName(userFirstName);
		request.setUserEmail(userEmail);
		request.setLocale(locale);
		
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
	
	public List<ContractWS> findContracts(String userRrn, ContractsFilterWS contractsFilter, OrderingWS ordering, PagingWS paging) {
		FindContractsRequest request = factory.createFindContractsRequest();
		request.setUserRrn(userRrn);
		request.setContractsFilter(contractsFilter);
        request.setOrdering(ordering);
        request.setPaging(paging);

        FindContractsResponse response = getWebservicePort().findContracts(request);
        return response.getContracts();
	}

    public ContractWS findContract(int contractId) {
        FindContractRequest request = factory.createFindContractRequest();
        request.setContractId(contractId);
        return getWebservicePort().findContract(request).getContract();
    }

    public int countContracts(String userRrn, ContractsFilterWS contractsFilter) {
        CountContractsRequest request = factory.createCountContractsRequest();
        request.setUserRrn(userRrn);
        request.setContractsFilter(contractsFilter);

        CountContractsResponse response = getWebservicePort().countContracts(request);
        return response.getSize();
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
	
	public void changeLocale(String userRRN, String locale) {
		ChangeLocaleRequest request = factory.createChangeLocaleRequest();
		request.setUserRrn(userRRN);
		request.setLocale(locale);
		getWebservicePort().changeLocale(request);
	}
	
	public List<CertificateTypeWS> getAllowedCertificateTypes(String userRRN, String dn, Collection<String> alternativeNames) {
		GetAllowedCertificateTypesRequest request = factory.createGetAllowedCertificateTypesRequest();
		request.setUserRRN(userRRN);
		request.setCertificateDN(dn);
		request.getAlternativeName().addAll(alternativeNames);
		
		GetAllowedCertificateTypesResponse response = getWebservicePort().getAllowedCertificateTypes(request);
		
		return response.getCertificateType();
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
