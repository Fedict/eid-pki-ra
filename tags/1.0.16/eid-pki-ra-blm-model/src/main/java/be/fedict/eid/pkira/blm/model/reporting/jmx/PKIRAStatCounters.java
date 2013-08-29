/**
 * eID PKI RA Project. 
 * Copyright (C) 2010-2012 FedICT. 
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
package be.fedict.eid.pkira.blm.model.reporting.jmx;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.blm.model.reporting.ReportEntry.ContractType;

@Name(PKIRAStatCountersMBean.NAME)
@Scope(ScopeType.APPLICATION)
public class PKIRAStatCounters implements PKIRAStatCountersMBean {

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getCertificateDomains() {
		return getEntityManager().createQuery("SELECT name FROM CertificateDomain").getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getSuccessfulRequestCount(String certificateDomainName) {
		return executeCountQuery(certificateDomainName, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getFailedRequestCount(String certificateDomainName) {
		return executeCountQuery(certificateDomainName, false);
	}

	private long executeCountQuery(String certificateDomainName, boolean success) {
		Query query = getEntityManager()
				.createQuery(
						"SELECT COUNT(*) FROM ReportEntry WHERE certificateDomainName=:certificateDomainName AND success=:success AND contractType=:type");
		query.setParameter("certificateDomainName", certificateDomainName);
		query.setParameter("success", success);
		query.setParameter("type", ContractType.REQUEST);
		return (Long) query.getSingleResult();
	}

	protected EntityManager getEntityManager() {
		// FIXME. For some reason, this method only works the second time. Find a better way to do this.
		int attempt = 1;
		while (true) {
			try {
				return (EntityManager) Component.getInstance("entityManager");
			} catch (IllegalArgumentException e) {
				if (attempt == 5) {
					throw e;
				}
				attempt++;
			}
		}
	}

}
