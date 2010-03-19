package be.fedict.eid.pkira.ws.impl;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.domain.Certificate;
import be.fedict.eid.pkira.blm.model.domain.DomainRepository;
import be.fedict.eid.pkira.blm.model.domain.User;
import be.fedict.eid.pkira.blm.model.jpa.UserRepository;
import be.fedict.eid.pkira.blm.model.mappers.CertificateMapper;
import be.fedict.eid.pkira.blm.model.mappers.UserMapper;
import be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType;
import be.fedict.eid.pkira.generated.privatews.FindCertificateRequest;
import be.fedict.eid.pkira.generated.privatews.FindCertificateResponse;
import be.fedict.eid.pkira.generated.privatews.FindUserRequest;
import be.fedict.eid.pkira.generated.privatews.FindUserResponse;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesRequest;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesResponse;

@Stateless
@WebService(endpointInterface = "be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType")
@HandlerChain(file = "/handlerChain.xml")
@Name(EIDPKIRAPrivateServiceImpl.NAME)
public class EIDPKIRAPrivateServiceImpl implements EIDPKIRAPrivatePortType {

	public static final String NAME = "eidPKIRAPrivateService";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListCertificatesResponse listCertificates(ListCertificatesRequest request) {		
		List<Certificate> allCertificates = getDomainRepository().findAllCertificates(request.getUserRRN());

		ListCertificatesResponse certificatesResponse = new ListCertificatesResponse();
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
		FindCertificateResponse response = new FindCertificateResponse();
		response.setCertificate(getCertificateMapper().map(certificate, true));
		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FindUserResponse findUser(FindUserRequest request) {
		User user = getUserRepository().findByNationalRegisterNumber(request.getUserRRN());

		FindUserResponse response = new FindUserResponse();
		response.setUser(getUserMapper().map(user));
		return response;
	}

	private DomainRepository getDomainRepository() {
		return (DomainRepository) Component.getInstance(DomainRepository.NAME, true);
	}

	private UserRepository getUserRepository() {
		return (UserRepository) Component.getInstance(UserRepository.NAME, true);
	}
	
	private UserMapper getUserMapper() {		
		return (UserMapper) Component.getInstance(UserMapper.NAME, true);
	}
	
	private CertificateMapper getCertificateMapper() {
		return (CertificateMapper) Component.getInstance(CertificateMapper.NAME, true);
	}
}
