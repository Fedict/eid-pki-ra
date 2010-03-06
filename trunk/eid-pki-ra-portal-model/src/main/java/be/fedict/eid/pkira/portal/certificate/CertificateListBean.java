package be.fedict.eid.pkira.portal.certificate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

@Stateful
@Name(CertificateList.NAME)
public class CertificateListBean implements CertificateList, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4024420123671643615L;

	@DataModel
	List<Certificate> certificatesList;

	@DataModelSelection
	@Out(required = false)
	Certificate certificate;

	
	@Override
	public List<Certificate> findCertificateList() {		
		List<Certificate> certificates = new ArrayList<Certificate>();
		if(certificatesList == null){
			EIDPKIRAPrivateServiceClient eidpkiraPrivateServiceClient = new EIDPKIRAPrivateServiceClient( "http://localhost:8080/eid-pki-ra/webservice/EIDPKIRAPrivateService");

			List<CertificateWS> listCertificates = eidpkiraPrivateServiceClient.listCertificates("");
			for (CertificateWS certificatews : listCertificates) {
				Certificate certificate = new Certificate(certificatews);
				certificates.add(certificate);
			}
		}
		return certificates;
	}
	
	@Begin(join=true)
	@Factory("certificatesList")
	public void certificateList() {
		certificatesList = findCertificateList();
	}

	@End
	public void cancel(){}
	
	@Remove
	@Destroy
	public void destroy() {
		certificatesList = null;
	}
}
