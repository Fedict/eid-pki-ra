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
package be.fedict.eid.pkira.portal.handler;

import java.io.Serializable;
import java.util.List;

import be.fedict.eid.pkira.portal.domain.Certificate;

public interface CertificateHandler extends Serializable {
	
	String NAME = "be.fedict.eid.pkira.portal.certificateHandler";
	
	List<Certificate> findCertificateList(String userRRN);
	
	String prepareRevocation(String serialNumber, String issuer);

	String showDetail(Certificate certificate);
	
	String getCertificate(String serialNumber);
	
	String download();
}
