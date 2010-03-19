package be.fedict.eid.pkira.ws.impl;

import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.jboss.seam.Component;

import be.fedict.eid.pkira.blm.model.domain.Certificate;
import be.fedict.eid.pkira.blm.model.domain.CertificateType;
import be.fedict.eid.pkira.blm.model.domain.DomainRepository;
import be.fedict.eid.pkira.blm.model.domain.User;
import be.fedict.eid.pkira.blm.model.jpa.UserRepository;
import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType;
import be.fedict.eid.pkira.generated.privatews.FindCertificateRequest;
import be.fedict.eid.pkira.generated.privatews.FindCertificateResponse;
import be.fedict.eid.pkira.generated.privatews.FindUserRequest;
import be.fedict.eid.pkira.generated.privatews.FindUserResponse;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesRequest;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesResponse;
import be.fedict.eid.pkira.generated.privatews.ObjectFactory;
import be.fedict.eid.pkira.generated.privatews.UserWS;

@Stateless
@WebService(endpointInterface = "be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType")
@HandlerChain(file="/handlerChain.xml")
public class EIDPKIRAPrivateServiceImpl implements EIDPKIRAPrivatePortType {

	public static final String NAME = "eidPKIRAPrivateService";	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListCertificatesResponse listCertificates(ListCertificatesRequest request) {
		ListCertificatesResponse certificatesResponse = new ListCertificatesResponse();
		List<Certificate> allCertificates = getDomainRepository().findAllCertificates(request.getUserRRN());

		for (Certificate certificate : allCertificates) {
			CertificateWS parseCertificateToCertificateWS = parseCertificateToCertificateWS(certificate, false);
			certificatesResponse.getCertificates().add(parseCertificateToCertificateWS);
		}
		return certificatesResponse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FindCertificateResponse findCertificate(FindCertificateRequest request) {
		Certificate certificate = getDomainRepository().findCertificate(request.getUserRRN(), new BigInteger(request.getSerialNumber()));
		FindCertificateResponse response = new FindCertificateResponse();
		response.setCertificate(parseCertificateToCertificateWS(certificate, true));
		return response;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FindUserResponse findUser(FindUserRequest request) {
		User user = getUserRepository().findByNationalRegisterNumber(request.getUserRRN());
		
		FindUserResponse response = new FindUserResponse();
		response.setUser(mapUser(user));
		return response;
	}
	
	private UserWS mapUser(User user) {
		if (user==null) {
			return null;
		}
		
		UserWS result = new ObjectFactory().createUserWS();
		result.setId(user.getId().toString());
		result.setFirstName(user.getFirstName());
		result.setLastName(user.getLastName());
		result.setNationalRegisterNumber(user.getNationalRegisterNumber());
		
		return result;
	}

	private CertificateTypeWS mapCertificateType(CertificateType certificateType) {
		switch (certificateType) {
		case CLIENT:
			return CertificateTypeWS.CLIENT;
		case CODE:
			return CertificateTypeWS.CODE;
		case SERVER:
			return CertificateTypeWS.SERVER;
		}
		throw new RuntimeException("Unknown certificate type:" + certificateType);
	}

	/**
	 * Parse a certificate from the model to a certificate for the webservice
	 * 
	 * @param certificate
	 *            the certificate
	 * @return certificatews
	 */
	private CertificateWS parseCertificateToCertificateWS(Certificate certificate, boolean includeX509) {
		CertificateWS certificateWS = new ObjectFactory().createCertificateWS();
		try {
			certificateWS.setIssuer(certificate.getIssuer());
			certificateWS.setRequesterName(certificate.getRequesterName());
			certificateWS.setDistinguishedName(certificate.getDistinguishedName());

			DatatypeFactory df = DatatypeFactory.newInstance();

			GregorianCalendar calendarEnd = new GregorianCalendar();
			calendarEnd.setTime(certificate.getValidityEnd());

			GregorianCalendar calendarStart = new GregorianCalendar();
			calendarStart.setTime(certificate.getValidityStart());

			certificateWS.setValidityEnd(df.newXMLGregorianCalendar(calendarEnd));
			certificateWS.setValidityStart(df.newXMLGregorianCalendar(calendarStart));
			certificateWS.setCertificateType(mapCertificateType(certificate.getCertificateType()));
			certificateWS.setSerialNumber(certificate.getSerialNumber().toString());
			certificateWS.setDistinguishedName(certificate.getDistinguishedName());
			
			if (includeX509) {
				certificateWS.setX509(certificate.getX509());
			}
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
		return certificateWS;
	}
	
	private DomainRepository getDomainRepository() {
		return (DomainRepository) Component.getInstance(DomainRepository.NAME, true);
	}
	
	private UserRepository getUserRepository() {
		return (UserRepository) Component.getInstance(UserRepository.NAME, true);
	}
}
