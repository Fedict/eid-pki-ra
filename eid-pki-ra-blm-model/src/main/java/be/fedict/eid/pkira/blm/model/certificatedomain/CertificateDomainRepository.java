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

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.usermgmt.User;

/**
 * @author Bram Baeyens
 *
 */
@Local
public interface CertificateDomainRepository {

	String NAME = "be.fedict.eid.pkira.blm.certificateDomainRepository";
	
	void persist(CertificateDomain certificateDomain);

	CertificateDomain findByName(String name);

	CertificateDomain findByDnExpression(String string);

	CertificateDomain findById(Integer id);

	List<CertificateDomain> findUnregistered(User user);

	CertificateDomain getReference(Integer primaryKey);

	List<CertificateDomain> findByCertificateTypes(Set<CertificateType> types);	
	
	List<CertificateDomain> findAll();
	
}