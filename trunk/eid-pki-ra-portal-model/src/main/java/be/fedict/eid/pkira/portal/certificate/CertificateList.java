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

import java.util.List;

import javax.ejb.Local;

@Local
public interface CertificateList {
	
	String NAME = "be.fedict.eid.pkira.portal.certificatelist";
	
	/**
	 * 
	 */
	public void certificateList();
	
	/**
	 * 
	 */
	public void destroy();
	
	/**
	 * 
	 */
	public String detailCertificate(Certificate certificate);
	
	public List<Certificate> findCertificateList();
}
