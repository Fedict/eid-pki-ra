package be.fedict.eid.pkira.portal.certificate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import be.fedict.eid.pkira.generated.privatews.Certificatews;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

@Stateful
@Scope(ScopeType.SESSION)
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
		EIDPKIRAPrivateServiceClient eidpkiraPrivateServiceClient = new EIDPKIRAPrivateServiceClient();

		List<Certificatews> listCertificates = eidpkiraPrivateServiceClient
				.listCertificates("");
		List<Certificate> certificates = new ArrayList<Certificate>();
		for (Certificatews certificatews : listCertificates) {
			Certificate certificate = new Certificate(certificatews);
			certificates.add(certificate);

		}
		return certificates;
	}

	@Factory("certificatesList")
	public void certificateList() {
		certificatesList = findCertificateList();
	}

	@Remove
	@Destroy
	public void destroy() {
	}
}
