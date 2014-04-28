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

package be.fedict.eid.pkira.portal.contract;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.ContractTypeWS;
import be.fedict.eid.pkira.generated.privatews.ContractsFilterWS;
import be.fedict.eid.pkira.portal.certificate.CertificateType;
import be.fedict.eid.pkira.portal.framework.DataModelBase;
import be.fedict.eid.pkira.portal.framework.DataProviderBase;
import be.fedict.eid.pkira.portal.util.ExtraStringUtil;

@Name(ContractDataModel.NAME)
@Scope(ScopeType.SESSION)
public class ContractDataModel extends DataModelBase<Contract> {

    private class ContractDataProvider extends DataProviderBase<Contract> {
        @Override
        protected int fetchRowCount() {
            return privateServiceClient.countContracts(credentials.getUser().getRRN(), buildContractsFilter());
        }

        @Override
        protected List<Contract> fetchData(int firstRow, int endRow) {
            return typeMapper.mapContracts(privateServiceClient.findContracts(
                    credentials.getUser().getRRN(),
                    buildContractsFilter(),
                    buildOrdering(sortField, sortOrder),
                    buildPaging(firstRow, endRow)));
        }

        @Override
        protected Contract fetchItem(Integer key) {
            return typeMapper.map(privateServiceClient.findContract(key));
        }

        @Override
        public Integer getKey(Contract item) {
            return item.getId();
        }

        private ContractsFilterWS buildContractsFilter() {
            ContractsFilterWS result = new ContractsFilterWS();
            result.setContractType(typeMapper.getEnum(ContractTypeWS.class, contractTypeFilter));
            result.setRequesterName(ExtraStringUtil.blankAsNull(requesterNameFilter));
            result.setCertificateType(typeMapper.getEnum(CertificateTypeWS.class, certificateTypeFilter));
            result.setDnExpression(ExtraStringUtil.blankAsNull(dnExpressionFilter));
            result.setCreationDateFrom(typeMapper.map(creationDateFromFilter));
            result.setCreationDateTo(typeMapper.map(creationDateToFilter));
            result.setResultMessage(ExtraStringUtil.blankAsNull(resultMessageFilter));
            result.setCertificateDomainId(getCertificateDomainId());

            return result;
        }
    }

    public static final String NAME = "be.fedict.eid.pkira.portal.contractDataModel";

    private String contractTypeFilter;
    private String requesterNameFilter="";
    private String certificateTypeFilter;
    private String dnExpressionFilter="";
    private String resultMessageFilter="";
    private Date creationDateFromFilter, creationDateToFilter;

    private int pageNumber;

    public ContractDataModel() {
        super.setDataProvider(new ContractDataProvider());
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getDnExpressionFilter() {
        return dnExpressionFilter;
    }

    public void setDnExpressionFilter(String dnExpressionFilter) {
        if (!StringUtils.equals(this.dnExpressionFilter, dnExpressionFilter)) {
            this.dnExpressionFilter = dnExpressionFilter;
            getDataProvider().invalidateFilter();
        }
    }

    public String getContractTypeFilter() {
        return contractTypeFilter;
    }

    public void setContractTypeFilter(String contractTypeFilter) {
        if (!StringUtils.equals(this.contractTypeFilter, contractTypeFilter)) {
            this.contractTypeFilter = contractTypeFilter;
            getDataProvider().invalidateFilter();
        }
    }

    public List<ContractType> getContractTypes() {
        return Arrays.asList(ContractType.values());
    }

    public String getRequesterNameFilter() {
        return requesterNameFilter;
    }

    public void setRequesterNameFilter(String requesterNameFilter) {
        if (!StringUtils.equals(this.requesterNameFilter, requesterNameFilter)) {
            this.requesterNameFilter = requesterNameFilter;
            getDataProvider().invalidateFilter();
        }
    }

    public String getCertificateTypeFilter() {
        return certificateTypeFilter;
    }

    public void setCertificateTypeFilter(String certificateTypeFilter) {
        if (!StringUtils.equals(this.certificateTypeFilter, certificateTypeFilter)) {
            this.certificateTypeFilter = certificateTypeFilter;
            getDataProvider().invalidateFilter();
        }
    }

    public List<CertificateType> getCertificateTypes() {
        return Arrays.asList(CertificateType.values());
    }

    public Date getCreationDateFromFilter() {
        return creationDateFromFilter;
    }

    public void setCreationDateFromFilter(Date creationDateFromFilter) {
        if (!ObjectUtils.equals(this.creationDateFromFilter, creationDateFromFilter)) {
            this.creationDateFromFilter = creationDateFromFilter;
            getDataProvider().invalidateFilter();
        }
    }

    public Date getCreationDateToFilter() {
        return creationDateToFilter;
    }

    public void setCreationDateToFilter(Date creationDateToFilter) {
        if (!ObjectUtils.equals(this.creationDateToFilter, creationDateToFilter)) {
            this.creationDateToFilter = creationDateToFilter;
            getDataProvider().invalidateFilter();
        }
    }

    public String getResultMessageFilter() {
        return resultMessageFilter;
    }

    public void setResultMessageFilter(String resultMessageFilter) {
        if (!StringUtils.equals(this.resultMessageFilter, resultMessageFilter)) {
            this.resultMessageFilter = resultMessageFilter;
            getDataProvider().invalidateFilter();
        }
    }

}

