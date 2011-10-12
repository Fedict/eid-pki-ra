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
import org.jboss.seam.annotations.Name;

/**
 * @author Jan Van den Bergh
 */
@Name(Registration.NAME)
@RegistrationEmailVerification
public class Registration implements Serializable {

	private static final long serialVersionUID = 6787261553475188888L;
	
	public static final String NAME = "be.fedict.pkira.portal.registration";

	private String id;
	private RegistrationStatus status;
	private String certificateDomainName;
	private String certificateDomainExpression;
	
	@NotEmpty(message="{registration.certificateDomain.required}")
	private String certificateDomainId;
	
	@NotEmpty(message="{registration.email.required}") 
	@Email(message="{registration.email.invalid}")
	private String emailAddress;
	
	@NotEmpty(message="{registration.email.required}") 
	private String emailAddressVerification;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RegistrationStatus getStatus() {
		return status;
	}

	public void setStatus(RegistrationStatus status) {
		this.status = status;
	}

	public String getCertificateDomainName() {
		return certificateDomainName;
	}

	public void setCertificateDomainName(String certificateDomainName) {
		this.certificateDomainName = certificateDomainName;
	}
	
	public String getCertificateDomainId() {
		return certificateDomainId;
	}

	public void setCertificateDomainId(String certificateDomainId) {
		this.certificateDomainId = certificateDomainId;
	}

	
	public String getCertificateDomainExpression() {
		return certificateDomainExpression;
	}

	
	public void setCertificateDomainExpression(String certificateDomainExpression) {
		this.certificateDomainExpression = certificateDomainExpression;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	
	public String getEmailAddressVerification() {
		return emailAddressVerification;
	}

	
	public void setEmailAddressVerification(String emailAddressVerification) {
		this.emailAddressVerification = emailAddressVerification;
	}

}
