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

import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.richfaces.model.DataProvider;

import be.fedict.eid.pkira.generated.privatews.OrderingWS;
import be.fedict.eid.pkira.generated.privatews.PagingWS;
import be.fedict.eid.pkira.generated.privatews.SortOrderWS;

public abstract class DataProviderBase<T> implements DataProvider<T> {
    private int rowCount = -1;
    private List<T> data = null;
    private int firstRowOfData;
    private int endRowOfData;

    @Override
    public int getRowCount() {
        if (rowCount == -1) rowCount = fetchRowCount();
        return rowCount;
    }

    @Override
    public List<T> getItemsByRange(int firstRow, int endRow) {
        if (data ==null || firstRow!=firstRowOfData || endRow!=endRowOfData) {
            data = fetchData(firstRow, endRow);
            firstRowOfData = firstRow;
            endRowOfData = endRow;
        }

        return data;
    }

    @Override
    public T getItemByKey(Object key) {
        Integer theKey = (Integer) key;
        if (theKey==null) return null;

        if (data !=null) {
            for (T item : data) {
                if (ObjectUtils.equals(getKey(item), theKey)) return item;
            }
        }

        return fetchItem((Integer) key);
    }



    public void invalidateFilter() {
        rowCount = -1;
        data = null;
    }

    public void invalidateOrder() {
        data = null;
    }

    protected PagingWS buildPaging(int firstRow, int endRow) {
        PagingWS paging = new PagingWS();
        paging.setFirstRow(firstRow);
        paging.setEndRow(endRow);
        return paging;
    }

    protected OrderingWS buildOrdering(String sortField, SortOrderWS sortOrder) {
        OrderingWS result = new OrderingWS();
        result.setOrder(sortOrder);
        result.setField(sortField);
        return result;
    }

    public abstract Integer getKey(T item);
    protected abstract T fetchItem(Integer key);
    protected abstract List<T> fetchData(int firstRow, int endRow);
    protected abstract int fetchRowCount();
}
