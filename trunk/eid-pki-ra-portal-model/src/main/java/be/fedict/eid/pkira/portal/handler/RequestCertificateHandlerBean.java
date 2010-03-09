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

import be.fedict.eid.pkira.contracts.CertificateSigningRequestBuilder;
import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.contracts.EntityBuilder;
import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.crypto.CryptoException;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;
import be.fedict.eid.pkira.portal.domain.CSRUpload;
import be.fedict.eid.pkira.portal.domain.CertificateSigningRequest;
import be.fedict.eid.pkira.portal.domain.Operator;

/**
 * @author Bram Baeyens
 * 
 */
@Name(RequestCertificateHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class RequestCertificateHandlerBean implements RequestCertificateHandler, Serializable {

	private static final long serialVersionUID = -3944656911997801484L;

	@Logger
	private Log log;
	
	@Out
	private CertificateSigningRequest signableCertificate;
	private CSRUpload csrUpload;
	private FacesMessages facesMessages;
	private EIDPKIRAContractsClient contractsClientPortal;
	
	protected void setLog(Log log) {
		this.log = log;
	}
	
	@In(create=true)
	protected void setSignableCertificate(CertificateSigningRequest signableCertificate) {
		this.signableCertificate = signableCertificate;
	}
	
	@In(create=true, value="csrUpload")
	protected void setCsrUpload(CSRUpload csrUpload) {
		this.csrUpload = csrUpload;
	}

	@In(create=true)
	public void setFacesMessages(FacesMessages facesMessages) {
		this.facesMessages = facesMessages;
	}

	@In(value=EIDPKIRAContractsClient.NAME, create=true)
	public void setContractsClientPortal(EIDPKIRAContractsClient contractsClientPortal) {
		this.contractsClientPortal = contractsClientPortal;
	}

	@Override
	@Begin
	public String uploadCertificateSigningRequest() {
		log.debug(">>> uploadCertificateSigningRequest(certificateSigningRequest=[{0}])", signableCertificate);
		try {
			signableCertificate.setDistinguishedName(csrUpload.extractCsrInfo());
			signableCertificate.setBase64Csr(csrUpload.getBase64Csr());
		} catch (CryptoException e) {
			log.info("<<< uploadCertificateSigningRequest: invalid CSR", e);
			facesMessages.addFromResourceBundle("validator.csr.invalid");
			return null;
		}
		log.debug("<<< uploadCertificateSigningRequest: {0}", signableCertificate);
		return "success";
	}

	@Override
	public String preDssSignCertificateSigningRequest() {
		log.info(">>> preDssSignCertificateSigningRequest(certificateSigningRequest=[{0}])", signableCertificate);
		CertificateSigningRequestBuilder builder = initBuilder(signableCertificate);
		try {
			String base64CsrXml = contractsClientPortal.marshalToBase64(builder.toRequestType(), 
					CertificateSigningRequestType.class);
			signableCertificate.setBase64CsrXml(base64CsrXml);
		} catch (XmlMarshallingException e) {
			log.info("<<< requestCertificateSigningRequest: marshalling failed", e);
			throw new RuntimeException(e);
		}
		log.info("<<< preDssSignCertificateSigningRequest: {0}", signableCertificate);
		return "success";
	}

	@Override
	public String preDssSignCertificateRevokationRequest() {
//		log.info(">>> preDssSignCertificateRevokationRequest(certificateRevokationRequest=[{0}])", signableCertificate);
//		CertificateRevocationRequestBuilder builder = initBuilder(certificateSigningRequest);
//		try {
//			String base64Xml = contractsClientPortal.marshalToBase64(builder.toRequestType(), 
//					CertificateRevocationRequestType.class);
//			certificateSigningRequest.setCsrBase64Xml(base64Xml);
//		} catch (XmlMarshallingException e) {
//			log.info("<<< preDssSignCertificateRevokationRequest: marshalling failed", e);
//			throw new RuntimeException(e);
//		}
//		log.info("<<< preDssSignCertificateRevokationRequest: {0}", signableCertificate);
		return "success";
	}

	private CertificateSigningRequestBuilder initBuilder(CertificateSigningRequest certificate) {
		return new CertificateSigningRequestBuilder()
				.setOperator(initBuilder(certificate.getOperator()).toEntityType())
				.setLegalNotice(certificate.getLegalNotice())
				.setDistinguishedName(certificate.getDistinguishedName().getSubject())
				.setCsr(certificate.getBase64Csr())
				.setCertificateType(Enum.valueOf(CertificateTypeType.class, certificate.getCertificateType().name()))
				.setValidityPeriodMonths(certificate.getValidityPeriod())
				.setDescription(certificate.getDescription());
	}

	private EntityBuilder initBuilder(Operator operator) {
		return new EntityBuilder()
				.setName(operator.getName())
				.setFunction(operator.getFunction())
				.setPhone(operator.getPhone());
	}
}
