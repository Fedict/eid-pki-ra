package be.fedict.eid.pkira.ws.impl;

import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.jboss.seam.Component;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainManager;
import be.fedict.eid.pkira.blm.model.certificatedomain.DistinguishedNameOverlapsException;
import be.fedict.eid.pkira.blm.model.certificatedomain.DuplicateCertificateDomainNameException;
import be.fedict.eid.pkira.blm.model.domain.Certificate;
import be.fedict.eid.pkira.blm.model.domain.CertificateType;
import be.fedict.eid.pkira.blm.model.domain.DomainRepository;
import be.fedict.eid.pkira.dnfilter.InvalidDistinguishedNameException;
import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.generated.privatews.CreateCertificateDomainRequest;
import be.fedict.eid.pkira.generated.privatews.CreateCertificateDomainResponse;
import be.fedict.eid.pkira.generated.privatews.CreateCertificateDomainResult;
import be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType;
import be.fedict.eid.pkira.generated.privatews.FindCertificateRequest;
import be.fedict.eid.pkira.generated.privatews.FindCertificateResponse;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesRequest;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesResponse;

@Stateless
@WebService(endpointInterface = "be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType")
@HandlerChain(file="/handlerChain.xml")
public class EIDPKIRAPrivateServiceImpl implements EIDPKIRAPrivatePortType {

	public static final String NAME = "eidPKIRAPrivateService";	

	@Override
	public CreateCertificateDomainResponse createCertificateDomain(CreateCertificateDomainRequest request) {
		CreateCertificateDomainResponse response = new CreateCertificateDomainResponse();
		try {
			CertificateDomain certificateDomain = getCertificateDomainManager().registerCertificateDomain(request.getName(), request.getCaId(), request
					.getDnExpression(), map(request.getCertificateTypes()));
			
			response.setResult(CreateCertificateDomainResult.SUCCESS);
			response.setDomainId(Integer.toString(certificateDomain.getId()));
		} catch (InvalidDistinguishedNameException e) {
			response.setResult(CreateCertificateDomainResult.INVALID_DN);
		} catch (DistinguishedNameOverlapsException e) {
			response.setResult(CreateCertificateDomainResult.INVALID_DN);
		} catch (DuplicateCertificateDomainNameException e) {
			response.setResult(CreateCertificateDomainResult.DUPLICATE_NAME);
		}

		return response;
	}

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

	@Override
	public FindCertificateResponse findCertificate(FindCertificateRequest request) {
		Certificate certificate = getDomainRepository().findCertificate(request.getUserRRN(), new BigInteger(request.getSerialNumber()));
		FindCertificateResponse response = new FindCertificateResponse();
		response.setCertificate(parseCertificateToCertificateWS(certificate, true));
		return response;
	}
	
	private CertificateType map(CertificateTypeWS certificateType) {
		switch (certificateType) {
		case CLIENT:
			return CertificateType.CLIENT;
		case CODE:
			return CertificateType.CODE;
		case SERVER:
			return CertificateType.SERVER;
		}
		throw new RuntimeException("Unknown certificate type:" + certificateType);
	}

	private Set<CertificateType> map(List<CertificateTypeWS> certificateTypes) {
		Set<CertificateType> result = new HashSet<CertificateType>();
		for (CertificateTypeWS type : certificateTypes) {
			result.add(map(type));
		}
		return result;
	}

	/**
	 * @param certificateType
	 * @return
	 */
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
		CertificateWS certificateWS = new CertificateWS();
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
			// TODO: Error handling
		}
		return certificateWS;
	}
	
	private DomainRepository getDomainRepository() {
		return (DomainRepository) Component.getInstance(DomainRepository.NAME, true);
	}
	
	private CertificateDomainManager getCertificateDomainManager() {
		return (CertificateDomainManager) Component.getInstance(CertificateDomainManager.NAME, true);
	}
}
