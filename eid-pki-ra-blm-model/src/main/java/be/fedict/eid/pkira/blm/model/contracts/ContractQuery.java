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
import java.util.Date;
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
    public List<AbstractContract> findContractsByFilter(String userRrn, ContractsFilter contractsFilter,
                                                        String orderByField, boolean orderByAscending,
                                                        Integer firstRow, Integer endRow) {
        StringBuilder queryString = new StringBuilder();

        queryString.append("SELECT contract FROM ");
        appendFromAndWhere(contractsFilter, queryString);
        appendOrderBy(orderByField, orderByAscending, queryString);

        Query query = getEntityManager().createQuery(queryString.toString());
        addQueryParameters(userRrn, contractsFilter, query);
        setPaging(firstRow, endRow, query);

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
            queryString.append(" AND lower(contract.requester) LIKE :requesterName");
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
        if ((contractsFilter.includeRequestContracts || !contractsFilter.includeRevocationContracts) && contractsFilter.getCertificateType() != null)
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

    private void appendOrderBy(String orderByField, boolean orderByAscending, StringBuilder queryString) {
        if (orderByField != null)
            queryString.append(" ORDER BY contract.").append(orderByField).append(orderByAscending ? " ASC" : " DESC");
        else
            queryString.append(" ORDER BY contract.creationDate DESC");
    }

    private void setPaging(Integer firstRow, Integer endRow, Query query) {
        if (firstRow != null && endRow != null)
            query.setFirstResult(firstRow).setMaxResults(endRow - firstRow);
    }

    public CertificateDomainHome getCertificateDomainHome() {
        return certificateDomainHome;
    }

    public static class ContractsFilter {
        private boolean includeRequestContracts;
        private boolean includeRevocationContracts;
        private Integer certificateDomainId;
        private CertificateType certificateType;
        private String requesterName;
        private String dnExpression;
        private Date creationDateFrom;
        private Date creationDateTo;
        private String resultMessage;

        public boolean isIncludeRequestContracts() {
            return includeRequestContracts;
        }

        public boolean isIncludeRevocationContracts() {
            return includeRevocationContracts;
        }

        public Integer getCertificateDomainId() {
            return certificateDomainId;
        }

        public CertificateType getCertificateType() {
            return certificateType;
        }

        public String getRequesterName() {
            return requesterName;
        }

        public String getDnExpression() {
            return dnExpression;
        }

        public Date getCreationDateFrom() {
            return creationDateFrom;
        }

        public Date getCreationDateTo() {
            return creationDateTo;
        }

        public String getResultMessage() {
            return resultMessage;
        }

        public void setIncludeRequestContracts(boolean includeRequestContracts) {
            this.includeRequestContracts = includeRequestContracts;
        }

        public void setIncludeRevocationContracts(boolean includeRevocationContracts) {
            this.includeRevocationContracts = includeRevocationContracts;
        }

        public void setCertificateDomainId(Integer certificateDomainId) {
            this.certificateDomainId = certificateDomainId;
        }

        public void setCertificateType(CertificateType certificateType) {
            this.certificateType = certificateType;
        }

        public void setRequesterName(String requesterName) {
            this.requesterName = requesterName;
        }

        public void setDnExpression(String dnExpression) {
            this.dnExpression = dnExpression;
        }

        public void setCreationDateFrom(Date creationDateFrom) {
            this.creationDateFrom = creationDateFrom;
        }

        public void setCreationDateTo(Date creationDateTo) {
            this.creationDateTo = creationDateTo;
        }

        public void setResultMessage(String resultMessage) {
            this.resultMessage = resultMessage;
        }
    }
}
