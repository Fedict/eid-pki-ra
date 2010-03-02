package be.fedict.eid.blm.model.certificatehandler;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;

import be.fedict.eid.blm.model.jpa.Certificate;

@AutoCreate
@Stateless
public class CertificateHandlerBean implements CertificateHandler{

    @In private EntityManager entityManager;
	
	@Override
	public List<Certificate> findAllCertificates() {
		Query query = entityManager.createNamedQuery("SELECT distinct c from CERTIFICATE c");
		return query.getResultList();
	}

	@Override
	public List<Certificate> findCertificate() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void createCertificate(String x509, String subject, Date validityStart,
			Date validityEnd, String requestorName, String issuer) {
		Certificate certificate = new Certificate(x509, subject, validityStart, validityEnd, requestorName, issuer);
		
		entityManager.persist(certificate);
	}

	@Override
	public void deleteCertificate() {
		// TODO Auto-generated method stub
		
	}


}
