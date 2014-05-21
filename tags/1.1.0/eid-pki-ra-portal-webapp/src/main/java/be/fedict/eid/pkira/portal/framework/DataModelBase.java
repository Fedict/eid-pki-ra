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

package be.fedict.eid.pkira.portal.framework;

import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.In;
import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.FilterField;
import org.richfaces.model.Modifiable;
import org.richfaces.model.SortField2;

import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.common.util.StringShortener;
import be.fedict.eid.pkira.generated.privatews.SortOrderWS;
import be.fedict.eid.pkira.portal.certificatedomain.CertificateDomainWSHome;
import be.fedict.eid.pkira.portal.util.TypeMapper;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

public abstract class DataModelBase<T> extends ExtendedTableDataModel<T> implements Modifiable {

    protected static final int DEFAULT_MAX_PAGES = 10;
    protected static final int DEFAULT_FAST_STEP = 5;
    private static final int DEFAULT_ROWS = 10;

    @In(EIdUserCredentials.NAME)
    protected EIdUserCredentials credentials;

    @In(value = EIDPKIRAPrivateServiceClient.NAME, create = true)
    protected EIDPKIRAPrivateServiceClient privateServiceClient;

    @In(value = TypeMapper.NAME, create = true)
    protected TypeMapper typeMapper;

    protected String sortField;
    protected SortOrderWS sortOrder;
    protected Integer certificateDomainId;
    @In(value= CertificateDomainWSHome.NAME, create = true)
    private CertificateDomainWSHome certificateDomainWSHome;

    public DataModelBase() {
        super(null);
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

        for (T item : getDataProvider().getItemsByRange(firstRow, endRow)) {
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

    public void sortBy(String sortField) {
        if (StringUtils.equals(sortField, this.sortField)) {
            sortOrder = sortOrder== SortOrderWS.ASC ? SortOrderWS.DESC : SortOrderWS.ASC;
        } else {
            this.sortField = sortField;
            this.sortOrder = SortOrderWS.ASC;
        }

        getDataProvider().invalidateOrder();
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

    public DataProviderBase<T> getDataProvider() {
        return (DataProviderBase<T>) super.getDataProvider();
    }

    public int getRows() {
        return DEFAULT_ROWS;
    }

    public int getMaxPages() {
        return DEFAULT_MAX_PAGES;
    }

    public int getFastStep() {
        return DEFAULT_FAST_STEP;
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

    public String getCertificateDomainExpressionShortened() {
        return StringShortener.shorten(getCertificateDomainExpression(), 120);
    }

    public String getCertificateDomainExpression() {
        if (certificateDomainId != null) {
            certificateDomainWSHome.setId(certificateDomainId);
        }
        return certificateDomainWSHome.getInstance().getDnExpression();
    }
}
