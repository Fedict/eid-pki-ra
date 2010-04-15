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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.contexts.Contexts;

import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationManager;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationRepository;


/**
 * Handler for bulk approvals.
 * @author Jan Van den Bergh
 *
 */
@Name(BulkApprovalHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class BulkApprovalHandler implements Serializable {
	
	private static final long serialVersionUID = -6783798704960869564L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.bulkApprovalHandler";
	public static final String NAMEITEMS = "registrations";
	
	@In(value=RegistrationRepository.NAME, create=true)
	private RegistrationRepository registrationRepository;
	
	@In(value=RegistrationManager.NAME, create=true)
	private RegistrationManager registrationManager;
	
	private String reason;

	
	private Map<Registration, Boolean> selectedRegistrations;
	@DataModel
	private List<Registration> registrations;

	/**
	 * Initializes the bean.
	 * @return 
	 */
	@Factory(value=NAMEITEMS)
	public List<Registration> initRegistrations() {
		List<Registration> items = new ArrayList<Registration>();
		for(Registration registration: registrationRepository.findAllNewRegistrations()) {
			items.add(registration);
		}
		setSelectedRegistrations(new HashMap<Registration, Boolean>());
		registrations = items;
		return items;		
	}
	
	/**
	 * Approve the selected registrations.
	 */
	@SuppressWarnings("unchecked")
	public String approveRegistrations() {
		Iterator itr = getSelectedRegistrations().entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<Registration, Boolean> entry = (Entry<Registration, Boolean>) itr.next();
			if (entry.getValue().booleanValue()) {
				registrationManager.approveRegistration(entry.getKey().getId(), getReason());
				registrations.remove(entry.getKey());
			}
		}
		removeApprovals();
		return "approved";
	}

	/**
	 * Disapprove the selected registrations.
	 */
	@SuppressWarnings("unchecked")
	public String disapproveRegistrations() {
		Iterator itr = getSelectedRegistrations().entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<Registration, Boolean> entry = (Entry<Registration, Boolean>) itr.next();
			if (entry.getValue().booleanValue()) {
				registrationManager.disapproveRegistration(entry.getKey().getId(), getReason());
				registrations.remove(entry.getKey());
			}
		}
		removeApprovals();
		return "disapproved";
	}
	
	private void removeApprovals() {
		Contexts.getConversationContext().remove(NAMEITEMS);
	}
	

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public void setRegistrations(List<Registration> registrations) {
		this.registrations = registrations;
	}

	public List<Registration> getRegistrations() {
		return registrations;
	}

	public void setSelectedRegistrations(Map<Registration, Boolean> selectedRegistrations) {
		this.selectedRegistrations = selectedRegistrations;
	}

	public Map<Registration, Boolean> getSelectedRegistrations() {
		return selectedRegistrations;
	}
}