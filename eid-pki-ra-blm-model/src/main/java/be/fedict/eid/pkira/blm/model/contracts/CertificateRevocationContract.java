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

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.jboss.seam.annotations.Name;

/**
 * A contract used to request the signing of a certificate.
 * 
 * @author Jan Van den Bergh
 */
@Entity
@DiscriminatorValue("Revoke")
@Name(CertificateRevocationContract.NAME)
public class CertificateRevocationContract extends AbstractContract {

	private static final long serialVersionUID = -4583829107256983511L;

	public static final String NAME = "certificateRevocationContract";

	private Date startDate;    
    private Date endDate;
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
