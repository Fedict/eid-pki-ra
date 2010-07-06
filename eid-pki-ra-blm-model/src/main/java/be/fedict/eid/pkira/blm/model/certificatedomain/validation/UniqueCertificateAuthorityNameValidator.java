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

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.Validator;
import org.jboss.seam.Component;

import be.fedict.eid.pkira.blm.model.ca.CertificateAuthority;
import be.fedict.eid.pkira.blm.model.ca.CertificateAuthorityHome;

/**
 * @author Bram Baeyens
 *
 */
public class UniqueCertificateAuthorityNameValidator implements Validator<UniqueCertificateAuthorityName> {

	@Override
	public void initialize(UniqueCertificateAuthorityName constraintAnnotation) {		
	}

	@Override
	public boolean isValid(Object value) {
		if (StringUtils.isEmpty((String) value)) {
			return true;
		}		
		CertificateAuthorityHome certificateAuthorityHome = (CertificateAuthorityHome) Component.getInstance(CertificateAuthorityHome.NAME);
		CertificateAuthority certificateAuthority = certificateAuthorityHome.findByName((String) value);
		if (certificateAuthority == null || certificateAuthority.equals(certificateAuthorityHome.getInstance())) {
			return true;
		}
		return false;
	}

}
