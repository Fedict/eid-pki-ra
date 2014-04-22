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

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainHome;
import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationStatus;

/**
 * @author Bram Baeyens
 *
 */
@Name(ContractQuery.NAME)
public class ContractQuery extends EntityQuery<AbstractContract> {

	private static final long serialVersionUID = -1687988818281539366L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.contractQuery";
	
	private Registration registration;

	@In(value = CertificateDomainHome.NAME, create = true)
	private CertificateDomainHome certificateDomainHome;
	
	@Override
	public String getEjbql() {
		return "select contract from AbstractContract contract";
	}
	
	@SuppressWarnings("unchecked")
	public List<AbstractContract> getFindContracts(Integer certificateDomainId, String userRrn) {
		Query query = getEntityManager().createNamedQuery(getNamedQuery(certificateDomainId))
				.setParameter("nationalRegisterNumber", userRrn)
				.setParameter("registrationStatus", RegistrationStatus.APPROVED);
		if (certificateDomainId != null) {
			query.setParameter("certificateDomainId", certificateDomainId);
		}
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<AbstractContract> getFindContracts(Integer certificateDomainId) {
		if (certificateDomainId==null) {
			return Collections.emptyList();
		}
		
		return getEntityManager().createNamedQuery(AbstractContract.NQ_FIND_CONTRACTS_BY_CERTIFICATE_DOMAIN_ID)
				.setParameter("certificateDomainId", certificateDomainId)
				.getResultList();
	}
	
	private String getNamedQuery(Integer certificateDomainId) {
		if (certificateDomainId == null) {
			return AbstractContract.NQ_FIND_CONTRACTS_BY_USER_RRN;
		} else {
			return AbstractContract.NQ_FIND_CONTRACTS_BY_USER_RRN_AND_CERTIFICATE_DOMAIN_ID;
		}
	}

	public Registration getRegistration() {
		return registration;
	}

	public CertificateDomainHome getCertificateDomainHome() {
		return certificateDomainHome;
	}
}
