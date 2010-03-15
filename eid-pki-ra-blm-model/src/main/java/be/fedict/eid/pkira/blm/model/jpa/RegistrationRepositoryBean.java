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

import be.fedict.eid.pkira.blm.model.domain.Registration;
import be.fedict.eid.pkira.blm.model.domain.RegistrationStatus;

/**
 * @author Bram Baeyens
 *
 */
@Stateless
@Name(RegistrationRepository.NAME)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RegistrationRepositoryBean implements RegistrationRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Registration> findAllNewRegistrations() {
		return findRegistrationsByStatus(RegistrationStatus.NEW);
	}

	@Override
	public void persist(Registration registration) {
		entityManager.persist(registration);
	}
	
	@SuppressWarnings("unchecked")
	private List<Registration> findRegistrationsByStatus(RegistrationStatus status) {
		return entityManager.createNamedQuery("findRegistrationsByStatus")
				.setParameter("status", status)
				.getResultList();
	}

	@Override
	public void reject(Registration registration) {
		updateStatus(registration, RegistrationStatus.REJECTED);
	}
	
	@Override
	public void confirm(Registration registration) {
		updateStatus(registration, RegistrationStatus.CONFIRMED);
	}
	
	private void updateStatus(Registration registration, RegistrationStatus status) {
		entityManager.getReference(Registration.class, registration.getId());
		registration.setStatus(status);
	}

}
