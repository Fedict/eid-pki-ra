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

package be.fedict.eid.pkira.portal.certificaterequest;

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
import be.fedict.eid.pkira.portal.framework.Operator;

/**
 * @author Bram Baeyens
 */
@Name(CSRUploadHandler.NAME)
@Scope(ScopeType.EVENT)
public class CSRUploadHandlerBean implements CSRUploadHandler, Serializable {

	private static final long serialVersionUID = -8223326678483303162L;

	@In(create = true, value = CSRUpload.NAME)
	private CSRUpload csrUpload;

	@In(value = Operator.NAME, scope = ScopeType.SESSION)
	private Operator currentOperator;

	@In(create = true)
	private FacesMessages facesMessages;

	@Logger
	private Log log;

	@In(create = true, value = RequestContract.NAME)
	@Out(value = RequestContract.NAME)
	private RequestContract requestContract;

	@Override
	@Begin
	public String uploadCertificateSigningRequest() {
		log.debug(">>> uploadCertificateSigningRequest(csrUpload=[{0}])", csrUpload);
		requestContract.setOperator(currentOperator);
		try {
			requestContract.setDistinguishedName(csrUpload.extractCsrInfo().getSubject());
			requestContract.setBase64Csr(csrUpload.getBase64Csr());
		} catch (CryptoException e) {
			log.info("<<< uploadCertificateSigningRequest: invalid CSR", e);
			facesMessages.addFromResourceBundle("validator.invalid.csr");
			return null;
		}
		log.debug("<<< uploadCertificateSigningRequest: {0}", requestContract);
		return "success";
	}

	protected void setCsrUpload(CSRUpload csrUpload) {
		this.csrUpload = csrUpload;
	}

	protected void setCurrentoperator(Operator currentOperator) {
		this.currentOperator = currentOperator;
	}

	protected void setFacesMessages(FacesMessages facesMessages) {
		this.facesMessages = facesMessages;
	}

	protected void setLog(Log log) {
		this.log = log;
	}

	protected void setRequestContract(RequestContract requestContract) {
		this.requestContract = requestContract;
	}
}
