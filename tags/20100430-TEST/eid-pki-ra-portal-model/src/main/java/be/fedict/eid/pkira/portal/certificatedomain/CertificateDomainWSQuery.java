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

package be.fedict.eid.pkira.portal.certificatedomain;

import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.generated.privatews.CertificateDomainWS;
import be.fedict.eid.pkira.portal.ra.PrivateWSQuery;

/**
 * @author Bram Baeyens
 *
 */
@Name(CertificateDomainWSQuery.NAME)
public class CertificateDomainWSQuery extends PrivateWSQuery {

	private static final long serialVersionUID = -1247395543330472577L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.certificateDomainWSQuery";
	
	@In
	private EIdUserCredentials credentials;
	
	private List<CertificateDomainWS> certificateDomains;
	
	public List<CertificateDomainWS> getFindRemainingCertificateDomains() {
		if (certificateDomains == null) {
			certificateDomains = getServiceClient().findRemainingCertificateDomainsForUser(
					credentials.getUser().getRRN());
		}
		return certificateDomains;
	}
	
	public List<CertificateDomainWS> getFindRegisteredCertificateDomains(){
		if (certificateDomains == null) {
			certificateDomains = getServiceClient().findRegisteredCertificateDomainsForUser(
					credentials.getUser().getRRN());
		}
		return certificateDomains;
	}

}
