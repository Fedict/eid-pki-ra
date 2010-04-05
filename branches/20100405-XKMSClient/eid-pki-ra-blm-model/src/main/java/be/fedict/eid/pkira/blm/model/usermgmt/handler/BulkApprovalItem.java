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

import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.blm.model.usermgmt.User;

/**
 * Item in the bulk approval table.
 * 
 * @author Jan Van den Bergh
 */
public class BulkApprovalItem {

	private String requester;
	private String certificateDomain;
	private String email;
	private boolean selected;
	private Integer registrationId;

	public BulkApprovalItem(Registration registration) {
		this.registrationId = registration.getId();
		User requester = registration.getRequester();
		this.requester = requester.getFirstName() + " " + requester.getLastName();
		this.certificateDomain = registration.getCertificateDomain().getName();
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

	public String getEmail() {
		return email;
	}

	public Integer getRegistrationId() {
		return registrationId;
	}

}
