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
package be.fedict.eid.pkira.blm.model.contracts;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.jboss.seam.annotations.Name;

/**
 * A contract used to request the signing of a certificate. 
 * @author Jan Van den Bergh
 */
@Entity
@DiscriminatorValue("Sign")
@Name(CertificateSigningContract.NAME)
public class CertificateSigningContract extends AbstractContract {
	
	private static final long serialVersionUID = 582083322769739724L;

	public static final String NAME = "be.fedict.eid.pkira.blm.certificateSigningContract";

	@Column(name="VALIDITY")
	private Integer validityPeriodMonths;
	
	@Enumerated(EnumType.STRING)
	@Column(name="CERTIFICATE_TYPE")
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

	@Override
	protected void appendFields(StringBuilder builder) {
		super.appendFields(builder);
		appendField(builder, "Validity period", validityPeriodMonths);
		appendField(builder, "Certificate type", certificateType);
	}
}
