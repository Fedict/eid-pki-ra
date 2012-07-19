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

package be.fedict.eid.pkira.blm.model.contracts;

import java.util.Collections;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

/**
 * @author Bram Baeyens
 *
 */
@Name(CertificateQuery.NAME)
public class CertificateQuery extends EntityQuery<Certificate> {

	private static final long serialVersionUID = -1687988818281531366L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.certificateQuery";
	
	@Override
	public String getEjbql() {
		return "select c from Certificate c";
	}
	
	@SuppressWarnings("unchecked")
	public List<Certificate> getFindCertificates(Integer certificateDomainId) {
		if (certificateDomainId==null) {
			return Collections.emptyList();
		}
		
		return getEntityManager().createNamedQuery(Certificate.NQ_FIND_CERTIFICATES_BY_CERTIFICATE_DOMAIN_ID)
				.setParameter("certificateDomainId", certificateDomainId)
				.getResultList();
	}

}
