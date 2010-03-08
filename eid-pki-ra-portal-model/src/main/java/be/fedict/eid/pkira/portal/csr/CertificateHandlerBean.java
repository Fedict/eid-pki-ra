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

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.contracts.CertificateSigningRequestBuilder;
import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.contracts.EntityBuilder;
import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.crypto.CSRParser;
import be.fedict.eid.pkira.crypto.CryptoException;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;

/**
 * @author Bram Baeyens
 * 
 */
@Name(CertificateHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class CertificateHandlerBean implements CertificateHandler, Serializable {

	private static final long serialVersionUID = -3944656911997801484L;

	@Logger
	private Log log;
	
	private CertificateSigningRequest certificateSigningRequest;
	private CSRParser csrParser;
	private FacesMessages facesMessages;
	private EIDPKIRAContractsClient contractsClientPortal;
	
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

	@In(create=true)
	public void setFacesMessages(FacesMessages facesMessages) {
		this.facesMessages = facesMessages;
	}

	@In(create=true)
	public void setContractsClientPortal(EIDPKIRAContractsClient contractsClientPortal) {
		this.contractsClientPortal = contractsClientPortal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seebe.fedict.eid.pkira.portal.csr.CertificateHandler#
	 * uploadCertificateSigningRequest()
	 */
	@Override
	@Begin
	public String uploadCertificateSigningRequest() {
		log.debug(">>> uploadCertificateSigningRequest(certificateSigningRequest=[{0}])", certificateSigningRequest);
		try {
			CSRInfo csrInfo = csrParser.parseCSR(certificateSigningRequest.getBase64Csr());
			certificateSigningRequest.setDistinguishedName(csrInfo);
		} catch (CryptoException e) {
			log.info("<<< uploadCertificateSigningRequest: invalid CSR", e);
			facesMessages.addFromResourceBundle("validator.csr.invalid");
			return null;
		}
		log.debug("<<< uploadCertificateSigningRequest: {0}", certificateSigningRequest);
		return "success";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seebe.fedict.eid.pkira.portal.csr.CertificateHandler#
	 * requestCertificateSigningRequest()
	 */
	@Override
	public String signCertificateSigningRequest() {
		log.info(">>> preSignCertificateSigningRequest(certificateSigningRequest=[{0}])", certificateSigningRequest);
		CertificateSigningRequestBuilder builder = initBuilder(certificateSigningRequest);
		try {
			String base64Xml = contractsClientPortal.marshalToBase64(builder.toRequestType(), 
					CertificateSigningRequestType.class);
			certificateSigningRequest.setCsrBase64Xml(base64Xml);
		} catch (XmlMarshallingException e) {
			log.info("<<< requestCertificateSigningRequest: marshalling failed", e);
			throw new RuntimeException(e);
		}
		log.info("<<< preSignCertificateSigningRequest: {0}", certificateSigningRequest);
		return "success";
	}

	private CertificateSigningRequestBuilder initBuilder(CertificateSigningRequest csr) {
		return new CertificateSigningRequestBuilder()
				.setOperator(new EntityBuilder()
						.setName(csr.getOperatorName())
						.setFunction(csr.getOperatorFunction())
						.setPhone(csr.getOperatorPhone())
						.toEntityType())
				.setLegalNotice(csr.getLegalNotice())
				.setDistinguishedName(csr.getDistinguishedName().getSubject())
				.setCsr(csr.getBase64Csr())
				.setCertificateType(Enum.valueOf(CertificateTypeType.class, csr.getCertificateType().name()))
				.setValidityPeriodMonths(csr.getValidityPeriod())
				.setDescription(csr.getDescription());
	}
}
