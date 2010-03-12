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
import java.util.List;

import org.jboss.seam.annotations.In;

import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.portal.certificate.Certificate;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

public class CertificateHandlerBean implements CertificateHandler {

	@In(value = EIDPKIRAPrivateServiceClient.NAME, create = true)
	private EIDPKIRAPrivateServiceClient eidpkiraPrivateServiceClient;

	@Override
	public List<Certificate> findCertificateList(String userRRN) {
		List<CertificateWS> listCertificates = eidpkiraPrivateServiceClient.listCertificates(userRRN);
		List<Certificate> certificates = new ArrayList<Certificate>();
		for (CertificateWS certificatews : listCertificates) {
			Certificate certificate = new Certificate(certificatews);

			certificates.add(certificate);
		}
		return certificates;
	}

	@Override
	public Certificate findCertificate(String serialNumber) {
		return null;
	}

	
	protected void setEidpkiraPrivateServiceClient(EIDPKIRAPrivateServiceClient eidpkiraPrivateServiceClient) {
		this.eidpkiraPrivateServiceClient = eidpkiraPrivateServiceClient;
	}
}
