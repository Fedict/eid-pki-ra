/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
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

import static be.fedict.eid.pkira.contracts.util.JAXBUtil.createXmlGregorianCalendar;

import java.util.Collection;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.blm.model.contracts.Certificate;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;
import be.fedict.eid.pkira.generated.privatews.ObjectFactory;

/**
 * Mapper for certificates from WS to model and back.
 * 
 * @author Jan Van den Bergh
 */
@Name(CertificateMapper.NAME)
@Scope(ScopeType.STATELESS)
public class CertificateMapperBean implements CertificateMapper {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CertificateWS map(Certificate certificate, boolean includeX509) {
		if (certificate == null) {
			return null;
		}

		CertificateWS result = new ObjectFactory().createCertificateWS();
		result.setCertificateId(certificate.getId());
		result.setIssuer(certificate.getIssuer());
		result.setRequesterName(certificate.getRequesterName());
		result.setDistinguishedName(certificate.getDistinguishedName());
		result.setValidityEnd(createXmlGregorianCalendar(certificate.getValidityEnd()));
		result.setValidityStart(createXmlGregorianCalendar(certificate.getValidityStart()));
		result.setCertificateType(map(certificate.getCertificateType()));
		result.setSerialNumber(mapToString(certificate.getSerialNumber()));
		if (includeX509) {
			result.setX509(certificate.getX509());
			result.setCertificateZip(certificate.getCertificateZip());
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CertificateTypeWS map(CertificateType certificateType) {
		if (certificateType == null) {
			return null;
		}

		return Enum.valueOf(CertificateTypeWS.class, certificateType.name());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void map(Iterable<CertificateType> source, Collection<CertificateTypeWS> result) {
		for (CertificateType type : source) {
			CertificateTypeWS mapped = map(type);
			if (mapped != null) {
				result.add(mapped);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CertificateType map(CertificateTypeWS certificateType) {
		if (certificateType == null) {
			return null;
		}

		return Enum.valueOf(CertificateType.class, certificateType.name());
	}

	private String mapToString(Object object) {
		return object == null ? null : object.toString();
	}
}
