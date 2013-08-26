/**
 * eID PKI RA Project. 
 * Copyright (C) 2010-2012 FedICT. 
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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.framework.ValidatingEntityHome;
import be.fedict.eid.pkira.blm.model.usermgmt.User;

/**
 * @author Bram Baeyens
 */
@Name(CertificateDomainHome.NAME)
public class CertificateDomainHome extends ValidatingEntityHome<CertificateDomain> {

	private static final long serialVersionUID = -1444261850784921995L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.certificateDomainHome";
	
	public CertificateDomain findByDnExpression(String dnExpression) {
		try {
			return (CertificateDomain) getEntityManager().createNamedQuery("findCertificateDomainByDnExpression")
					.setParameter("dnExpression", dnExpression)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CertificateDomain> findUnregistered(User requester) {
		return getEntityManager().createNamedQuery("findCertificateDomainUnregistered")
				.setParameter("requester", requester)
				.getResultList();
	}

	public CertificateDomain findByName(String name) {
		try {
			return (CertificateDomain) getEntityManager().createNamedQuery("findCertificateDomainByName")
					.setParameter("name", name)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
		
	@SuppressWarnings("unchecked")
	public List<CertificateDomain> findByCertificateTypes(Set<CertificateType> types) {
		if (types == null || types.size() == 0) {
			return Collections.emptyList();
		}

		// Build the query
		StringBuilder query = new StringBuilder();
		for (CertificateType type : types) {
			if (query.length() != 0) {
				query.append(" OR ");
			}
			query.append(type.name());
			query.append("CERT=true");
		}
		query.insert(0, "FROM CertificateDomain WHERE ");

		// Execute it
		return getEntityManager().createQuery(query.toString())
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
}
