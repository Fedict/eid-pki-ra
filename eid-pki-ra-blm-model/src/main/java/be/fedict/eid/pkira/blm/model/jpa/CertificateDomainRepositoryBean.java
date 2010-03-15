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

package be.fedict.eid.pkira.blm.model.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.domain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.domain.User;

/**
 * @author Bram Baeyens
 *
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
		return (CertificateDomain) entityManager.createNamedQuery("findByName")
				.setParameter("name", name)
				.getSingleResult();
	}

	@Override
	public CertificateDomain findByDnExpression(String dnExpression) {
		return (CertificateDomain) entityManager.createNamedQuery("findByDnExpression")
				.setParameter("dnExpression", dnExpression)
				.getSingleResult();
	}

	@Override
	public CertificateDomain findById(Integer id) {
		return entityManager.find(CertificateDomain.class, id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CertificateDomain> getUnregistered(User requester) {
		return entityManager.createNamedQuery("findUnregistered")
				.setParameter("requester", requester)
				.getResultList();
	}
}
