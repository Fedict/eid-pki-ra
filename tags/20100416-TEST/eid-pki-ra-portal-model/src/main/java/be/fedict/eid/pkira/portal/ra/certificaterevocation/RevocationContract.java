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

package be.fedict.eid.pkira.portal.ra.certificaterevocation;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.portal.certificate.Certificate;
import be.fedict.eid.pkira.portal.ra.AbstractContract;

/**
 * @author Bram Baeyens
 */
@Name(RevocationContract.NAME)
@Scope(ScopeType.CONVERSATION)
public class RevocationContract extends AbstractContract {

	public static final String NAME = "be.fedict.eid.pkira.portal.revocationContract";

	private static final long serialVersionUID = 6621109906303189341L;
	
	@In(required = true, scope = ScopeType.CONVERSATION, value = Certificate.NAME)
	private Certificate certificate;

	public Certificate getCertificate() {
		return certificate;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("RevocationContract[").append(super.toString()).append(']').toString();
	}
}
