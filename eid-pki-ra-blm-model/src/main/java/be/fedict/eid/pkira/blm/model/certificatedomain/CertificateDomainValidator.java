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

package be.fedict.eid.pkira.blm.model.certificatedomain;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.hibernate.validator.Validator;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityController;

import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.dnfilter.DistinguishedName;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameManager;
import be.fedict.eid.pkira.dnfilter.InvalidDistinguishedNameException;

/**
 * @author Bram Baeyens
 *
 */
@Name(CertificateDomainValidator.NAME)
public class CertificateDomainValidator extends EntityController implements Validator<CertificateDomainValidation> {

	private static final long serialVersionUID = 6890606988756527956L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.certificateDomainValidator";
	
	@In(create=true, value=DistinguishedNameManager.NAME)
	private DistinguishedNameManager distinguishedNameManager;
	
	public void validate(CertificateDomain certificateDomain) throws CertificateDomainException, InvalidDistinguishedNameException {
		validateName(certificateDomain);
		validateCertificateTypes(certificateDomain);
		validateDomainName(certificateDomain);
	}

	private void validateName(CertificateDomain certificateDomain) throws InvalidCertificateDomainNameException {
		CertificateDomain existing = findByName(certificateDomain.getName());
		if (existing != null && !existing.equals(certificateDomain)) {
			throw new InvalidCertificateDomainNameException();
		}		
	}
	
	private void validateCertificateTypes(CertificateDomain certificateDomain) throws NoCertificateTypesSelectedException {
		if (certificateDomain.getCertificateTypes().isEmpty()) {
			throw new NoCertificateTypesSelectedException();
		}		
	}

	private void validateDomainName(CertificateDomain certificateDomain) throws DistinguishedNameOverlapsException, InvalidDistinguishedNameException {
		// Create the DN
		DistinguishedName dn = distinguishedNameManager.createDistinguishedName(certificateDomain.getDnExpression());
		
		// Look up the matching certificate domains
		List<CertificateDomain> allDomains = findByCertificateTypes(certificateDomain.getCertificateTypes());

		// Look for overlaps
		for (CertificateDomain otherDomain : allDomains) {
			try {
				DistinguishedName otherDN = distinguishedNameManager.createDistinguishedName(otherDomain.getDnExpression());
				if (otherDN.matches(dn) && !otherDomain.equals(certificateDomain)) {
					throw new DistinguishedNameOverlapsException();
				}
			} catch (InvalidDistinguishedNameException e) {
				throw new RuntimeException("Invalid DN expression found in database: " + otherDomain.getDnExpression());
			}
		}		
	}

	protected CertificateDomain findByName(String name) {
		try {
			return (CertificateDomain) getEntityManager().createNamedQuery("findCertificateDomainByName").setParameter(
					"name", name).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
		
	@SuppressWarnings("unchecked")
	protected List<CertificateDomain> findByCertificateTypes(Set<CertificateType> types) {
		if (types == null || types.size() == 0) {
			return Collections.emptyList();
		}

		// Build the query
		StringBuilder query = new StringBuilder();
		for (CertificateType type : types) {
			if (query.length() != 0) {
				query.append(" OR ");
			}
			query.append(type.name());
			query.append("CERT=true");
		}
		query.insert(0, "FROM CertificateDomain WHERE ");

		// Execute it
		return getEntityManager().createQuery(query.toString()).getResultList();
	}

	public void setDistinguishedNameManager(DistinguishedNameManager distinguishedNameManager) {
		this.distinguishedNameManager = distinguishedNameManager;
	}

	@Override
	public void initialize(CertificateDomainValidation parameters) {		
	}

	@Override
	public boolean isValid(Object value) {
		return false;
	}
}
