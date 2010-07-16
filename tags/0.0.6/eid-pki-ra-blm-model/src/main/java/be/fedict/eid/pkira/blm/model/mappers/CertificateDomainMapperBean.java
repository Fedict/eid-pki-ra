/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.generated.privatews.CertificateDomainWS;
import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.ObjectFactory;

/**
 * @author Jan Van den Bergh
 */
@Name(CertificateDomainMapper.NAME)
@Scope(ScopeType.STATELESS)
public class CertificateDomainMapperBean implements CertificateDomainMapper {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CertificateDomainWS map(CertificateDomain domain) {
		if (domain == null) {
			return null;
		}
		
		CertificateDomainWS result = new ObjectFactory().createCertificateDomainWS();
		result.setId(domain.getId()!=null ? domain.getId().toString() : null);
		result.setName(domain.getName());
		result.setDnExpression(domain.getDnExpression());
		
		if (domain.isClientCertificate()) {
			result.getCertificateTypes().add(CertificateTypeWS.CLIENT);
		}
		if (domain.isServerCertificate()) {
			result.getCertificateTypes().add(CertificateTypeWS.SERVER);
		}
		if (domain.isCodeSigningCertificate()) {
			result.getCertificateTypes().add(CertificateTypeWS.CODE);
		}
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CertificateDomainWS> map(Collection<CertificateDomain> domains) {
		List<CertificateDomainWS> result = new ArrayList<CertificateDomainWS>();
		if (domains == null) {
			return result;
		}

		for (CertificateDomain domain : domains) {
			result.add(map(domain));
		}

		return result;
	}

}
