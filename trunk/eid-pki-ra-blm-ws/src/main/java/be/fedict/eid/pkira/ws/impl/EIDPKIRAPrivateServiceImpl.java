package be.fedict.eid.pkira.ws.impl;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;

import be.fedict.eid.pkira.blm.model.domain.Certificate;
import be.fedict.eid.pkira.blm.model.domain.DomainRepository;
import be.fedict.eid.pkira.blm.model.domain.User;
import be.fedict.eid.pkira.blm.model.jpa.UserRepository;
import be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType;
import be.fedict.eid.pkira.generated.privatews.FindCertificateRequest;
import be.fedict.eid.pkira.generated.privatews.FindCertificateResponse;
import be.fedict.eid.pkira.generated.privatews.FindUserRequest;
import be.fedict.eid.pkira.generated.privatews.FindUserResponse;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesRequest;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesResponse;
import be.fedict.eid.pkira.ws.impl.mapper.CertificateMapper;
import be.fedict.eid.pkira.ws.impl.mapper.UserMapper;

@Stateless
@WebService(endpointInterface = "be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType")
@HandlerChain(file = "/handlerChain.xml")
public class EIDPKIRAPrivateServiceImpl implements EIDPKIRAPrivatePortType {

	public static final String NAME = "eidPKIRAPrivateService";

	@In(value = CertificateMapper.NAME, create = true)
	private CertificateMapper certificateMapper;
	
	@In(value = UserMapper.NAME, create = true)
	private UserMapper userMapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListCertificatesResponse listCertificates(ListCertificatesRequest request) {		
		List<Certificate> allCertificates = getDomainRepository().findAllCertificates(request.getUserRRN());

		ListCertificatesResponse certificatesResponse = new ListCertificatesResponse();
		for (Certificate certificate : allCertificates) {
			certificatesResponse.getCertificates().add(certificateMapper.map(certificate, false));
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
		response.setCertificate(certificateMapper.map(certificate, true));
		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FindUserResponse findUser(FindUserRequest request) {
		User user = getUserRepository().findByNationalRegisterNumber(request.getUserRRN());

		FindUserResponse response = new FindUserResponse();
		response.setUser(userMapper.map(user));
		return response;
	}

	private DomainRepository getDomainRepository() {
		return (DomainRepository) Component.getInstance(DomainRepository.NAME, true);
	}

	private UserRepository getUserRepository() {
		return (UserRepository) Component.getInstance(UserRepository.NAME, true);
	}
}
