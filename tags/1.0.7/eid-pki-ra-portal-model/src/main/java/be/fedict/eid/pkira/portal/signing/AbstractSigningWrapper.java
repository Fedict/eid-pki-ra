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

package be.fedict.eid.pkira.portal.signing;

import java.io.Serializable;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.portal.contract.AbstractContract;
import be.fedict.eid.pkira.portal.util.FacesUtil;

/**
 * @author Bram Baeyens
 *
 */
public abstract class AbstractSigningWrapper<T extends AbstractContract> implements Serializable {
	
	private static final long serialVersionUID = -619211055116177667L;

	private FacesUtil facesUtil;
	
	private String base64CsrXml;
	private String decodedSignatureResponse;
	
	@In(create=true, value=FacesUtil.NAME)
	protected void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}	
	
	@Logger
	private Log log;

	public abstract void setContract(T contract);

	public abstract T getContract();

	public String getDecodedSignatureResponse() {
		return decodedSignatureResponse;
	}

	public void setDecodedSignatureResponse(String decodedSignatureResponse) {
		this.decodedSignatureResponse = decodedSignatureResponse;
	}

	public String getBase64CsrXml() {
		log.info("Sending contract to DSS:\n{0}" , base64CsrXml);
		return base64CsrXml;
	}

	public void setBase64CsrXml(String base64CsrXml) {
		this.base64CsrXml = base64CsrXml;
	}
	
	public String getDssSigningHandlerViewID() { 
		return facesUtil.getContextUrl().concat(getViewId() + "?cid=" + Conversation.instance().getId());
	}
	
	protected abstract String getViewId();
	
	@Override
	public String toString() {
		return new StringBuilder("AbstractSigningWrapper[")
				.append("contract").append(getContract())
				.append(']').toString();
	}
}
