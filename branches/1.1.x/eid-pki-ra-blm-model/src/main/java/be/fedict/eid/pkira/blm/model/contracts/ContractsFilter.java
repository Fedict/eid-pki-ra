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

public class ContractsFilter {
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
