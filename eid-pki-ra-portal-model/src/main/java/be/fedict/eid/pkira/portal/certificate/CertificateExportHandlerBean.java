
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.common.download.Document;
import be.fedict.eid.pkira.common.download.DownloadManager;
import be.fedict.eid.pkira.portal.util.ConfigurationEntryContainer;

@Name(CertificateExportHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class CertificateExportHandlerBean implements CertificateExportHandler {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final String LINE_SEPARATOR = "\r\n";

    @In(value = CertificateHandler.NAME, create = true)
    private CertificateHandler certificateHandler;

    @In(value = ConfigurationEntryContainer.NAME, create=true)
    private ConfigurationEntryContainer configurationEntryContainer;

    @In(value= DownloadManager.NAME, create = true)
    private DownloadManager downloadManager;

    @In private Map<String, String> messages;

    @Override
    public void exportToCSV() {
        List<Certificate> certificates = certificateHandler.findCertificateList();

        StringBuilder csv = new StringBuilder();
        String fieldSeparator = configurationEntryContainer.getCsvExportFieldSeparator();

        // add column headers
        csv
            .append(messages.get("certificate.serialNumber")).append(fieldSeparator)
            .append(messages.get("certificate.validityStart")).append(fieldSeparator)
            .append(messages.get("certificate.validityEnd")).append(fieldSeparator)
            .append(messages.get("certificate.issuer")).append(fieldSeparator)
            .append(messages.get("certificate.type")).append(fieldSeparator)
            .append(messages.get("certificate.dn")).append(LINE_SEPARATOR);

        // add rows
        for (Certificate certificate : certificates) {
            appendField(csv, certificate.getSerialNumber());
            csv.append(fieldSeparator);

            appendField(csv, certificate.getValidityStart());
            csv.append(fieldSeparator);

            appendField(csv, certificate.getValidityEnd());
            csv.append(fieldSeparator);

            appendField(csv, certificate.getIssuer());
            csv.append(fieldSeparator);

            appendField(csv, messages.get(certificate.getType().getMessageKey()));
            csv.append(fieldSeparator);

            appendField(csv, certificate.getDistinguishedName());
            csv.append(LINE_SEPARATOR);
        }

        downloadManager.download(new Document("pkira-certificate-list.csv", "text/csv", csv.toString().getBytes()));
    }

    private void appendField(StringBuilder csv, String value) {
        if (!StringUtils.isEmpty(value)) {
            csv.append(StringEscapeUtils.escapeCsv(value));
        }
    }

    private void appendField(StringBuilder csv, Date value) {
        if (value!=null) {
            appendField(csv, DATE_FORMAT.format(value));
        }
    }
}
