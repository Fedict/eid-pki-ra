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
package be.fedict.eid.pkira.blm.model.contracts;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

@Name(CertificateListHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class CertificateListHandlerBean implements CertificateListHandler {

	private static final long serialVersionUID = -5017092109045531175L;

	private static final String CERTIFICATES_NAME = "certificates";

	@DataModel
	private List<Certificate> certificates;

	private Integer certificateDomainId;

	@In(value = CertificateQuery.NAME, create = true)
	private CertificateQuery certificateQuery;

	@Override
	public List<Certificate> findCertificateList() {
		certificates = certificateQuery.getFindCertificates(certificateDomainId);
		return certificates;
	}

	@Factory(CERTIFICATES_NAME)
	public void initCertificateList() {
		findCertificateList();
	}

	public void setCertificateDomainId(Integer certificateDomainId) {
		this.certificateDomainId = certificateDomainId;
	}

	public Integer getCertificateDomainId() {
		return certificateDomainId;
	}

}
