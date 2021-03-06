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

package be.fedict.eid.pkira.ws.impl;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainHome;
import be.fedict.eid.pkira.blm.model.certificatedomain.RegisteredCertificateDomainQuery;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntry;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryQuery;
import be.fedict.eid.pkira.blm.model.contracts.AbstractContract;
import be.fedict.eid.pkira.blm.model.contracts.Certificate;
import be.fedict.eid.pkira.blm.model.contracts.CertificateHome;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.contracts.ContractHome;
import be.fedict.eid.pkira.blm.model.contracts.ContractQuery;
import be.fedict.eid.pkira.blm.model.contracts.ContractRepository;
import be.fedict.eid.pkira.blm.model.contracts.Ordering;
import be.fedict.eid.pkira.blm.model.contracts.Paging;
import be.fedict.eid.pkira.blm.model.mappers.CertificateDomainMapper;
import be.fedict.eid.pkira.blm.model.mappers.CertificateMapper;
import be.fedict.eid.pkira.blm.model.mappers.CertificatesFilterMapper;
import be.fedict.eid.pkira.blm.model.mappers.ConfigurationEntryMapper;
import be.fedict.eid.pkira.blm.model.mappers.ContractMapper;
import be.fedict.eid.pkira.blm.model.mappers.ContractsFilterMapper;
import be.fedict.eid.pkira.blm.model.mappers.RegistrationMapper;
import be.fedict.eid.pkira.blm.model.mappers.UserMapper;
import be.fedict.eid.pkira.blm.model.usermgmt.*;
import be.fedict.eid.pkira.generated.privatews.*;
import be.fedict.eid.pkira.generated.privatews.GetLegalNoticeRequest.ByDN;

@Stateless
@WebService(endpointInterface = "be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType")
@HandlerChain(file = "/handlerChain.xml")
@Name(EIDPKIRAPrivateServiceImpl.NAME)
public class EIDPKIRAPrivateServiceImpl implements EIDPKIRAPrivatePortType {

	public static final String NAME = "be.fedict.eid.pkira.wsclient.eidPKIRAPrivateService";

	private Log log = Logging.getLog(EIDPKIRAPrivateServiceImpl.class);

    @Override
    public FindCertificatesResponse findCertificates(FindCertificatesRequest request) {
        List<Certificate> certificates = getContractRepository().findCertificatesByFilter(
                request.getUserRrn(),
                getCertificatesFilterMapper().mapCertificatesFilter(request.getCertificatesFilter()),
                mapOrdering(request.getOrdering()),
                mapPaging(request.getPaging()));

        FindCertificatesResponse response = new ObjectFactory().createFindCertificatesResponse();
        response.getCertificates().addAll(getCertificateMapper().map(certificates, false));
        return response;
    }

    @Override
    public CountCertificatesResponse countCertificates(CountCertificatesRequest request) {
        int size = getContractRepository().countCertificatesByFilter(
                request.getUserRrn(),
                getCertificatesFilterMapper().mapCertificatesFilter(request.getCertificatesFilter()));

        CountCertificatesResponse response = new ObjectFactory().createCountCertificatesResponse();
        response.setSize(size);
        return response;
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	public FindCertificateResponse findCertificate(FindCertificateRequest request) {
		getCertificateHome().setId(request.getCertificateId());
		Certificate certificate = getCertificateHome().getInstance();
		FindCertificateResponse response = new ObjectFactory().createFindCertificateResponse();
		response.setCertificate(getCertificateMapper().map(certificate, true));
		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FindUserResponse findUser(FindUserRequest request) {
		User user = getUserRepository().findByNationalRegisterNumber(request.getUserRRN());
		int numberOfApprovedRegistrations = getRegistrationRepository().getNumberOfRegistrationsForForUserInStatus(user,
				RegistrationStatus.APPROVED);

		FindUserResponse response = new ObjectFactory().createFindUserResponse();
		response.setUser(getUserMapper().map(user, numberOfApprovedRegistrations > 0));
		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FindRemainingCertificateDomainsForUserResponse findRemainingCertificateDomainsForUser(
			FindRemainingCertificateDomainsForUserRequest request) {
		User user = getUserRepository().findByNationalRegisterNumber(request.getUserRRN());
		List<CertificateDomain> domains = getCertificateDomainHome().findUnregistered(user);

		FindRemainingCertificateDomainsForUserResponse response = new ObjectFactory()
				.createFindRemainingCertificateDomainsForUserResponse();
		response.getCertificateDomains().addAll(getCertificateDomainMapper().map(domains));
		return response;
	}

	@Override
	public FindRegisteredCertificateDomainsForUserResponse findRegisteredCertificateDomainsForUser(
			FindRegisteredCertificateDomainsForUserRequest request) {
		User user = getUserRepository().findByNationalRegisterNumber(request.getUserRRN());
		List<CertificateDomain> domains = getRegistratedCertificateDomainQuery().getFindRegisteredCertificateDomains(
				user.getNationalRegisterNumber());

		FindRegisteredCertificateDomainsForUserResponse response = new ObjectFactory()
				.createFindRegisteredCertificateDomainsForUserResponse();
		response.getCertificateDomains().addAll(getCertificateDomainMapper().map(domains));
		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CreateRegistrationForUserResponse createRegistrationForUser(CreateRegistrationForUserRequest request) {
		boolean result;
		try {
			getRegistrationManager().registerUser(request.getUserRRN(), request.getUserLastName(), request.getUserFirstName(),
					request.getCertificateDomainId() != null ? Integer.parseInt(request.getCertificateDomainId()) : null,
					request.getUserEmail(), request.getLocale());
			result = true;
		} catch (RegistrationException e) {
			log.error("Error creating registration", e);
			result = false;
		} catch (NumberFormatException e) {
			log.error("Error creating certificate: invalid domain id: " + request.getCertificateDomainId());
			result = false;
		}

		CreateRegistrationForUserResponse response = new ObjectFactory().createCreateRegistrationForUserResponse();
		response.setSuccess(result);
		return response;
	}

	@Override
	public FindRegistrationsByUserRRNResponse findRegistrationsByUserRRN(FindRegistrationsByUserRRNRequest request) {
		FindRegistrationsByUserRRNResponse response = new ObjectFactory().createFindRegistrationsByUserRRNResponse();
		List<Registration> registrations = getRegistrationQuery().findByUserRRN(request.getUserRRN());
		response.getRegistration().addAll(getRegistrationMapper().map(registrations));
		return response;
	}

	@Override
	public FindRegistrationByIdResponse findRegistrationById(FindRegistrationByIdRequest request) {
		FindRegistrationByIdResponse response = new ObjectFactory().createFindRegistrationByIdResponse();
		RegistrationHome registrationHome = getRegistrationHome();
		registrationHome.setId(Integer.valueOf(request.getRegistrationId()));
		Registration registration = registrationHome.find();
		response.setRegistration(getRegistrationMapper().map(registration));
		return response;
	}

	@Override
	public CreateOrUpdateRegistrationResponse createOrUpdateRegistration(CreateOrUpdateRegistrationRequest request) {
		CreateOrUpdateRegistrationResponse response = new ObjectFactory().createCreateOrUpdateRegistrationResponse();
		response.setSuccess(getRegistrationManager().createOrUpdateRegistration(request.getRegistration()));
		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FindConfigurationEntryResponse findConfigurationEntry(FindConfigurationEntryRequest request) {
		ConfigurationEntry configurationEntry = getConfigurationEntryQuery().findByEntryKey(
				getConfigurationEntryMapper().map(request.getEntryKey()));
		FindConfigurationEntryResponse response = new ObjectFactory().createFindConfigurationEntryResponse();
		response.setConfigurationEntry(getConfigurationEntryMapper().map(configurationEntry));
		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GetLegalNoticeResponse getLegalNotice(GetLegalNoticeRequest request) {
		String legalNotice = null;

		ByDN byDN = request.getByDN();
		if (byDN != null) {
			CertificateType certificateType = getCertificateMapper().map(byDN.getCertificateType());
            List<Registration> registrations = Collections.emptyList();
            try {
                registrations = getRegistrationManager().findRegistrationForUserDNAndCertificateType(byDN.getUserRRN(),
                        byDN.getCertificateDN(), byDN.getAlternativeName(), certificateType);
            } catch (BlacklistedException e) {
                log.error("CN is blacklisted. No registrations found");
            }
            if (registrations.size() != 0) {
				legalNotice = registrations.get(0).getCertificateDomain().getCertificateAuthority().getLegalNotice();
			}
		}

		if (request.getByCertificate() != null) {
			String issuer = request.getByCertificate().getIssuer();
			BigInteger serialNumber = new BigInteger(request.getByCertificate().getSerialNumber());
			Certificate certificate = getContractRepository().findCertificate(issuer, serialNumber);
			if (certificate != null) {
				legalNotice = certificate.getCertificateDomain().getCertificateAuthority().getLegalNotice();
			}
		}

		GetLegalNoticeResponse response = new ObjectFactory().createGetLegalNoticeResponse();

		response.setLegalNotice(legalNotice);
		return response;
	}

	@Override
	public FindContractsResponse findContracts(FindContractsRequest request) {
        List<AbstractContract> contracts = getContractQuery().findContractsByFilter(
                request.getUserRrn(),
                getContractsFilterMapper().mapContractsFilter(request.getContractsFilter()),
                mapOrdering(request.getOrdering()),
                mapPaging(request.getPaging())
        );

        FindContractsResponse response = new ObjectFactory().createFindContractsResponse();
		response.getContracts().addAll(getContractMapper().map(contracts));
		return response;
	}

    @Override
    public CountContractsResponse countContracts(CountContractsRequest request) {
        int size = getContractQuery().countContractsByFilter(
                request.getUserRrn(), 
                getContractsFilterMapper().mapContractsFilter(request.getContractsFilter()));

        CountContractsResponse response = new ObjectFactory().createCountContractsResponse();
        response.setSize(size);
        return response;
    }

    @Override
    public FindContractResponse findContract(FindContractRequest request) {
        AbstractContract contract = getContractQuery().findContractById(request.getContractId());

        FindContractResponse response = new FindContractResponse();
        response.setContract(getContractMapper().map(contract));
        return response;
    }

    @Override
	public FindContractDocumentResponse findContractDocument(FindContractDocumentRequest request) {
		FindContractDocumentResponse response = new ObjectFactory().createFindContractDocumentResponse();
		ContractHome contractHome = getContractHome();
		contractHome.setId(request.getContractId());
		response.setContractDocument(contractHome.getInstance().getContractDocument());
		return response;
	}

	@Override
	public FindCertificateDomainResponse findCertificateDomain(FindCertificateDomainRequest request) {
		FindCertificateDomainResponse response = new ObjectFactory().createFindCertificateDomainResponse();
		CertificateDomainHome certificateDomainHome = getCertificateDomainHome();
		certificateDomainHome.setId(request.getCertificateDomainId());
		response.setCertificateDomain(getCertificateDomainMapper().map(certificateDomainHome.getInstance()));
		return response;
	}

	@Override
	public ChangeLocaleResponse changeLocale(ChangeLocaleRequest request) {
		getRegistrationManager().changeLocale(request.getUserRrn(), request.getLocale());

		return new ChangeLocaleResponse();
	}

	@Override
	public GetAllowedCertificateTypesResponse getAllowedCertificateTypes(GetAllowedCertificateTypesRequest request) {
		String userRRN = request.getUserRRN();

        List<Registration> registrations = Collections.emptyList();
        try {
            registrations = getRegistrationManager().findRegistrationForUserDNAndCertificateType(userRRN, request.getCertificateDN(), request.getAlternativeName(), null);
        } catch (BlacklistedException e) {
            log.warn("CN is blacklisted. No registrations found");
        }
        Set<CertificateType> certificateTypes = new HashSet<CertificateType>();
		for(Registration registration: registrations) {
			certificateTypes.addAll(registration.getCertificateDomain().getCertificateTypes());
		}

		GetAllowedCertificateTypesResponse response = new GetAllowedCertificateTypesResponse();
		getCertificateMapper().map(certificateTypes, response.getCertificateType());
		return response;
	}

    private Ordering mapOrdering(OrderingWS ordering) {
        if (ordering !=null) {
            return new Ordering(
                    mapSortColumn(ordering.getField()),
                    ordering.getOrder()== SortOrderWS.ASC);
        }

        return new Ordering();
    }

    private Paging mapPaging(PagingWS paging) {
        if (paging !=null) {
            return new Paging(paging.getFirstRow(), paging.getEndRow());
        }
        return new Paging();
    }

    private String mapSortColumn(String field) {
        if (field==null) return null;

        // Special cases
        if (field.equals("contractType")) return "class"; // contract type mapped via subclass
        if (field.equals("dnExpression")) return "subject";

        return field;
    }

	private UserRepository getUserRepository() {
		return (UserRepository) Component.getInstance(UserRepository.NAME, true);
	}

	private CertificateDomainHome getCertificateDomainHome() {
		return (CertificateDomainHome) Component.getInstance(CertificateDomainHome.NAME, true);
	}

	private RegisteredCertificateDomainQuery getRegistratedCertificateDomainQuery() {
		return (RegisteredCertificateDomainQuery) Component.getInstance(RegisteredCertificateDomainQuery.NAME, true);
	}

	private RegistrationManager getRegistrationManager() {
		return (RegistrationManager) Component.getInstance(RegistrationManager.NAME, true);
	}

	private RegistrationRepository getRegistrationRepository() {
		return (RegistrationRepository) Component.getInstance(RegistrationRepository.NAME, true);
	}

	private UserMapper getUserMapper() {
		return (UserMapper) Component.getInstance(UserMapper.NAME, true);
	}

	private CertificateMapper getCertificateMapper() {
		return (CertificateMapper) Component.getInstance(CertificateMapper.NAME, true);
	}

	private CertificateDomainMapper getCertificateDomainMapper() {
		return (CertificateDomainMapper) Component.getInstance(CertificateDomainMapper.NAME, true);
	}

	private ConfigurationEntryMapper getConfigurationEntryMapper() {
		return (ConfigurationEntryMapper) Component.getInstance(ConfigurationEntryMapper.NAME, true);
	}

	private ConfigurationEntryQuery getConfigurationEntryQuery() {
		return (ConfigurationEntryQuery) Component.getInstance(ConfigurationEntryQuery.class, true);
	}

	private RegistrationQuery getRegistrationQuery() {
		return (RegistrationQuery) Component.getInstance(RegistrationQuery.NAME, true);
	}

	private RegistrationHome getRegistrationHome() {
		return (RegistrationHome) Component.getInstance(RegistrationHome.NAME, true);
	}

	private RegistrationMapper getRegistrationMapper() {
		return (RegistrationMapper) Component.getInstance(RegistrationMapper.NAME, true);
	}

	private ContractQuery getContractQuery() {
		return (ContractQuery) Component.getInstance(ContractQuery.NAME, true);
	}

	private ContractMapper getContractMapper() {
		return (ContractMapper) Component.getInstance(ContractMapper.NAME, true);
	}

    private ContractsFilterMapper getContractsFilterMapper() {
        return (ContractsFilterMapper) Component.getInstance(ContractsFilterMapper.NAME, true);
    }

    private CertificatesFilterMapper getCertificatesFilterMapper() {
        return (CertificatesFilterMapper) Component.getInstance(CertificatesFilterMapper.NAME, true);
    }

	private ContractHome getContractHome() {
		return (ContractHome) Component.getInstance(ContractHome.NAME, true);
	}

	private CertificateHome getCertificateHome() {
		return (CertificateHome) Component.getInstance(CertificateHome.NAME, true);
	}

	private ContractRepository getContractRepository() {
		return (ContractRepository) Component.getInstance(ContractRepository.NAME, true);
	}
}
