package be.fedict.eid.pkira.ws.impl;

import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import be.fedict.eid.pkira.blm.model.domain.Certificate;
import be.fedict.eid.pkira.blm.model.domain.DomainRepository;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType;
import be.fedict.eid.pkira.generated.privatews.FindCertificateRequest;
import be.fedict.eid.pkira.generated.privatews.FindCertificateResponse;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesRequest;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesResponse;

@WebService(endpointInterface = "be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType")
public class EIDPKIRAPrivateServiceImpl implements EIDPKIRAPrivatePortType {

	@EJB
	DomainRepository domainRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListCertificatesResponse listCertificates(ListCertificatesRequest request) {

		ListCertificatesResponse certificatesResponse = new ListCertificatesResponse();
		List<Certificate> allCertificates = domainRepository.findAllCertificates(request.getUserRRN());

		for (Certificate certificate : allCertificates) {
			CertificateWS parseCertificateToCertificateWS = parseCertificateToCertificateWS(certificate, false);
			certificatesResponse.getCertificates().add(parseCertificateToCertificateWS);
		}
		return certificatesResponse;
	}

	@Override
	public FindCertificateResponse findCertificate(FindCertificateRequest request) {
		Certificate certificate = domainRepository.findCertificate(request.getUserRRN(), new BigInteger(request.getSerialNumber()));
		FindCertificateResponse response = new FindCertificateResponse();
		response.setCertificate(parseCertificateToCertificateWS(certificate, true));
		return response;
	}

	/**
	 * Parse a certificate from the model to a certificate for the webservice
	 * 
	 * @param certificate the certificate
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
			certificateWS.setCertificateType(certificate.getCertificateType().name());
			certificateWS.setSerialNumber(certificate.getSerialNumber().toString());
			certificateWS.setDistinguishedName(certificate.getDistinguishedName());
			
			if (includeX509) {
				certificateWS.setX509(certificate.getX509());
			}

		} catch (DatatypeConfigurationException e) {
			//TODO: Error handling
		}
		return certificateWS;
	}
}
