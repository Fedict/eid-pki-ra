/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */

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
