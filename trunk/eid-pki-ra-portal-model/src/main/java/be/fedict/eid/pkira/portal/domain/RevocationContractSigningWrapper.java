/**
 * eID PKI RA Project. 
 * Copyright (C) 2010 FedICT. 
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

package be.fedict.eid.pkira.portal.domain;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * @author Bram Baeyens
 *
 */
@Name("revocationContractSigningWrapper")
@Scope(ScopeType.CONVERSATION)
public class RevocationContractSigningWrapper extends AbstractDSSSigningWrapper<RevocationContract> {

	private static final long serialVersionUID = 7810123499527839991L;

	private RevocationContract contract;

	@Override
	public RevocationContract getContract() {
		return contract;
	}

	@Override
	@In(value="revocationContract")
	public void setContract(RevocationContract contract) {
		this.contract = contract;
	}
	
	@Override
	protected String getDssSignatureHttpRequestHandlerPath() {
		return "/seam/resource/revocationContractHandler";
	}

}
