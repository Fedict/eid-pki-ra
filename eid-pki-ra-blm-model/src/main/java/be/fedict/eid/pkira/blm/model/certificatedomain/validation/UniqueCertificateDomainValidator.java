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

package be.fedict.eid.pkira.blm.model.certificatedomain.validation;

import java.util.List;

import org.hibernate.validator.Validator;
import org.jboss.seam.Component;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainHome;
import be.fedict.eid.pkira.dnfilter.DistinguishedName;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameManager;
import be.fedict.eid.pkira.dnfilter.InvalidDistinguishedNameException;

/**
 * @author Bram Baeyens
 *
 */
public class UniqueCertificateDomainValidator implements Validator<UniqueCertificateDomain> {

	@Override
	public void initialize(UniqueCertificateDomain constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object value) {		
		// Create the DN
		CertificateDomain certificateDomain = (CertificateDomain) value;
		DistinguishedName distinguishedName;		
		try {
			distinguishedName = getDistinguishedNameManager().createDistinguishedName(certificateDomain.getDnExpression());
		} catch (InvalidDistinguishedNameException e) {
			return false;
		}
		
		// Look up the matching certificate domains
		List<CertificateDomain> allDomains = getCertificateDomainHome().findByCertificateTypes(certificateDomain.getCertificateTypes());

		// Look for overlaps
		for (CertificateDomain otherDomain : allDomains) {
			try {
				DistinguishedName otherDistinguishedName = 
					getDistinguishedNameManager().createDistinguishedName(otherDomain.getDnExpression());
				if (otherDistinguishedName.matches(distinguishedName) 
						&& !otherDomain.equals(certificateDomain)) {
					return false;
				}
			} catch (InvalidDistinguishedNameException e) {
				throw new RuntimeException("Invalid DN expression found in database: " + otherDomain.getDnExpression());
			}
		}
		return true;
	}
	
	protected DistinguishedNameManager getDistinguishedNameManager() {
		return (DistinguishedNameManager) Component.getInstance(DistinguishedNameManager.NAME);
	}
	
	protected CertificateDomainHome getCertificateDomainHome() {
		return (CertificateDomainHome) Component.getInstance(CertificateDomainHome.NAME);
	}

}
