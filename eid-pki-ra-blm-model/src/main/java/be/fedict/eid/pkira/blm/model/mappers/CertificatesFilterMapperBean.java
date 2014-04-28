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

package be.fedict.eid.pkira.blm.model.mappers;

import java.io.Serializable;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.contracts.CertificatesFilter;
import be.fedict.eid.pkira.generated.privatews.CertificatesFilterWS;

/**
 * @author Bram Baeyens
 *
 */
@Name(CertificatesFilterMapper.NAME)
@Scope(ScopeType.STATELESS)
public class CertificatesFilterMapperBean implements Serializable, CertificatesFilterMapper {

	private static final long serialVersionUID = 518966394376372668L;

    @Override
    public CertificatesFilter mapCertificatesFilter(CertificatesFilterWS certificatesFilterWS) {
        CertificatesFilter certificatesFilter = new CertificatesFilter();
        if (certificatesFilterWS !=null) {
            certificatesFilter.setCertificateDomainId(certificatesFilterWS.getCertificateDomainId());
            certificatesFilter.setCertificateType(CertificateType.valueOf(certificatesFilterWS.getCertificateType().name()));
            certificatesFilter.setRequesterName(certificatesFilterWS.getRequesterName());
            certificatesFilter.setIssuer(certificatesFilterWS.getIssuer());
            certificatesFilter.setDistinguishedName(certificatesFilterWS.getDistinguishedName());
            certificatesFilter.setSerialNumber(certificatesFilterWS.getSerialNumber());
            certificatesFilter.setValidityStartFrom(map(certificatesFilterWS.getValidityStartFrom()));
            certificatesFilter.setValidityStartTo(map(certificatesFilterWS.getValidityStartTo()));
            certificatesFilter.setValidityEndFrom(map(certificatesFilterWS.getValidityEndFrom()));
            certificatesFilter.setValidityEndTo(map(certificatesFilterWS.getValidityEndTo()));
        }
        return certificatesFilter;
    }

    private Date map(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar==null) return null;
        return xmlGregorianCalendar.toGregorianCalendar().getTime();
    }

}
