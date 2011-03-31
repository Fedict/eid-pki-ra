/**
 * eID PKI RA Project. 
 * Copyright (C) 2010 FedICT. 
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

import java.io.Serializable;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;

/**
 * @author Bram Baeyens
 *
 */
@Name(CertificateMapper.NAME)
@Scope(ScopeType.STATELESS)
public class CertificateMapper implements Serializable {

	private static final long serialVersionUID = -7768339678024295092L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.certificateMapper";
	
	public Certificate map(CertificateWS certificateWS) {
		Certificate certificate = new Certificate();
		certificate.setId(certificateWS.getCertificateId());
		certificate.setDistinguishedName(certificateWS.getDistinguishedName());
		certificate.setIssuer(certificateWS.getIssuer());
		certificate.setSerialNumber(certificateWS.getSerialNumber());
		certificate.setType(map(certificateWS.getCertificateType()));
		certificate.setValidityStart(map(certificateWS.getValidityStart()));
		certificate.setValidityEnd(map(certificateWS.getValidityEnd()));
		certificate.setX509(certificateWS.getX509());
		certificate.setRequesterName(certificateWS.getRequesterName());		
		return certificate;
	}

	private CertificateType map(CertificateTypeWS certificateTypeWS) {
		return Enum.valueOf(CertificateType.class, certificateTypeWS.name());
	}

	private Date map(XMLGregorianCalendar xmlGregorianCalendar) {
		if (xmlGregorianCalendar == null) {
			return null;
		} else {
			return xmlGregorianCalendar.toGregorianCalendar().getTime();
		}
	}
}
