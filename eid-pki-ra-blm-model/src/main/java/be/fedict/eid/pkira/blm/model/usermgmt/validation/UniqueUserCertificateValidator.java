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

package be.fedict.eid.pkira.blm.model.usermgmt.validation;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.Validator;
import org.jboss.seam.Component;

import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.blm.model.usermgmt.UserHome;
import be.fedict.eid.pkira.crypto.CertificateInfo;
import be.fedict.eid.pkira.crypto.CertificateParser;
import be.fedict.eid.pkira.crypto.CryptoException;

/**
 * @author Bram Baeyens
 */
public class UniqueUserCertificateValidator implements Validator<UniqueUserCertificate> {

	@Override
	public void initialize(UniqueUserCertificate constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object value) {
		if (StringUtils.isEmpty((String) value)) {
			return true;
		}

		CertificateInfo info;
		try {
			CertificateParser certificateParser = (CertificateParser) Component.getInstance(CertificateParser.NAME);
			info = certificateParser.parseCertificate((String) value);
		} catch (CryptoException e) {
			// Examined elsewhere
			return true;
		}

		UserHome userHome = (UserHome) Component.getInstance(UserHome.NAME);
		User user = userHome.findByCertificateSubject(info.getDistinguishedName());
		if (user == null || user.equals(userHome.getInstance())) {
			return true;
		}
		return false;
	}
}
