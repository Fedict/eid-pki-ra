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

import be.fedict.eid.pkira.blm.model.ca.CertificateAuthority;
import be.fedict.eid.pkira.blm.model.ca.CertificateAuthorityHome;
import be.fedict.eid.pkira.blm.model.util.BaseEntity;

/**
 * @author Bram Baeyens
 *
 */
public class UniqueCertificateAuthorityNameValidator extends BaseEntity implements Validator<UniqueCertificateAuthorityName> {

	@Override
	public void initialize(UniqueCertificateAuthorityName constraintAnnotation) {		
	}

	@Override
	public boolean isValid(Object value) {
		if (StringUtils.isEmpty((String) value)) {
			return true;
		}		
		CertificateAuthorityHome certificateAuthorityHome = (CertificateAuthorityHome) getComponent(CertificateAuthorityHome.class, CertificateAuthorityHome.NAME);//Component.getInstance(CertificateAuthorityHome.NAME);
		CertificateAuthority certificateAuthority = certificateAuthorityHome.findByName((String) value);
		if (certificateAuthority == null || certificateAuthority.equals(certificateAuthorityHome.getInstance())) {
			return true;
		}
		return false;
	}

}
