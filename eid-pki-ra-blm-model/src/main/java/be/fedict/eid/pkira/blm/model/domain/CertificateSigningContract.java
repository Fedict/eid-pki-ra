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
package be.fedict.eid.pkira.blm.model.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.crypto.CertificateType;

/**
 * A contract used to request the signing of a certificate. 
 * @author Jan Van den Bergh
 */
@Entity
@DiscriminatorValue("Sign")
@Name(CertificateSigningContract.NAME)
public class CertificateSigningContract extends AbstractContract {
	
	private static final long serialVersionUID = 582083322769739724L;

	public static final String NAME="certificateSigningContract";

	private Integer validityPeriodMonths;
	private CertificateType certificateType;

	public Integer getValidityPeriodMonths() {
		return validityPeriodMonths;
	}

	public void setValidityPeriodMonths(Integer validityPeriodMonths) {
		this.validityPeriodMonths = validityPeriodMonths;
	}

	public CertificateType getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(CertificateType certificateType) {
		this.certificateType = certificateType;
	}
}
