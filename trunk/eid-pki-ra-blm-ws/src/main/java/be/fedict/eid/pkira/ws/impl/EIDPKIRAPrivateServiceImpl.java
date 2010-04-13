package be.fedict.eid.pkira.ws.impl;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainHome;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntry;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryQuery;
import be.fedict.eid.pkira.blm.model.contracts.Certificate;
import be.fedict.eid.pkira.blm.model.contracts.ContractRepository;
import be.fedict.eid.pkira.blm.model.mappers.CertificateDomainMapper;
import be.fedict.eid.pkira.blm.model.mappers.CertificateMapper;
import be.fedict.eid.pkira.blm.model.mappers.ConfigurationEntryMapper;
import be.fedict.eid.pkira.blm.model.mappers.UserMapper;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationException;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationManager;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationRepository;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationStatus;
import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.blm.model.usermgmt.UserRepository;
import be.fedict.eid.pkira.generated.privatews.CreateRegistrationForUserRequest;
import be.fedict.eid.pkira.generated.privatews.CreateRegistrationForUserResponse;
import be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType;
import be.fedict.eid.pkira.generated.privatews.FindCertificateRequest;
import be.fedict.eid.pkira.generated.privatews.FindCertificateResponse;
import be.fedict.eid.pkira.generated.privatews.FindConfigurationEntryRequest;
import be.fedict.eid.pkira.generated.privatews.FindConfigurationEntryResponse;
import be.fedict.eid.pkira.generated.privatews.FindRemainingCertificateDomainsForUserRequest;
import be.fedict.eid.pkira.generated.privatews.FindRemainingCertificateDomainsForUserResponse;
import be.fedict.eid.pkira.generated.privatews.FindUserRequest;
import be.fedict.eid.pkira.generated.privatews.FindUserResponse;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesRequest;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesResponse;
import be.fedict.eid.pkira.generated.privatews.ObjectFactory;

@Stateless
@WebService(endpointInterface = "be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType")
@HandlerChain(file = "/handlerChain.xml")
@Name(EIDPKIRAPrivateServiceImpl.NAME)
public class EIDPKIRAPrivateServiceImpl implements EIDPKIRAPrivatePortType {

	public static final String NAME = "be.fedict.eid.pkira.wsclient.eidPKIRAPrivateService";

	private Log log = Logging.getLog(EIDPKIRAPrivateServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListCertificatesResponse listCertificates(ListCertificatesRequest request) {
		List<Certificate> allCertificates = getDomainRepository().findAllCertificates(request.getUserRRN());

		ListCertificatesResponse certificatesResponse = new ObjectFactory().createListCertificatesResponse();
		for (Certificate certificate : allCertificates) {
			certificatesResponse.getCertificates().add(getCertificateMapper().map(certificate, false));
		}
		return certificatesResponse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FindCertificateResponse findCertificate(FindCertificateRequest request) {
		Certificate certificate = getDomainRepository().findCertificate(request.getUserRRN(),
				new BigInteger(request.getSerialNumber()));
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
		int numberOfApprovedRegistrations = getRegistrationRepository().getNumberOfRegistrationsForForUserInStatus(user, RegistrationStatus.APPROVED);

		FindUserResponse response = new ObjectFactory().createFindUserResponse();
		response.setUser(getUserMapper().map(user, numberOfApprovedRegistrations>0));
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CreateRegistrationForUserResponse createRegistrationForUser(CreateRegistrationForUserRequest request) {
		boolean result;
		try {
			getRegistrationManager().registerUser(request.getUserRRN(), request.getUserLastName(),
					request.getUserFirstName(), Integer.parseInt(request.getCertificateDomainId()),
					request.getUserEmail());
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

	private ContractRepository getDomainRepository() {
		return (ContractRepository) Component.getInstance(ContractRepository.NAME, true);
	}

	private UserRepository getUserRepository() {
		return (UserRepository) Component.getInstance(UserRepository.NAME, true);
	}

	private CertificateDomainHome getCertificateDomainHome() {
		return (CertificateDomainHome) Component.getInstance(CertificateDomainHome.NAME, true);
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

}
