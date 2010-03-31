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

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.usermgmt.User;

/**
 * @author Bram Baeyens
 */
@Stateless
@Name(CertificateDomainRepository.NAME)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CertificateDomainRepositoryBean implements CertificateDomainRepository {

	@PersistenceContext
	private EntityManager entityManager;

	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void persist(CertificateDomain certificateDomain) {
		entityManager.persist(certificateDomain);
	}

	@Override
	public CertificateDomain findByName(String name) {
		try {
			return (CertificateDomain) entityManager.createNamedQuery("findCertificateDomainByName").setParameter(
					"name", name).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public CertificateDomain findByDnExpression(String dnExpression) {
		try {
			return (CertificateDomain) entityManager.createNamedQuery("findCertificateDomainByDnExpression")
					.setParameter("dnExpression", dnExpression).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public CertificateDomain findById(Integer id) {
		return entityManager.find(CertificateDomain.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
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
		return entityManager.createQuery(query.toString()).getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CertificateDomain> findUnregistered(User requester) {
		return entityManager.createNamedQuery("findCertificateDomainUnregistered").setParameter("requester", requester)
				.getResultList();
	}

	@Override
	public CertificateDomain getReference(Integer primaryKey) {
		return entityManager.getReference(CertificateDomain.class, primaryKey);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CertificateDomain> findAll() {
		return entityManager.createNamedQuery("findCertificateDomainAll").getResultList();
	}
}
