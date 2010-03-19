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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.portal.domain.Certificate;
import be.fedict.eid.pkira.portal.domain.CertificateType;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

@Name(CertificateHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class CertificateHandlerBean implements CertificateHandler {
	
	private static final long serialVersionUID = -5017092109045531172L;

	@Logger
	private Log log;
	
	@In(value = EIDPKIRAPrivateServiceClient.NAME, create = true)
	private EIDPKIRAPrivateServiceClient eidpkiraPrivateServiceClient;
	
	@Out(scope=ScopeType.CONVERSATION)
	private Certificate certificate;	
	
	@Override
	public List<Certificate> findCertificateList(String userRRN) {
		List<CertificateWS> listCertificates = eidpkiraPrivateServiceClient.listCertificates(userRRN);
		List<Certificate> certificates = new ArrayList<Certificate>();
		for (CertificateWS certificatews : listCertificates) {
			certificates.add(parse(certificatews));
		}
		return certificates;
	}

	@Override
	public String getCertificate(String serialNumber) {
		certificate = findCertificate("#{currentUser.userRRN}", serialNumber);
		return "success";
	}
	 
	private Certificate findCertificate(String userRRN, String serialNumber) {
		CertificateWS certificateWS = eidpkiraPrivateServiceClient.findCertificate(userRRN, serialNumber);
		return parse(certificateWS);
	}
	
	protected void setEidpkiraPrivateServiceClient(EIDPKIRAPrivateServiceClient eidpkiraPrivateServiceClient) {
		this.eidpkiraPrivateServiceClient = eidpkiraPrivateServiceClient;
	}

	private Certificate parse(CertificateWS certificatews) {
		Certificate certificate = new Certificate();
		certificate.setDistinguishedName(new CSRInfo(certificatews.getDistinguishedName()));
		certificate.setIssuer(certificatews.getIssuer());
		certificate.setSerialNumber(certificatews.getSerialNumber());
		certificate.setType(map(certificatews.getCertificateType()));
		if(certificatews.getValidityStart() != null){
			certificate.setValidityStart(map(certificatews.getValidityStart()));
		}
		if(certificatews.getValidityEnd() != null){
			certificate.setValidityEnd(map(certificatews.getValidityEnd()));
		}
		certificate.setX509(certificatews.getX509());
		return certificate;
	}

	private Date map(XMLGregorianCalendar validityStart) {
		return validityStart.toGregorianCalendar().getTime();
	}

	private CertificateType map(CertificateTypeWS certificateType) {
		return Enum.valueOf(CertificateType.class, certificateType.toString());
	}

	@Override
	public String prepareRevocation(String serialNumber, String issuer) {
		log.info(">>> preprareRevocation(serialNumber[{0}])",serialNumber);
		certificate = findCertificate(issuer, serialNumber);
		log.info("<<< preprareRevocation: {0})",certificate);
		return "revokeContract";
	}
}