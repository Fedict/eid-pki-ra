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

package be.fedict.eid.pkira.portal.csr;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.crypto.CSRParser;
import be.fedict.eid.pkira.crypto.CryptoException;

/**
 * @author Bram Baeyens
 * 
 */
@Name(CertificateHandler.NAME)
public class CertificateHandlerBean implements CertificateHandler {

	@Logger
	private Log log;
	
	@Out
	private CertificateSigningRequest certificateSigningRequest;
	private CSRParser csrParser;
	
	protected void setLog(Log log) {
		this.log = log;
	}
	
	@In(create=true)
	protected void setCertificateSigningRequest(CertificateSigningRequest certificateSigningRequest) {
		this.certificateSigningRequest = certificateSigningRequest;
	}
	
	@In(create=true)
	protected void setCsrParser(CSRParser csrParser) {
		this.csrParser = csrParser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seebe.fedict.eid.pkira.portal.csr.CertificateHandler#
	 * uploadCertificateSigningRequest()
	 */
	@Override
	public String uploadCertificateSigningRequest() {
		log.info(">>> uploadCertificateSigningRequest(certificateSigningRequest=[{0}])", certificateSigningRequest);
//		try {
			//TODO: get this to work
			//CSRInfo csrInfo = csrParser.parseCSR(certificateSigningRequest.getBase64Csr());
			certificateSigningRequest.setDistinguishedName(new CSRInfo("testDN"));
//		} catch (CryptoException e) {
//			throw new RuntimeException("Invalid csr", e);
//		}
		log.info("<<< uploadCertificateSigningRequest: {0}", certificateSigningRequest);
		return "/page/csr/complete.xhtml";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seebe.fedict.eid.pkira.portal.csr.CertificateHandler#
	 * requestCertificateSigningRequest()
	 */
	@Override
	public String requestCertificateSigningRequest() {
		log.info(">>> requestCertificateSigningRequest(certificateSigningRequest=[{0}])", certificateSigningRequest);
//		CertificateSigningRequestBuilder builder = new CertificateSigningRequestBuilder();
//		
		log.info("<<< requestCertificateSigningRequest");
		return "success";
	}
}
