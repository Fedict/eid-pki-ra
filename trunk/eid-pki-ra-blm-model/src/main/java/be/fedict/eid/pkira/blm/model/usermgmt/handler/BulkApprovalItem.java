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
package be.fedict.eid.pkira.blm.model.usermgmt.handler;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.blm.model.usermgmt.User;

/**
 * Item in the bulk approval table.
 * 
 * @author Jan Van den Bergh
 */

@Entity
@Name(BulkApprovalItem.NAME)
public class BulkApprovalItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2894324903467525328L;

	public static final String NAME = "be.fedict.eid.pkira.blm.bulkApprovalItem";
	
	@Id
	@GeneratedValue
	private String id;
	
	private String requester;
	private String certificateDomain;
	private String email;
	private boolean selected;
	private Integer registrationId;

	public BulkApprovalItem(){}
	
	public BulkApprovalItem(Registration registration) {
		this.registrationId = registration.getId();
		User requester = registration.getRequester();
		this.requester = requester.getFirstName() + " " + requester.getLastName();
		this.setCertificateDomain(registration.getCertificateDomain().getName());
		this.email = registration.getEmail();
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getRequester() {
		return requester;
	}

	public String getCertificateDomain() {
		return certificateDomain;
	}
	
	public void setCertificateDomain(String certificateDomain) {
		this.certificateDomain = certificateDomain;
	}

	public String getEmail() {
		return email;
	}

	public Integer getRegistrationId() {
		return registrationId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
