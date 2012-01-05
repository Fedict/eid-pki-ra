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

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;


/**
 * @author Bram Baeyens
 */
@Stateless
@Name(UserRepository.NAME)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserRepositoryBean	implements UserRepository {

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
		try {
			return entityManager.find(User.class, id);
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public User findByNationalRegisterNumber(String nationalRegisterNumber) {
		try {
			return (User) entityManager.createNamedQuery("findByNationalRegisterNumber").setParameter(
					"nationalRegisterNumber", nationalRegisterNumber).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public User findByCertificateSubject(String certificateSubject) {
		try {
			return (User) entityManager.createNamedQuery("findByCertificateSubject").setParameter(
					"certificateSubject", certificateSubject).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public User getReference(Integer primaryKey) {
		return entityManager.getReference(User.class, primaryKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Registration> findActiveUsersByCertificateDomain(
			CertificateDomain certificateDomain) {
		Query namedQuery = entityManager.createNamedQuery("findRegistrationsByCertificateDomain");
		namedQuery.setParameter("certificateDomain", certificateDomain.getId());
		
		List<Registration> requestors = namedQuery.getResultList();
		
		return requestors;
	}

	@Override
	public int getUserCount() {
		Query namedQuery = entityManager.createNamedQuery("getUserCount");
		Long result = (Long) namedQuery.getSingleResult();
		return result.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAdminUsersWithEmail() {
		Query namedQuery = entityManager.createNamedQuery("getAdminUsersWithEmail");
		List<User> result = namedQuery.getResultList();
		return result;
	}
	
	

}
