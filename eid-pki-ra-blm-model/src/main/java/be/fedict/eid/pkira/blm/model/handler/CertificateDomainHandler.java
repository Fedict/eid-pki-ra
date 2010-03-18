/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */
package be.fedict.eid.pkira.blm.model.handler;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainManager;
import be.fedict.eid.pkira.blm.model.certificatedomain.DistinguishedNameOverlapsException;
import be.fedict.eid.pkira.blm.model.certificatedomain.InvalidCertificateDomainNameException;
import be.fedict.eid.pkira.blm.model.certificatedomain.NoCertificateTypesSelectedException;
import be.fedict.eid.pkira.dnfilter.InvalidDistinguishedNameException;


/**
 * @author Jan Van den Bergh
 *
 */
@Name("certificateDomainHandler")
@Scope(ScopeType.EVENT)
public class CertificateDomainHandler {

	@In(value="certificateDomain")
	private CertificateDomain certificateDomain;
	
	@In(value=CertificateDomainManager.NAME, create=true)
	private CertificateDomainManager certificateDomainManager;
	
	@In(create=true)
	private FacesMessages facesMessages;
	
	public String saveCertificateDomain() {
		try {
			certificateDomainManager.saveCertificateDomain(certificateDomain);
			facesMessages.addFromResourceBundle("certificatedomain.saved']");			
			return "success";
		} catch (InvalidDistinguishedNameException e) {
			facesMessages.addFromResourceBundle("certificatedomain.error.invaliddn");			
		} catch (DistinguishedNameOverlapsException e) {
			facesMessages.addFromResourceBundle("certificatedomain.error.dnoverlaps");
		} catch (InvalidCertificateDomainNameException e) {
			facesMessages.addFromResourceBundle("certificatedomain.error.invalidname");
		} catch (NoCertificateTypesSelectedException e) {
			facesMessages.addFromResourceBundle("certificatedomain.error.nocertificatetypes");
		}
		return "error";
	}
}
