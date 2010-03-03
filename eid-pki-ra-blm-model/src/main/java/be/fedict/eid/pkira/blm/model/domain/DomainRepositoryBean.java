/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */
package be.fedict.eid.pkira.blm.model.domain;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;



/**
 * Implementation of the domain repository.
 * @author Jan Van den Bergh
 */
@Stateless
public class DomainRepositoryBean implements DomainRepository {

	@In
	private EntityManager entityManager;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void persistCertificate(Certificate certificate) {
		entityManager.persist(certificate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void persistContract(AbstractContract contract) {
		entityManager.persist(contract);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Certificate> findAllCertificates(String userRRN) {
		Query query = entityManager.createNamedQuery("SELECT distinct c from CERTIFICATE c");
		return query.getResultList();
	}

}
