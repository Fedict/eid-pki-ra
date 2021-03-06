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

package be.fedict.eid.pkira.blm.model.certificatedomain.validation;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.Validator;
import org.jboss.seam.Component;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainHome;

/**
 * @author Bram Baeyens
 *
 */
public class UniqueCertificateDomainNameValidator implements Validator<UniqueCertificateDomainName> {

	@Override
	public void initialize(UniqueCertificateDomainName constraintAnnotation) {		
	}

	@Override
	public boolean isValid(Object value) {
		if (StringUtils.isEmpty((String) value)) {
			return true;
		}		
		CertificateDomainHome certificateDomainHome = 
			(CertificateDomainHome) Component.getInstance(CertificateDomainHome.NAME);
		CertificateDomain certificateDomain = certificateDomainHome.findByName((String) value);
		if (certificateDomain == null || certificateDomain.equals(certificateDomainHome.getInstance())) {
			return true;
		}
		return false;
	}

}
