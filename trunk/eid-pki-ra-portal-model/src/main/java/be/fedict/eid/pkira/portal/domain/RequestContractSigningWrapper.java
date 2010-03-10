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
@Name("requestContractSigningWrapper")
@Scope(ScopeType.CONVERSATION)
public class RequestContractSigningWrapper extends AbstractDSSSigningWrapper<RequestContract> {

	private static final long serialVersionUID = 4814775483024392737L;

	private RequestContract contract;

	@Override
	@In(value="requestContract")
	public void setContract(RequestContract contract) {
		this.contract = contract;		
	}

	@Override
	public RequestContract getContract() {
		return contract;
	}
	
	@Override
	protected String getDssSignatureHttpRequestHandlerPath() {
		return "/seam/resource/requestContractHandler";
	}

}
