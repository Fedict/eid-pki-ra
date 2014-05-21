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

package be.fedict.eid.pkira.blm.model.usermgmt.validation;

import org.hibernate.validator.Validator;
import org.jboss.seam.Component;

import be.fedict.eid.pkira.crypto.certificate.CertificateParser;
import be.fedict.eid.pkira.crypto.exception.CryptoException;

/**
 * @author Bram Baeyens
 */
public class ValidUserCertificateValidator implements Validator<ValidUserCertificate> {

	@Override
	public void initialize(ValidUserCertificate constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object value) {
		if (value == null) {
			return true;
		}

		String certificate = (String) value;
		try {
			CertificateParser certificateParser = (CertificateParser) Component.getInstance(CertificateParser.NAME);
			certificateParser.parseCertificate(certificate);
			return true;
		} catch (CryptoException e) {
			return false;
		}
	}

}
