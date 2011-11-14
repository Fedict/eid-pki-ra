package be.fedict.eid.pkira.blm.model.ca;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.framework.ValidatingEntityHome;

@Name(CertificateAuthorityParameterHome.NAME)
public class CertificateAuthorityParameterHome extends ValidatingEntityHome<CertificateAuthorityParameter>{
	
	private static final long serialVersionUID = 2378990512594315412L;
	
	public static final String NAME="be.fedict.eid.pkira.blm.certificateAuthorityParameterHome";
}
