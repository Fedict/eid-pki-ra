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
package be.fedict.eid.pkira.portal.registration;

import java.io.Serializable;

import org.hibernate.validator.Email;
import org.hibernate.validator.NotEmpty;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * @author Jan Van den Bergh
 */
@Name(Registration.NAME)
@Scope(ScopeType.CONVERSATION)
public class Registration implements Serializable {

	private static final long serialVersionUID = 6787261553475188888L;
	
	public static final String NAME = "be.fedict.pkira.portal.registration";

	@NotEmpty
	private String selectedDomainId;
	
	@NotEmpty @Email
	private String emailAddress;
	
	public String getSelectedDomainId() {
		return selectedDomainId;
	}

	public void setSelectedDomainId(String selectedDomain) {
		this.selectedDomainId = selectedDomain;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
