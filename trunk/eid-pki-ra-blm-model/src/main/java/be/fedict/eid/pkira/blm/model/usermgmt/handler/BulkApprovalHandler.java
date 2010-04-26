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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationHome;


/**
 * Handler for bulk approvals.
 * @author Jan Van den Bergh
 * @author Bram Baeyens
 *
 */
@Name(BulkApprovalHandler.NAME)
public class BulkApprovalHandler implements Serializable {
	
	private static final long serialVersionUID = -6783798704960869564L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.bulkApprovalHandler";
	
	@In(value=RegistrationHome.NAME, create=true)
	private RegistrationHome registrationHome;
	
	private String reason;
	
	private Map<Integer, Boolean> selectedRegistrations = new HashMap<Integer, Boolean>();
	
	/**
	 * Approve the selected registrations.
	 */
	public String approveRegistrations() {
		for(Entry<Integer, Boolean> entry : selectedRegistrations.entrySet()) {
			if (entry.getValue()) {
				registrationHome.approve(entry.getKey(), reason);
			}
		}
		selectedRegistrations.clear();
		return "approved";
	}

	/**
	 * Disapprove the selected registrations.
	 */
	public String disapproveRegistrations() {
		for(Entry<Integer, Boolean> entry : selectedRegistrations.entrySet()) {
			if (entry.getValue()) {
				registrationHome.disapprove(entry.getKey(), reason);
			}
		}
		selectedRegistrations.clear();
		return "disapproved";
	}	

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public void setSelectedRegistrations(Map<Integer, Boolean> selectedRegistrations) {
		this.selectedRegistrations = selectedRegistrations;
	}

	public Map<Integer, Boolean> getSelectedRegistrations() {
		return selectedRegistrations;
	}
}