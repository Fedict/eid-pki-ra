package be.fedict.eid.pkira.blm.model.ca;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name(CertificateAuthorityParameterHandler.NAME)
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class CertificateAuthorityParameterHandler implements Serializable {

	public static final String NAME = "be.fedict.eid.pkira.blm.certificateAuthorityParameterHandler";

	private static final long serialVersionUID = -	5672530054643982245L;

	@In(value = CertificateAuthorityHome.NAME, create = true)
	private CertificateAuthorityHome certificateAuthorityHome;

	@In(value = CertificateAuthorityParameterHome.NAME, create = true)
	private CertificateAuthorityParameterHome certificateAuthorityParameterHome;
		
	private String newParameterKey;
	private String newParameterValue;
	
	public void deleteParameter(CertificateAuthorityParameter parameter) {
		if (parameter == null) {
			return;
		}			
			certificateAuthorityHome.getInstance().getParameters().remove(parameter);
			
			certificateAuthorityParameterHome.setInstance(parameter);
			certificateAuthorityParameterHome.remove();

			certificateAuthorityHome.update();
	}
	
	public void addParameter(){
		if (newParameterKey == null || newParameterValue == null) {
			return;
		}
		CertificateAuthorityParameter parameter = new CertificateAuthorityParameter();
		parameter.setCa(certificateAuthorityHome.getInstance());
		parameter.setKey(newParameterKey);
		parameter.setValue(newParameterValue);
		certificateAuthorityHome.getInstance().getParameters().add(parameter);
		
		certificateAuthorityHome.update();
		
		newParameterKey = null;
		newParameterValue = null;
	}

	public void setNewParameterKey(String newParameterKey) {
		this.newParameterKey = newParameterKey;
	}

	public String getNewParameterKey() {
		return newParameterKey;
	}

	public void setNewParameterValue(String newParameterValue) {
		this.newParameterValue = newParameterValue;
	}

	public String getNewParameterValue() {
		return newParameterValue;
	}
}
