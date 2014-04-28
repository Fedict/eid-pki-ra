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

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainHome;
import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationStatus;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * @author Bram Baeyens
 */
@Name(ContractQuery.NAME)
public class ContractQuery extends EntityQuery<AbstractContract> {

    private static final long serialVersionUID = -1687988818281539366L;

    public static final String NAME = "be.fedict.eid.pkira.blm.contractQuery";

    private Registration registration;

    @In(value = CertificateDomainHome.NAME, create = true)
    private CertificateDomainHome certificateDomainHome;

    @Override
    public String getEjbql() {
        return "select contract from AbstractContract contract";
    }

    @SuppressWarnings("unchecked")
    public List<AbstractContract> findContractsByFilter(String userRrn, ContractsFilter contractsFilter, Ordering ordering, Paging paging) {
        StringBuilder queryString = new StringBuilder();

        queryString.append("SELECT contract FROM ");
        appendFromAndWhere(contractsFilter, queryString);
        appendOrderBy(ordering, queryString);

        Query query = getEntityManager().createQuery(queryString.toString());
        addQueryParameters(userRrn, contractsFilter, query);
        setPaging(paging, query);

        return query.getResultList();
    }

    public int countContractsByFilter(String userRrn, ContractsFilter contractsFilter) {
        StringBuilder queryString = new StringBuilder();

        queryString.append("SELECT COUNT(contract) FROM ");
        appendFromAndWhere(contractsFilter, queryString);

        Query query = getEntityManager().createQuery(queryString.toString());
        addQueryParameters(userRrn, contractsFilter, query);

        return ((Long) query.getSingleResult()).intValue();
    }

    public AbstractContract findContractById(int contractId) {
        return getEntityManager().find(AbstractContract.class, contractId);
    }

    @SuppressWarnings("unchecked")
    public List<AbstractContract> getFindContracts(Integer certificateDomainId) {
        if (certificateDomainId == null) {
            return Collections.emptyList();
        }

        return getEntityManager().createNamedQuery(AbstractContract.NQ_FIND_CONTRACTS_BY_CERTIFICATE_DOMAIN_ID)
                .setParameter("certificateDomainId", certificateDomainId)
                .getResultList();
    }

    private void appendFromAndWhere(ContractsFilter contractsFilter, StringBuilder queryString) {
        if (contractsFilter.isIncludeRequestContracts() && !contractsFilter.isIncludeRevocationContracts())
            queryString.append("CertificateSigningContract");
        else if (!contractsFilter.isIncludeRequestContracts() && contractsFilter.isIncludeRevocationContracts())
            queryString.append("CertificateRevocationContract");
        else
            queryString.append("AbstractContract");
        queryString.append(" contract, Registration registration WHERE registration.requester.nationalRegisterNumber = :nationalRegisterNumber AND registration.status = :registrationStatus AND registration.certificateDomain = contract.certificateDomain ");

        if (contractsFilter.getCertificateDomainId() != null)
            queryString.append(" AND contract.certificateDomain.id = :certificateDomainId");
        if ((contractsFilter.isIncludeRequestContracts() || !contractsFilter.isIncludeRevocationContracts()) && contractsFilter.getCertificateType() != null)
            queryString.append(" AND contract.certificateType = :certificateType");
        if (isNotBlank(contractsFilter.getRequesterName()))
            queryString.append(" AND lower(contract.requesterName) LIKE :requesterName");
        if (isNotBlank(contractsFilter.getDnExpression()))
            queryString.append(" AND lower(contract.subject) LIKE :dnExpression");
        if (contractsFilter.getCreationDateFrom() != null)
            queryString.append(" AND contract.creationDate >= :creationDateFrom");
        if (contractsFilter.getCreationDateTo() != null)
            queryString.append(" AND contract.creationDate <= :creationDateTo");
        if (isNotBlank(contractsFilter.getResultMessage()))
            queryString.append(" AND lower(contract.resultMessage) like :resultMessage");
    }

    private void addQueryParameters(String userRrn, ContractsFilter contractsFilter, Query query) {
        query.setParameter("nationalRegisterNumber", userRrn);
        query.setParameter("registrationStatus", RegistrationStatus.APPROVED);
        if (contractsFilter.getCertificateDomainId() != null)
            query.setParameter("certificateDomainId", contractsFilter.getCertificateDomainId());
        if ((contractsFilter.isIncludeRequestContracts() || !contractsFilter.isIncludeRevocationContracts()) && contractsFilter.getCertificateType() != null)
            query.setParameter("certificateType", contractsFilter.getCertificateType());
        if (isNotBlank(contractsFilter.getRequesterName()))
            query.setParameter("requesterName", "%" + contractsFilter.getRequesterName().toLowerCase() + "%");
        if (isNotBlank(contractsFilter.getDnExpression()))
            query.setParameter("dnExpression", "%" + contractsFilter.getDnExpression().toLowerCase() + "%");
        if (contractsFilter.getCreationDateFrom() != null)
            query.setParameter("creationDateFrom", contractsFilter.getCreationDateFrom());
        if (contractsFilter.getCreationDateTo() != null)
            query.setParameter("creationDateTo", contractsFilter.getCreationDateTo());
        if (isNotBlank(contractsFilter.getResultMessage()))
            query.setParameter("resultMessage", "%" + contractsFilter.getResultMessage().toLowerCase() + "%");
    }

    private void appendOrderBy(Ordering ordering, StringBuilder queryString) {
        if (ordering!=null && ordering.getOrderByField() != null)
            queryString.append(" ORDER BY contract.").append(ordering.getOrderByField()).append(ordering.isOrderByAscending() ? " ASC" : " DESC");
        else
            queryString.append(" ORDER BY contract.creationDate DESC");
    }

    private void setPaging(Paging paging, Query query) {
        if (paging!=null && paging.getFirstRow() != null && paging.getEndRow() != null)
            query.setFirstResult(paging.getFirstRow()).setMaxResults(paging.getEndRow() - paging.getFirstRow());
    }

    public CertificateDomainHome getCertificateDomainHome() {
        return certificateDomainHome;
    }

}
