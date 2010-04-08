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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.usermgmt.User;

/**
 * @author Bram Baeyens
 */
@Name(CertificateDomainHome.NAME)
public class CertificateDomainHome extends EntityHome<CertificateDomain> {

	private static final long serialVersionUID = -1444261850784921995L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.certificateDomainHome";
	
	public void setCertificateDomainId(Integer id) {
		setId(id);
	}
	
	public Integer getCertificateDomainId() {
		return (Integer) getId();
	}
	
	@Override
	protected CertificateDomain createInstance() {
		return new CertificateDomain();
	}
	
	public void wire() {
		if (getInstance() != null && getInstance().getId() != null 
				 && !isManaged() && !getEntityManager().contains(getInstance())) {
			getEntityManager().merge(getInstance());
		}
	}
	
	public boolean isWired() {
		return true;
	}
	
	public CertificateDomain findByDnExpression(String dnExpression) {
		try {
			getEntityManager().setFlushMode(FlushModeType.COMMIT);
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
			getEntityManager().setFlushMode(FlushModeType.COMMIT);
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
