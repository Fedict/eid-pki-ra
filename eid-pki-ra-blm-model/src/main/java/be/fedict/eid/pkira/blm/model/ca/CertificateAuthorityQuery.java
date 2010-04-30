package be.fedict.eid.pkira.blm.model.ca;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;


@Name(value=CertificateAuthorityQuery.NAME)
public class CertificateAuthorityQuery extends EntityQuery<CertificateAuthority>  {
	
	private static final long serialVersionUID = -3160482419948863045L;
	
	@Override
	public String getEjbql() {
		return "SELECT certificateAuthority FROM CertificateAuthority certificateAuthority";
	}
	
	public static final String NAME = "be.fedict.eid.pkira.blm.certificateAuthorityQuery";
}
