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

package be.fedict.eid.pkira.portal.certificate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.CertificatesFilterWS;
import be.fedict.eid.pkira.portal.framework.DataModelBase;
import be.fedict.eid.pkira.portal.framework.DataProviderBase;

import static be.fedict.eid.pkira.portal.util.ExtraStringUtil.blankAsNull;

@Name(CertificateDataModel.NAME)
@Scope(ScopeType.SESSION)
public class CertificateDataModel extends DataModelBase<Certificate> {

    private class CertificateDataProvider extends DataProviderBase<Certificate> {
        @Override
        protected int fetchRowCount() {
            return privateServiceClient.countCertificates(credentials.getUser().getRRN(), buildCertificatesFilter());
        }

        @Override
        protected List<Certificate> fetchData(int firstRow, int endRow) {
            return typeMapper.mapCertificates(privateServiceClient.findCertificates(
                    credentials.getUser().getRRN(),
                    buildCertificatesFilter(),
                    buildOrdering(sortField, sortOrder),
                    buildPaging(firstRow, endRow)));
        }

        @Override
        protected Certificate fetchItem(Integer key) {
            return typeMapper.map(privateServiceClient.findCertificate(key));
        }

        @Override
        public Integer getKey(Certificate item) {
            return item.getId();
        }

        private CertificatesFilterWS buildCertificatesFilter() {
            CertificatesFilterWS result = new CertificatesFilterWS();
            result.setCertificateDomainId(getCertificateDomainId());
            result.setCertificateType(typeMapper.getEnum(CertificateTypeWS.class, certificateTypeFilter));
            result.setDistinguishedName(blankAsNull(distinguishedNameFilter));
            result.setValidityStartFrom(typeMapper.map(validityStartFromFilter));
            result.setValidityStartTo(typeMapper.map(validityStartToFilter));
            result.setValidityEndFrom(typeMapper.map(validityEndFromFilter));
            result.setValidityEndTo(typeMapper.map(validityEndToFilter));
            result.setIssuer(blankAsNull(issuerFilter));
            result.setSerialNumber(blankAsNull(serialNumberFilter));

            return result;
        }
    }

    public static final String NAME = "be.fedict.eid.pkira.portal.certificateDataModel";

    private String serialNumberFilter = "";
    private String certificateTypeFilter;
    private String distinguishedNameFilter ="";
    private String issuerFilter="";
    private Date validityStartFromFilter, validityStartToFilter, validityEndFromFilter, validityEndToFilter;

    public CertificateDataModel() {
        super.setDataProvider(new CertificateDataProvider());
    }

    public String getSerialNumberFilter() {
        return serialNumberFilter;
    }

    public void setSerialNumberFilter(String serialNumberFilter) {
        if (!StringUtils.equals(this.serialNumberFilter, serialNumberFilter)) {
            this.serialNumberFilter = serialNumberFilter;
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

    public String getDistinguishedNameFilter() {
        return distinguishedNameFilter;
    }

    public void setDistinguishedNameFilter(String distinguishedNameFilter) {
        if (!StringUtils.equals(this.distinguishedNameFilter, distinguishedNameFilter)) {
            this.distinguishedNameFilter = distinguishedNameFilter;
            getDataProvider().invalidateFilter();
        }
    }

    public String getIssuerFilter() {
        return issuerFilter;
    }

    public void setIssuerFilter(String issuerFilter) {
        if (!StringUtils.equals(this.issuerFilter, issuerFilter)) {
            this.issuerFilter = issuerFilter;
            getDataProvider().invalidateFilter();
        }
    }

    public Date getValidityStartFromFilter() {
        return validityStartFromFilter;
    }

    public void setValidityStartFromFilter(Date validityStartFromFilter) {
        if (!ObjectUtils.equals(this.validityStartFromFilter, validityStartFromFilter)) {
            this.validityStartFromFilter = validityStartFromFilter;
            getDataProvider().invalidateFilter();
        }
    }

    public Date getValidityStartToFilter() {
        return validityStartToFilter;
    }

    public void setValidityStartToFilter(Date validityStartToFilter) {
        if (!ObjectUtils.equals(this.validityStartToFilter, validityStartToFilter)) {
            this.validityStartToFilter = validityStartToFilter;
            getDataProvider().invalidateFilter();
        }
    }

    public Date getValidityEndFromFilter() {
        return validityEndFromFilter;
    }

    public void setValidityEndFromFilter(Date validityEndFromFilter) {
        if (!ObjectUtils.equals(this.validityEndFromFilter, validityEndFromFilter)) {
            this.validityEndFromFilter = validityEndFromFilter;
            getDataProvider().invalidateFilter();
        }
    }

    public Date getValidityEndToFilter() {
        return validityEndToFilter;
    }

    public void setValidityEndToFilter(Date validityEndToFilter) {
        if (!ObjectUtils.equals(this.validityEndToFilter, validityEndToFilter)) {
            this.validityEndToFilter = validityEndToFilter;
            getDataProvider().invalidateFilter();
        }
    }

    public List<CertificateType> getCertificateTypes() {
        return Arrays.asList(CertificateType.values());
    }
}

