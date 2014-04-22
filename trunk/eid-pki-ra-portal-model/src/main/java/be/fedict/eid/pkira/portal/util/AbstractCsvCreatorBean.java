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

package be.fedict.eid.pkira.portal.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.In;

import be.fedict.eid.pkira.common.download.Document;
import be.fedict.eid.pkira.common.download.DownloadManager;
import be.fedict.eid.pkira.portal.certificate.CertificateType;
import be.fedict.eid.pkira.portal.contract.ContractExportHandlerBean;
import be.fedict.eid.pkira.portal.contract.ContractType;

public abstract class AbstractCsvCreatorBean {

    public class CsvBuilder {

        private StringBuffer csv = new StringBuffer();
        private String fieldSeparator = configurationEntryContainer.getCsvExportFieldSeparator();

        public ContractExportHandlerBean.CsvBuilder append(String value) {
            if (!StringUtils.isEmpty(value)) csv.append(StringEscapeUtils.escapeCsv(value));
            csv.append(fieldSeparator);
            return this;
        }

        public ContractExportHandlerBean.CsvBuilder append(Date value) {
            if (value!=null) append(AbstractCsvCreatorBean.DATE_FORMAT.format(value));
            return this;
        }

        public CsvBuilder nextRow() {
            csv.append(LINE_SEPARATOR);
            return this;
        }

        public byte[] getBytes() {
            return csv.toString().getBytes();
        }

        public void download(String fileName, String contentType) {
            downloadManager.download(new Document(fileName, contentType, getBytes()));
        }
    }

    static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final String LINE_SEPARATOR = "\r\n";

    @In
    protected Map<String, String> messages;

    @In(value= DownloadManager.NAME, create = true)
    private DownloadManager downloadManager;

    @In(value = ConfigurationEntryContainer.NAME, create=true)
    private ConfigurationEntryContainer configurationEntryContainer;

    protected String translateCertificateType(CertificateType certificateType) {
        if (certificateType==null || certificateType.getMessageKey()==null) return null;
        return messages.get(certificateType.getMessageKey());
    }

    protected String translateContractType(ContractType contractType) {
        if (contractType==null || contractType.getMessageKey()==null) return null;
        return messages.get(contractType.getMessageKey());
    }
}
