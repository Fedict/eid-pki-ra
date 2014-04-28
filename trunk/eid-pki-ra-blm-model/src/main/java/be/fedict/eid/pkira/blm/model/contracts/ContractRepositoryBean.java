/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
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

import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationStatus;

import static org.apache.commons.lang.StringUtils.isNotBlank;

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
		entityManager.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void persistContract(AbstractContract contract) {
		entityManager.persist(contract);
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<Certificate> findCertificatesByFilter(String userRrn, CertificatesFilter certificatesFilter, Ordering ordering, Paging paging) {
        StringBuilder queryString = new StringBuilder();

        queryString.append("SELECT certificate FROM ");
        appendFromAndWhere(certificatesFilter, queryString);
        appendOrderBy(ordering, queryString);

        Query query = entityManager.createQuery(queryString.toString());
        addQueryParameters(userRrn, certificatesFilter, query);
        setPaging(paging, query);

        return query.getResultList();
    }

    @Override
    public int countCertificatesByFilter(String userRrn, CertificatesFilter certificatesFilter) {
        StringBuilder queryString = new StringBuilder();

        queryString.append("SELECT COUNT(certificate) FROM ");
        appendFromAndWhere(certificatesFilter, queryString);

        Query query = entityManager.createQuery(queryString.toString());
        addQueryParameters(userRrn, certificatesFilter, query);

        return ((Long) query.getSingleResult()).intValue();
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	public Certificate findCertificate(String issuer, BigInteger serialNumber) {
		Query query = entityManager.createQuery("SELECT c from Certificate c WHERE serialNumber=:serialNumber");
		query.setParameter("serialNumber", serialNumber);
		
		try {
            return (Certificate) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (EntityNotFoundException e) {
			return null;
		} catch (NonUniqueResultException e) {
			throw new RuntimeException("Too many results for certificate search " + serialNumber);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Certificate findCertificate(int certificateID) {
		Query query = entityManager.createQuery("SELECT c from Certificate c WHERE id=:id");
		//query.setParameter(1, issuer);
		query.setParameter("id", certificateID);
		
		try {
            return (Certificate) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (EntityNotFoundException e) {
			return null;
		} catch (NonUniqueResultException e) {
			throw new RuntimeException("Too many results for certificate search" + certificateID);
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

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updateContract(AbstractContract contract) {
		entityManager.merge(contract);		
	}

    private void appendFromAndWhere(CertificatesFilter certificatesFilter, StringBuilder queryString) {
        queryString.append(" Certificate certificate, Registration registration WHERE registration.requester.nationalRegisterNumber = :nationalRegisterNumber AND registration.status = :registrationStatus AND registration.certificateDomain = certificate.certificateDomain ");

        if (certificatesFilter.getCertificateDomainId() != null)
            queryString.append(" AND certificate.certificateDomain.id = :certificateDomainId");
        if (isNotBlank(certificatesFilter.getDistinguishedName()))
            queryString.append(" AND lower(certificate.distinguishedName) LIKE :distinguishedName");
        if (isNotBlank(certificatesFilter.getIssuer()))
            queryString.append(" AND lower(certificate.issuer) LIKE :issuer");
        if (isNotBlank(certificatesFilter.getSerialNumber()))
            queryString.append(" AND serialNumber=:serialNumber");
        if (certificatesFilter.getCertificateType()!=null) {
            queryString.append(" AND certificate.certificateType=:certificateType");
        }
        if (certificatesFilter.getValidityStartFrom() != null)
            queryString.append(" AND certificate.validityStart >= :validityStartFrom");
        if (certificatesFilter.getValidityStartTo() != null)
            queryString.append(" AND certificate.validityStart <= :validityStartTo");
        if (certificatesFilter.getValidityEndFrom() != null)
            queryString.append(" AND certificate.validityEnd >= :validityEndFrom");
        if (certificatesFilter.getValidityEndTo() != null)
            queryString.append(" AND certificate.validityEnd <= :validityEndTo");
    }

    private void addQueryParameters(String userRrn, CertificatesFilter certificatesFilter, Query query) {
        query.setParameter("nationalRegisterNumber", userRrn);
        query.setParameter("registrationStatus", RegistrationStatus.APPROVED);
        if (certificatesFilter.getCertificateDomainId() != null)
            query.setParameter("certificateDomainId", certificatesFilter.getCertificateDomainId());
        if (isNotBlank(certificatesFilter.getDistinguishedName()))
            query.setParameter("distinguishedName", certificatesFilter.getDistinguishedName());
        if (isNotBlank(certificatesFilter.getIssuer()))
            query.setParameter("issuer", certificatesFilter.getIssuer());
        if (isNotBlank(certificatesFilter.getSerialNumber())) {
            BigInteger serialNumber;
            try {
                serialNumber = new BigInteger(certificatesFilter.getSerialNumber());
            } catch (NumberFormatException e) {
                serialNumber = BigInteger.ZERO;
            }
            query.setParameter("serialNumber", serialNumber);
        }
        if (certificatesFilter.getCertificateType()!=null) {
            query.setParameter("certificateType", certificatesFilter.getCertificateType());
        }
        if (certificatesFilter.getValidityStartFrom() != null)
            query.setParameter("validityStartFrom", certificatesFilter.getValidityStartFrom());
        if (certificatesFilter.getValidityStartTo() != null)
            query.setParameter("validityStartTo", certificatesFilter.getValidityStartTo());
        if (certificatesFilter.getValidityEndFrom() != null)
            query.setParameter("validityEndFrom", certificatesFilter.getValidityEndFrom());
        if (certificatesFilter.getValidityEndTo() != null)
            query.setParameter("validityEndTo", certificatesFilter.getValidityEndTo());
    }

    private void appendOrderBy(Ordering ordering, StringBuilder queryString) {
        if (ordering!=null && ordering.getOrderByField() != null)
            queryString.append(" ORDER BY certificate.").append(ordering.getOrderByField()).append(ordering.isOrderByAscending() ? " ASC" : " DESC");
        else
            queryString.append(" ORDER BY certificate.validityStart DESC");
    }

    private void setPaging(Paging paging, Query query) {
        if (paging!=null && paging.getFirstRow() != null && paging.getEndRow() != null)
            query.setFirstResult(paging.getFirstRow()).setMaxResults(paging.getEndRow() - paging.getFirstRow());
    }
}
