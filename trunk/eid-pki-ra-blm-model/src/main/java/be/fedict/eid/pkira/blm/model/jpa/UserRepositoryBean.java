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

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.domain.User;

/**
 * @author Bram Baeyens
 *
 */
@Stateless
@Name(UserRepository.NAME)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserRepositoryBean implements UserRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void persist(User user) {
		entityManager.persist(user);
	}

	@Override
	public User findById(Integer id) {
		return entityManager.find(User.class, id);
	}

	@Override
	public User findByNationalRegisterNumber(String nationalRegisterNumber) {
		return (User) entityManager.createNamedQuery("findByNationalRegisterNumber")
				.setParameter("nationalRegisterNumber", nationalRegisterNumber)
				.getSingleResult();
	}

}
