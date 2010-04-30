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
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;
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

	@Logger
	private Log log;
	
	@DataModel
	private List<Certificate> certificates;
	
	@In(value=CertificateMapper.NAME, create=true)
	private CertificateMapper certificateMapper;
	
	private String certificateDomainWSID = null;
	
	@In(value=CertificateWSHome.NAME, create=true)
	private CertificateWSHome certificateWSHome;
	
	@Override
	public List<Certificate> findCertificateList(){
		return findCertificateList(credentials.getUser().getRRN());
	}
	
	@Override 
	public List<Certificate> findCertificateList(String userRRN) {
		List<CertificateWS> listCertificates = eidpkiraPrivateServiceClient.listCertificates(userRRN, certificateDomainWSID);
		certificates = new ArrayList<Certificate>();
		for (CertificateWS certificatews : listCertificates) {
			certificates.add(certificateMapper.map(certificatews));
		}
		return certificates;
	}
	 
	@Factory("certificates")
	public void initCertificateList(){
		findCertificateList(credentials.getUser().getRRN());
	}
	
	@Override
	@Begin(join=true)
	public String prepareRevocation(Integer certificateId) {
		log.info(">>> preprareRevocation(certificateId[{0}])", certificateId);
		certificateWSHome.setId(certificateId);
		certificate = certificateWSHome.getInstance();
		log.info("<<< preprareRevocation: {0})", certificate);
		return "revokeContract";
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
