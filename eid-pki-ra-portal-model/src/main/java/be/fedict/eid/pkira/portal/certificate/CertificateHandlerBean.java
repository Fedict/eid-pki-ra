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
package be.fedict.eid.pkira.portal.certificate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.portal.ra.CertificateType;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

@Name(CertificateHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class CertificateHandlerBean implements CertificateHandler {
	
	private static final long serialVersionUID = -5017092109045531172L;

	@Out(value=Certificate.NAME, scope=ScopeType.CONVERSATION, required=false)
	private Certificate certificate;

	@In
	private EIdUserCredentials credentials;
	
	@In(value = EIDPKIRAPrivateServiceClient.NAME, create = true)
	private EIDPKIRAPrivateServiceClient eidpkiraPrivateServiceClient;

	@In(value="#{facesContext.externalContext}")
	private ExternalContext extCtx;
	
	@Logger
	private Log log;
	
	@DataModel
	private List<Certificate> certificates;
	
	@In(value="#{facesContext}")
	private javax.faces.context.FacesContext facesContext;	
	
	private String certificateDomainWSID = null;
	
	@Override
	public void download(){
		HttpServletResponse response = (HttpServletResponse) extCtx.getResponse();
	
		response.setContentType("application/x-pem-file");
		String fileName = certificate.getSerialNumber() + ".crt";
		response.addHeader("Content-disposition", "attachment; filename=\"" + fileName +"\"");
		
		try{
			ServletOutputStream servletOutputStream = response.getOutputStream();
			servletOutputStream.write(certificate.getX509().getBytes());
			servletOutputStream.flush();
			servletOutputStream.close();
			facesContext.responseComplete();
		}catch(Exception e){
			log.error("Failure: " + e.toString());
		}
	}
	
	@Override
	public List<Certificate> findCertificateList(){
		return findCertificateList(credentials.getUser().getRRN());
	}
	
	@Override 
	public List<Certificate> findCertificateList(String userRRN) {
		List<CertificateWS> listCertificates = eidpkiraPrivateServiceClient.listCertificates(userRRN, certificateDomainWSID);
		certificates = new ArrayList<Certificate>();
		for (CertificateWS certificatews : listCertificates) {
			certificates.add(parse(certificatews));
		}
		return certificates;
	}

	@Override
	public String getCertificate(String serialNumber) {
		certificate = findCertificate(credentials.getUser().getRRN(), serialNumber);
		return "success";
	}
	 
	@Factory("certificates")
	public void initCertificateList(){
		findCertificateList(credentials.getUser().getRRN());
	}
	
	@Override
	public String prepareRevocation(String serialNumber, String issuer) {
		log.info(">>> preprareRevocation(serialNumber[{0}])",serialNumber);
		certificate = findCertificate(issuer, serialNumber);
		log.info("<<< preprareRevocation: {0})",certificate);
		return "revokeContract";
	}

	@Override
	public String showDetail(Certificate certificate) {
		log.info(">>> showDetail(certificaat:{0})",certificate.toString());
		this.certificate = findCertificate(credentials.getUser().getRRN(), certificate.getSerialNumber());
		log.info("<<< showDetail: {0})",certificate);
		return "certificateDetail";
	}

	private Certificate findCertificate(String userRRN, String serialNumber) {
		CertificateWS certificateWS = eidpkiraPrivateServiceClient.findCertificate(userRRN, serialNumber);
		return parse(certificateWS);
	}

	private CertificateType map(CertificateTypeWS certificateType) {
		return Enum.valueOf(CertificateType.class, certificateType.toString());
	}

	private Date map(XMLGregorianCalendar validityStart) {
		return validityStart.toGregorianCalendar().getTime();
	}
	
	private Certificate parse(CertificateWS certificatews) {
		Certificate certificate = new Certificate();
		certificate.setDistinguishedName(certificatews.getDistinguishedName());
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
		certificate.setRequesterName(certificatews.getRequesterName());
		
		return certificate;
	}
	
	protected void setEidpkiraPrivateServiceClient(EIDPKIRAPrivateServiceClient eidpkiraPrivateServiceClient) {
		this.eidpkiraPrivateServiceClient = eidpkiraPrivateServiceClient;
	}

	public void setCertificateDomainWSID(String certificateDomainWSID) {
		this.certificateDomainWSID = certificateDomainWSID;
	}

	public String getCertificateDomainWSID() {
		return certificateDomainWSID;
	}
}
