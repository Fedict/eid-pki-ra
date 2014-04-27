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

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.FilterField;
import org.richfaces.model.Modifiable;
import org.richfaces.model.SortField2;

import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.common.util.StringShortener;
import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.ContractTypeWS;
import be.fedict.eid.pkira.generated.privatews.ContractsFilterWS;
import be.fedict.eid.pkira.generated.privatews.OrderingWS;
import be.fedict.eid.pkira.generated.privatews.PagingWS;
import be.fedict.eid.pkira.generated.privatews.SortOrderWS;
import be.fedict.eid.pkira.portal.certificate.CertificateType;
import be.fedict.eid.pkira.portal.certificatedomain.CertificateDomainWSHome;
import be.fedict.eid.pkira.portal.util.TypeMapper;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

@Name(ContractDataModel.NAME)
@Scope(ScopeType.SESSION)
public class ContractDataModel extends ExtendedTableDataModel<Contract> implements Modifiable {

    private class ContractDataProvider implements DataProvider<Contract> {
        private int rowCount = -1;
        private List<Contract> contracts = null;
        private int firstRowOfData;
        private int endRowOfData;

        @Override
        public int getRowCount() {
            if (rowCount == -1)
                rowCount = privateServiceClient.countContracts(credentials.getUser().getRRN(), buildContractsFilter());

            return rowCount;
        }

        @Override
        public List<Contract> getItemsByRange(int firstRow, int endRow) {
            if (contracts==null || firstRow!=firstRowOfData || endRow!=endRowOfData) {
                contracts = typeMapper.map(privateServiceClient.findContracts(credentials.getUser().getRRN(), buildContractsFilter(), buildOrdering(), buildPaging(firstRow, endRow)));
                firstRowOfData = firstRow;
                endRowOfData = endRow;
            }

            return contracts;
        }

        @Override
        public Contract getItemByKey(Object key) {
            Integer theKey = (Integer) key;
            if (theKey==null) return null;

            if (contracts!=null)
                for (Contract contract : contracts)
                    if (ObjectUtils.equals(contract.getId(), theKey)) return contract;

            return typeMapper.map(privateServiceClient.findContract((Integer) key));
        }

        @Override
        public Object getKey(Contract item) {
            return item.getId();
        }

        public void invalidateFilter() {
            rowCount = -1;
            contracts = null;

            pageNumber = 1;
        }

        public void invalidateOrdering() {
            contracts = null;
        }

        private ContractsFilterWS buildContractsFilter() {
            ContractsFilterWS result = new ContractsFilterWS();
            result.setContractType(typeMapper.getEnum(ContractTypeWS.class, contractTypeFilter));
            result.setRequesterName(blankAsNull(requesterNameFilter));
            result.setCertificateType(typeMapper.getEnum(CertificateTypeWS.class, certificateTypeFilter));
            result.setDnExpression(blankAsNull(dnExpressionFilter));
            result.setCreationDateFrom(typeMapper.map(creationDateFromFilter));
            result.setCreationDateTo(typeMapper.map(creationDateToFilter));
            result.setResultMessage(blankAsNull(resultMessageFilter));
            result.setCertificateDomainId(certificateDomainId);

            return result;
        }

        private OrderingWS buildOrdering() {
            OrderingWS result = new OrderingWS();
            result.setOrder(sortOrder);
            result.setField(sortField);
            return result;
        }

        private PagingWS buildPaging(int firstRow, int endRow) {
            PagingWS paging = new PagingWS();
            paging.setFirstRow(firstRow);
            paging.setEndRow(endRow);
            return paging;
        }

        private String blankAsNull(String s) {
            return StringUtils.isBlank(s) ? null : s;
        }
    }

    public static final String NAME = "be.fedict.eid.pkira.portal.contractDataModel";

    @In(EIdUserCredentials.NAME)
    private EIdUserCredentials credentials;

    @In(value = EIDPKIRAPrivateServiceClient.NAME, create = true)
    private EIDPKIRAPrivateServiceClient privateServiceClient;

    @In(value = TypeMapper.NAME, create = true)
    private TypeMapper typeMapper;

    @In(value=CertificateDomainWSHome.NAME, create = true)
    private CertificateDomainWSHome certificateDomainWSHome;

    private Integer certificateDomainId;
    private String contractTypeFilter;
    private String requesterNameFilter="";
    private String certificateTypeFilter;
    private String dnExpressionFilter="";
    private String resultMessageFilter="";
    private Date creationDateFromFilter, creationDateToFilter;

    private String sortField;
    private SortOrderWS sortOrder;

    private int pageNumber;

    public ContractDataModel() {
        super(null);
        super.setDataProvider(new ContractDataProvider());
    }

    @Override
    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) throws IOException {
        SequenceRange sequenceRange = (SequenceRange) range;

        int totalRowCount = getDataProvider().getRowCount();
        int firstRow = sequenceRange.getFirstRow();
        int numberOfRows = sequenceRange.getRows();
        if (numberOfRows <= 0) numberOfRows = totalRowCount;


        int endRow = firstRow + numberOfRows;
        if (endRow > totalRowCount) endRow = totalRowCount;

        for (Contract item : getDataProvider().getItemsByRange(firstRow, endRow)) {
            visitor.process(context, getKey(item), argument);
        }
    }

    @Override
    public int getRowCount() {
        return getDataProvider().getRowCount();
    }

    @Override
    public void modify(List<FilterField> filterFields, List<SortField2> sortFields) {
        // do nothing, since filtering and sorting is handled externally
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void sortBy(String sortField) {
        if (StringUtils.equals(sortField, this.sortField)) {
            sortOrder = sortOrder==SortOrderWS.ASC ? SortOrderWS.DESC : SortOrderWS.ASC;
        } else {
            this.sortField = sortField;
            this.sortOrder = SortOrderWS.ASC;
        }

        getDataProvider().invalidateOrdering();
    }

    public String getSortIcon(String sortField) {
        String icon = "none";
        if (StringUtils.equals(sortField, this.sortField)) {
            if (sortOrder == SortOrderWS.ASC)
                icon = "asc";
            else
                icon = "desc";
        }

        return "../../img/sort_icon_" + icon + ".gif";
    }

    public ContractDataProvider getDataProvider() {
        return (ContractDataProvider) super.getDataProvider();
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

    public Integer getCertificateDomainId() {
        return certificateDomainId;
    }

    public void setCertificateDomainId(Integer certificateDomainId) {
        if (!ObjectUtils.equals(certificateDomainId, this.certificateDomainId)) {
            this.certificateDomainId = certificateDomainId;
            getDataProvider().invalidateFilter();
        }
    }

    public CertificateDomainWSHome getCertificateDomainWSHome() {
        if (certificateDomainId != null) {
            certificateDomainWSHome.setId(certificateDomainId);
        }
        return certificateDomainWSHome;
    }

    public String getCertificateDomainExpressionShortened() {
        return StringShortener.shorten(getCertificateDomainExpression(), 120);
    }

    public String getCertificateDomainExpression() {
        return getCertificateDomainWSHome().getInstance().getDnExpression();
    }

}

