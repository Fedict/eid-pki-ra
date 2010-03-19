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

package be.fedict.eid.pkira.portal.handler;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.crypto.CryptoException;
import be.fedict.eid.pkira.portal.domain.CSRUpload;
import be.fedict.eid.pkira.portal.domain.Operator;
import be.fedict.eid.pkira.portal.domain.RequestContract;

/**
 * @author Bram Baeyens
 *
 */
@Name(CSRUploadHandler.NAME)
@Scope(ScopeType.EVENT)
public class CSRUploadHandlerBean implements CSRUploadHandler, Serializable {
	
	private static final long serialVersionUID = -8223326678483303162L;

	@Logger
	private Log log;
	
	@Out
	private RequestContract requestContract;
	private CSRUpload csrUpload;
	private FacesMessages facesMessages;
	private Operator currentOperator;
	
	protected void setLog(Log log) {
		this.log = log;
	}
	
	@In(create=true, value="requestContract")
	protected void setRequestContract(RequestContract requestContract) {
		this.requestContract = requestContract;
	}
	
	@In(create=true, value="csrUpload")
	protected void setCsrUpload(CSRUpload csrUpload) {
		this.csrUpload = csrUpload;
	}

	@In(create=true)
	public void setFacesMessages(FacesMessages facesMessages) {
		this.facesMessages = facesMessages;
	}

	@In(value="currentOperator", scope=ScopeType.SESSION)
	public void setCurrentoperator(Operator currentOperator) {
		this.currentOperator = currentOperator;
	}

	@Override
	@Begin
	public String uploadCertificateSigningRequest() {
		log.debug(">>> uploadCertificateSigningRequest(csrUpload=[{0}])", csrUpload);
		requestContract.setOperator(currentOperator);
		try {
			requestContract.setDistinguishedName(csrUpload.extractCsrInfo());
			requestContract.setBase64Csr(csrUpload.getBase64Csr());
		} catch (CryptoException e) {
			log.info("<<< uploadCertificateSigningRequest: invalid CSR", e);
			facesMessages.addFromResourceBundle("validator.invalid.csr");
			return null;
		}
		log.debug("<<< uploadCertificateSigningRequest: {0}", requestContract);
		return "success";
	}
}
