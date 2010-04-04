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

package be.fedict.eid.pkira.blm.model.certificatedomain;

import java.util.List;

import javax.persistence.NoResultException;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.dnfilter.InvalidDistinguishedNameException;

/**
 * @author Bram Baeyens
 */
@Name(CertificateDomainHome.NAME)
public class CertificateDomainHome extends EntityHome<CertificateDomain> {

	private static final long serialVersionUID = -1444261850784921995L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.certificateDomainHome";
	
	@In(create=true, value=CertificateDomainValidator.NAME)
	private CertificateDomainValidator certificateDomainValidator;
	
	@Override
	public String persist() {
		try {
			certificateDomainValidator.validate(getInstance());
		} catch (CertificateDomainException e) {
			getStatusMessages().addFromResourceBundle(e.getMessageKey());
			return null;
		} catch (InvalidDistinguishedNameException e) {
			getStatusMessages().addFromResourceBundle("certificatedomain.error.invalidname");
			return null;
		}
		return super.persist();
	}
	
	@Override
	public String update() {
		try {
			certificateDomainValidator.validate(getInstance());
		} catch (CertificateDomainException e) {
			getStatusMessages().addFromResourceBundle(e.getMessageKey());
			return null;
		} catch (InvalidDistinguishedNameException e) {
			getStatusMessages().addFromResourceBundle("certificatedomain.error.invalidname");
			return null;
		}
		return super.update();
	}

	public CertificateDomain findByDnExpression(String dnExpression) {
		try {
			return (CertificateDomain) getEntityManager().createNamedQuery("findCertificateDomainByDnExpression")
					.setParameter("dnExpression", dnExpression).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CertificateDomain> findUnregistered(User requester) {
		return getEntityManager().createNamedQuery("findCertificateDomainUnregistered").setParameter("requester", requester)
				.getResultList();
	}
	
	@Override
	protected String getUpdatedMessageKey() {
		return "certificatedomain.updated";
	}
	
	@Override
	protected String getCreatedMessageKey() {
		return "certificatedomain.created";
	}

	public void setCertificateDomainValidator(CertificateDomainValidator certificateDomainValidator) {
		this.certificateDomainValidator = certificateDomainValidator;
	}
	
}
