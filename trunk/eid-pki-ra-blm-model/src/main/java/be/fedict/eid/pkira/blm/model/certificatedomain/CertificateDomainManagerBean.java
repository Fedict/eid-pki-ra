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
package be.fedict.eid.pkira.blm.model.certificatedomain;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.domain.CertificateType;
import be.fedict.eid.pkira.dnfilter.DistinguishedName;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameManager;
import be.fedict.eid.pkira.dnfilter.InvalidDistinguishedNameException;

/**
 * @author Jan Van den Bergh
 */
@Stateless
@Name(CertificateDomainManager.NAME)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CertificateDomainManagerBean implements CertificateDomainManager {

	@In(value = DistinguishedNameManager.NAME, create = true)
	private DistinguishedNameManager distinguishedNameManager;

	@In(value = CertificateDomainRepository.NAME, create = true)
	private CertificateDomainRepository domainRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CertificateDomain registerCertificateDomain(String name, String caId, String dnExpression,
			Set<CertificateType> types) throws InvalidDistinguishedNameException, DistinguishedNameOverlapsException,
			DuplicateCertificateDomainNameException {
		// Validate the name
		if (domainRepository.findByName(name) != null) {
			throw new DuplicateCertificateDomainNameException();
		}
		
		// Validate the DN expression
		DistinguishedName dn = distinguishedNameManager.createDistinguishedName(dnExpression);
		checkForOverlaps(dn, types);
		

		// Create the domain
		CertificateDomain domain = new CertificateDomain();
		domain.setName(name);
		domain.setDnExpression(dnExpression);
		domain.setForClientCertificate(types.contains(CertificateType.CLIENT));
		domain.setForCodeSigningCertificate(types.contains(CertificateType.CODE));
		domain.setForServerCertificate(types.contains(CertificateType.SERVER));
		domainRepository.persist(domain);
		// TODO fill in CA

		return domain;
	}

	/**
	 * Checks if the DN overlaps with the already registered ones.
	 */
	private void checkForOverlaps(DistinguishedName dn, Set<CertificateType> types)
			throws DistinguishedNameOverlapsException {
		// Look up the matching certificate domains
		List<CertificateDomain> domains = domainRepository.findByCertificateTypes(types);

		// Look for overlaps
		for (CertificateDomain domain : domains) {
			try {
				DistinguishedName otherDN = distinguishedNameManager.createDistinguishedName(domain.getDnExpression());
				if (otherDN.matches(dn)) {
					throw new DistinguishedNameOverlapsException(domain.getDnExpression());
				}
			} catch (InvalidDistinguishedNameException e) {
				throw new RuntimeException("Invalid DN expression found in database: " + domain.getDnExpression());
			}
		}
	}

	protected void setDistinguishedNameManager(DistinguishedNameManager distinguishedNameManager) {
		this.distinguishedNameManager = distinguishedNameManager;
	}

	protected void setDomainRepository(CertificateDomainRepository domainRepository) {
		this.domainRepository = domainRepository;
	}

}
