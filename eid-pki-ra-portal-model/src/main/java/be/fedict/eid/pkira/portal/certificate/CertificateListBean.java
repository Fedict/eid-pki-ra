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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

@Stateful
@Name(CertificateList.NAME)
public class CertificateListBean implements CertificateList, Serializable{

	private static final long serialVersionUID = 4024420123671643615L;

	@In(value=EIDPKIRAPrivateServiceClient.NAME, create=true)
	private EIDPKIRAPrivateServiceClient privateServiceClient;
	
	@DataModel
	List<Certificate> certificatesList;

	@DataModelSelection
	@Out(required = false)
	private Certificate certificate;

	@Override
	public List<Certificate> findCertificateList() {		
		List<Certificate> certificates = new ArrayList<Certificate>();
		if(certificatesList == null){
			List<CertificateWS> listCertificates = privateServiceClient.listCertificates("");
			for (CertificateWS certificatews : listCertificates) {
				Certificate certificate = new Certificate(certificatews);
				certificates.add(certificate);
			}
		}
		return certificates;
	}

	@Begin(join = true)
	@Factory("certificatesList")
	public void certificateList() {
		certificatesList = findCertificateList();
	}

	@Override
	public String detailCertificate(Certificate certificate) {
		//TODO Hans: Certificate van back halen ipv het reeds opgehaalde certificaat te gebruiken. 
		setCertificate(certificate);
		return "showcertificate";
	}

	@End
	public void cancel() {
	}

	@Remove
	@Destroy
	public void destroy() {
		certificatesList = null;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public Certificate getCertificate() {
		return certificate;
	}
}
