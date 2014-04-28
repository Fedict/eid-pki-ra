/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
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

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

@Name(CertificateHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class CertificateHandlerBean implements CertificateHandler {

	private String certificateDomainWSID;

    @Out(value = Certificate.NAME, scope = ScopeType.CONVERSATION, required = false)
    private Certificate certificate;

    @In(value=CertificateWSHome.NAME, create=true)
    private CertificateWSHome certificateWSHome;

	@Override
	@Begin(join = true)
	public String prepareRevocation(Integer certificateId) {
		certificateWSHome.setId(certificateId);
		certificate = certificateWSHome.getInstance();
		return "revokeContract";
	}

	public String getCertificateDomainWSID() {
		return certificateDomainWSID;
	}

	public void setCertificateDomainWSID(String certificateDomainWSID) {
		this.certificateDomainWSID = certificateDomainWSID;
	}

}
