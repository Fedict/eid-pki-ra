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

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
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
public class BulkApprovalHandler {

	public static final String NAME = "be.fedict.eid.pkira.blm.bulkApprovalHandler";
	
	@In(value=RegistrationRepository.NAME, create=true)
	private RegistrationRepository registrationRepository;
	
	@In(value=RegistrationManager.NAME, create=true)
	private RegistrationManager registrationManager;
	
	@In(value=BulkApprovalDetails.NAME, required=false)
	private BulkApprovalDetails bulkApprovalDetails;	
	
	/**
	 * Initializes the bean.
	 * @return 
	 */
	@Begin(join=true)
	@Factory(value=BulkApprovalDetails.NAME, scope=ScopeType.CONVERSATION)
	public BulkApprovalDetails getBulkApprovalDetails() {		
		List<BulkApprovalItem> items = new ArrayList<BulkApprovalItem>();
		for(Registration registration: registrationRepository.findAllNewRegistrations()) {
			items.add(new BulkApprovalItem(registration));
		}
		
		BulkApprovalDetails result = new BulkApprovalDetails();
		result.setItems(items);
		result.setScrollerPage(1);
		
		return result;
	}
	
	/**
	 * Approve the selected registrations.
	 */
	public void approveRegistrations() {
		for(BulkApprovalItem item: bulkApprovalDetails.getItems()) {
			if (item.isSelected()) {
				registrationManager.approveRegistration(item.getRegistrationId(), bulkApprovalDetails.getReason());
			}
		}
		
		removeApprovals();
	}
	
	/**
	 * Disapprove the selected registrations.
	 */
	public void disapproveRegistrations() {
		for(BulkApprovalItem item: bulkApprovalDetails.getItems()) {
			if (item.isSelected()) {
				registrationManager.disapproveRegistration(item.getRegistrationId(), bulkApprovalDetails.getReason());
			}
		}
		
		removeApprovals();
	}

	private void removeApprovals() {
		Contexts.getConversationContext().remove(BulkApprovalDetails.NAME);
	}
}
