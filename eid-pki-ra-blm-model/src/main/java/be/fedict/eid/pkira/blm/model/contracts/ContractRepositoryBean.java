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
package be.fedict.eid.pkira.blm.model.contracts;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationStatus;

/**
 * Implementation of the domain repository.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
@Name(ContractRepository.NAME)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ContractRepositoryBean implements ContractRepository {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void persistCertificate(Certificate certificate) {
		if (findCertificate(certificate.getIssuer(), certificate.getSerialNumber()) != null) {
			// certificate already exists!
			throw new RuntimeException("Duplicate certificate in database: " + certificate.getIssuer() + "/"
					+ certificate.getSerialNumber());
		}
		
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
	public List<Certificate> findAllCertificates(String userRRN, String certificateDomainID) {		
		String squery = "SELECT c" +
						" FROM Certificate c, Registration r" +
						" WHERE c.certificateDomain=r.certificateDomain" +
						" AND r.status='" + RegistrationStatus.APPROVED.name() + "'" +
						" AND r.requester.nationalRegisterNumber=" + userRRN;
		
		if(certificateDomainID != null){
			squery += " AND c.certificateDomain.id = " + certificateDomainID ;
		}
		Query query = entityManager.createQuery(squery);
		return query.getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Certificate findCertificate(String issuer, BigInteger serialNumber) {
		Query query = entityManager
		//TODO: add issuer
				.createQuery("SELECT distinct c from Certificate c WHERE serialNumber=?");
		//query.setParameter(1, issuer);
		query.setParameter(1, serialNumber);
		try {
			Certificate result = (Certificate) query.getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
		} catch (EntityNotFoundException e) {
			return null;
		} catch (NonUniqueResultException e) {
			throw new RuntimeException("Too many results for certificate search" + issuer + "/" + serialNumber);
		}
	}
	
	@Override
	public Certificate findCertificateByCertificateDomain(
			CertificateDomain certificateDomain) {
		Query query = entityManager
		//TODO: add issuer
				.createQuery("SELECT distinct c from Certificate c WHERE distinguishedName=?");
		//query.setParameter(1, issuer);
		query.setParameter(1, certificateDomain.getDnExpression());
		try {
			Certificate result = (Certificate) query.getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
		} catch (EntityNotFoundException e) {
			return null;
		} catch (NonUniqueResultException e) {
			throw new RuntimeException("Too many results for certificate search:" +  certificateDomain.getDnExpression());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void removeCertificate(Certificate certificate) {
		certificate = entityManager.getReference(Certificate.class, certificate.getId());
		entityManager.remove(certificate);
	}

	/**
	 * Injects the entity manager.
	 */
	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
