package be.fedict.eid.pkira.blm.model.ca;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.framework.EntityHome;

@Name(CertificateChainCertificateHome.NAME)
@Scope(ScopeType.EVENT)
public class CertificateChainCertificateHome extends EntityHome<CertificateChainCertificate> {

	private static final long serialVersionUID = -7689636241849328965L;
	public static final String NAME = "be.fedict.eid.pkira.blm.certificateChainCertificateHome";

	@Override
	protected String getUpdatedMessageKey() {
		return null;
	}

	@Override
	protected String getCreatedMessageKey() {
		return null;
	}

	@Override
	protected String getDeletedMessageKey() {
		return null;
	}
	
	@Override
	public ValueExpression<?> getCreatedMessage() {
		return Expressions.instance().createValueExpression(null);
	}

	@Override
	public ValueExpression<?> getDeletedMessage() {
		return Expressions.instance().createValueExpression(null);
	}

	@Override
	public ValueExpression<?> getUpdatedMessage() {
		return Expressions.instance().createValueExpression(null);
	}
}
