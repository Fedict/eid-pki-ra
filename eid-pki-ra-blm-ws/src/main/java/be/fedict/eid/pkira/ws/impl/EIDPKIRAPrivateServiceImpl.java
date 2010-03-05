package be.fedict.eid.pkira.ws.impl;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import be.fedict.eid.pkira.blm.model.domain.Certificate;
import be.fedict.eid.pkira.blm.model.domain.DomainRepository;
import be.fedict.eid.pkira.generated.privatews.Certificatews;
import be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesRequest;
import be.fedict.eid.pkira.generated.privatews.ListCertificatesResponse;

@WebService(endpointInterface = "be.fedict.eid.pkira.generated.privatews.EIDPKIRAPrivatePortType")
public class EIDPKIRAPrivateServiceImpl implements EIDPKIRAPrivatePortType {

	@EJB
	DomainRepository domainRepository;

	@Override
	public ListCertificatesResponse listCertificates(
			ListCertificatesRequest request) {

		ListCertificatesResponse certificatesResponse = new ListCertificatesResponse();
		List<Certificate> allCertificates = domainRepository
				.findAllCertificates(request.getUserRRN());
		for (Certificate certificate : allCertificates) {
			Certificatews certificatews = new Certificatews();
			certificatews.setIssuer(certificate.getIssuer());
			certificatews.setRequesterName(certificate.getRequesterName());
			certificatews.setSubject(certificate.getSubject());

			try {
				DatatypeFactory df = DatatypeFactory.newInstance();

				GregorianCalendar calendarEnd = new GregorianCalendar();
				calendarEnd.setTime(certificate.getValidityEnd());

				GregorianCalendar calendarStart = new GregorianCalendar();
				calendarStart.setTime(certificate.getValidityStart());

				certificatews.setValidityEnd(df
						.newXMLGregorianCalendar(calendarEnd));
				certificatews.setValidityStart(df
						.newXMLGregorianCalendar(calendarStart));
				
				
				certificatesResponse.getCertificates().add(certificatews);
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return certificatesResponse;
	}
}
