package be.fedict.eid.pkira.blm.model.ca;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.framework.EntityHome;

@Name(CertificateChainHome.NAME)
@Scope(ScopeType.CONVERSATION)
public class CertificateChainHome extends EntityHome<CertificateChain> {

	private static final long serialVersionUID = 7811373542507965638L;

	public static final String NAME = "be.fedict.eid.pkira.blm.certificateChainHome";

	@Override
	public ValueExpression<?> getCreatedMessage() {
		return Expressions.instance().createValueExpression("");
	}

	@Override
	public ValueExpression<?> getDeletedMessage() {
		return Expressions.instance().createValueExpression("");
	}

	@Override
	public ValueExpression<?> getUpdatedMessage() {
		return Expressions.instance().createValueExpression("");
	}
}
