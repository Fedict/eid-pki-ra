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

package be.fedict.eid.pkira.blm.model.usermgmt;

import javax.persistence.NoResultException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.blm.model.framework.ValidatingEntityHome;
import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.crypto.certificate.CertificateInfo;
import be.fedict.eid.pkira.crypto.certificate.CertificateParser;
import be.fedict.eid.pkira.crypto.exception.CryptoException;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameManager;
import be.fedict.eid.pkira.dnfilter.InvalidDistinguishedNameException;

/**
 * @author Bram Baeyens
 */
@Name(UserHome.NAME)
@Scope(ScopeType.CONVERSATION)
public class UserHome extends ValidatingEntityHome<User> {

	private static final long serialVersionUID = -9041171409557145771L;

	public static final String NAME = "be.fedict.eid.pkira.blm.userHome";

	@In(required = false)
	private EIdUserCredentials credentials;

	@In(create = true, value = CertificateParser.NAME)
	private CertificateParser certificateParser;

	@In(create = true, value = DistinguishedNameManager.NAME)
	private DistinguishedNameManager distinguishedNameManager;

	@Logger
	private Log log;

	@Override
	protected String getUpdatedMessageKey() {
		return "user.updated";
	}

	@Override
	public String persist() {
		updateCertificateSubject();
		return super.persist();
	}

	@Override
	public String update() {
		updateCertificateSubject();
		return super.update();
	}

	public boolean isCurrentUser() {
		if (credentials == null) {
			return false;
		}
		return getInstance().getNationalRegisterNumber().equals(credentials.getUser().getRRN());
	}

	public String revokeAdmin() {
		getInstance().setAdmin(false);
		return update();
	}

	public String grantAdmin() {
		getInstance().setAdmin(true);
		return update();
	}

	public User findByNationalRegisterNumber(String nationalRegisterNumber) {
		try {
			return (User) getEntityManager().createNamedQuery("findByNationalRegisterNumber")
					.setParameter("nationalRegisterNumber", nationalRegisterNumber).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public User findByCertificateSubject(String value) {
		try {
			return (User) getEntityManager().createNamedQuery("findByCertificateSubject")
					.setParameter("certificateSubject", value).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	private void updateCertificateSubject() {
		User instance = getInstance();
		String certificate = instance.getCertificate();

		if (certificate == null) {
			instance.setCertificateSubject(null);
		} else {
			try {
				CertificateInfo certificateInfo = certificateParser.parseCertificate(certificate);
				String distinguishedName = certificateInfo.getDistinguishedName();

				distinguishedName = distinguishedNameManager.createDistinguishedName(distinguishedName).toString();

				instance.setCertificateSubject(distinguishedName);
			} catch (CryptoException e) {
				StatusMessages.instance().add(Severity.ERROR, "Cannot parse the certificate.");
				log.error("Cannot parse user certificate. Leaving both set to null.", e);

				instance.setCertificate(null);
				instance.setCertificateSubject(null);
			} catch (InvalidDistinguishedNameException e) {
				StatusMessages.instance().add(Severity.ERROR, "Cannot parse the DN in the certificate.");
				log.error("Cannot parse the DN in the certificate. Leaving both set to null.", e);

				instance.setCertificate(null);
				instance.setCertificateSubject(null);
			}

		}
	}
}
