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

import java.util.Date;

public class CertificatesFilter {
    protected Integer certificateDomainId;
    protected String distinguishedName;
    protected String issuer;
    protected String requesterName;
    protected String serialNumber;
    protected CertificateType certificateType;
    protected Date validityStartFrom;
    protected Date validityStartTo;
    protected Date validityEndFrom;
    protected Date validityEndTo;

    public Integer getCertificateDomainId() {
        return certificateDomainId;
    }

    public void setCertificateDomainId(Integer certificateDomainId) {
        this.certificateDomainId = certificateDomainId;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public Date getValidityStartFrom() {
        return validityStartFrom;
    }

    public void setValidityStartFrom(Date validityStartFrom) {
        this.validityStartFrom = validityStartFrom;
    }

    public Date getValidityStartTo() {
        return validityStartTo;
    }

    public void setValidityStartTo(Date validityStartTo) {
        this.validityStartTo = validityStartTo;
    }

    public Date getValidityEndFrom() {
        return validityEndFrom;
    }

    public void setValidityEndFrom(Date validityEndFrom) {
        this.validityEndFrom = validityEndFrom;
    }

    public Date getValidityEndTo() {
        return validityEndTo;
    }

    public void setValidityEndTo(Date validityEndTo) {
        this.validityEndTo = validityEndTo;
    }
}
