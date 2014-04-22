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

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.portal.util.AbstractCsvCreatorBean;

@Name(CertificateExportHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class CertificateExportHandlerBean extends AbstractCsvCreatorBean implements CertificateExportHandler {

    @In(value = CertificateHandler.NAME, create = true)
    private CertificateHandler certificateHandler;

    @Override
    public void exportToCSV() {
        List<Certificate> certificates = certificateHandler.findCertificateList();
        CsvBuilder csv = new CsvBuilder();

        // add column headers
        csv
            .append(messages.get("certificate.serialNumber"))
            .append(messages.get("certificate.validityStart"))
            .append(messages.get("certificate.validityEnd"))
            .append(messages.get("certificate.issuer"))
            .append(messages.get("certificate.type"))
            .append(messages.get("certificate.dn"))
            .nextRow();

        // add rows
        for (Certificate certificate : certificates) {
            csv
                .append(certificate.getSerialNumber())
                .append(certificate.getValidityStart())
                .append(certificate.getValidityEnd())
                .append(certificate.getIssuer())
                .append(translateCertificateType(certificate.getType()))
                .append(certificate.getDistinguishedName())
                .nextRow();
        }

        csv.download("pkira-certificate-list.csv", "text/csv");
    }

}
