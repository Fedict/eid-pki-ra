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

import org.jboss.seam.annotations.In;

import be.fedict.eid.pkira.portal.util.FacesUtil;

/**
 * @author Bram Baeyens
 *
 */
public abstract class SignableCertificate extends Certificate {

	private static final long serialVersionUID = 3784599851308966681L;

	private FacesUtil facesUtil;
	
	private String base64CsrXml;
	private String decodedSignatureResponse;
	
	@In(create=true)
	protected void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}	

	public String getDecodedSignatureResponse() {
		return decodedSignatureResponse;
	}

	public void setDecodedSignatureResponse(String decodedSignatureResponse) {
		this.decodedSignatureResponse = decodedSignatureResponse;
	}

	public String getBase64CsrXml() {
		return base64CsrXml;
	}

	public void setBase64CsrXml(String base64CsrXml) {
		this.base64CsrXml = base64CsrXml;
	}
	
	public String getDssRequestHandlerUrl() {
		return facesUtil.getContextUrl().concat(getDssSignatureHttpRequestHandlerPath());
	}
	
	protected abstract String getDssSignatureHttpRequestHandlerPath();
}
