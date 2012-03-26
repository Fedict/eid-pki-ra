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
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.crypto.csr.CSRInfo;
import be.fedict.eid.pkira.crypto.exception.CryptoException;
import be.fedict.eid.pkira.dnfilter.DistinguishedName;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameManager;
import be.fedict.eid.pkira.dnfilter.InvalidDistinguishedNameException;
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
	
	@In(create=true, value=DistinguishedNameManager.NAME)
	private DistinguishedNameManager distinguishedNameManager;

	@Logger
	private Log log;

	@In(create = true, value = RequestContract.NAME)
	@Out(value = RequestContract.NAME)
	private RequestContract requestContract;

	@Override
	@Begin(join = true)
	public String uploadCertificateSigningRequest() {
		log.debug(">>> uploadCertificateSigningRequest(csrUpload=[{0}])", csrUpload);
		requestContract.setOperator(currentOperator);
		CSRInfo csrInfo;
		try {
			csrInfo = csrUpload.extractCsrInfo();
		} catch (CryptoException e) {
			log.info("<<< uploadCertificateSigningRequest: invalid CSR", e);
			addInvalidCSRMessage();
			return null;
		}

		if (!validateCsrAndPopulateContract(csrInfo)) {
			addInvalidCSRMessage();
			return null;
		}

		log.debug("<<< uploadCertificateSigningRequest: {0}", requestContract);
		return "success";
	}

	/**
	 * Validates the CSR.
	 */
	private boolean validateCsrAndPopulateContract(CSRInfo csrInfo) {
		// Extract the SANs
		List<String> subjectAlternativeNames;
		try {
			subjectAlternativeNames = csrInfo.getSubjectAlternativeNames();
		} catch (CryptoException e) {
			// Invalid SAN detected
			log.warn("Invalid CSR detected", e);
			return false;
		}
		
		// Parse the DN
		DistinguishedName distinguishedName;
		try {
			distinguishedName = distinguishedNameManager.createDistinguishedName(csrInfo.getSubject());
		} catch (InvalidDistinguishedNameException e) {
			// Invalid DN in CSR
			return false;
		}
		
		// When there are SANs, the cn of the distinguished name should be one of them
		if (!subjectAlternativeNames.isEmpty()) {
			if (!subjectAlternativeNames.containsAll(distinguishedName.getPart("cn"))) {
				return false;
			}
		}
		
		// Fill the contract
		requestContract.setDistinguishedName(csrInfo.getSubject());
		requestContract.setAlternativeNames(subjectAlternativeNames);
		requestContract.setBase64Csr(csrUpload.getBase64Csr());	
		return true;
	}

	private void addInvalidCSRMessage() {
		facesMessages.addFromResourceBundle("validator.invalid.csr");
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

	
	public void setDistinguishedNameManager(DistinguishedNameManager distinguishedNameManager) {
		this.distinguishedNameManager = distinguishedNameManager;
	}
}
