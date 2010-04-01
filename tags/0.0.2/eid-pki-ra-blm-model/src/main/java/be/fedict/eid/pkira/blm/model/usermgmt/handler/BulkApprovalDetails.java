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
import java.util.List;

import org.jboss.seam.annotations.Name;

/**
 * Input of the bulk approval form.
 * 
 * @author Jan Van den Bergh
 */
@Name(BulkApprovalDetails.NAME)
public class BulkApprovalDetails implements Serializable {

	private static final long serialVersionUID = 9086965612890614589L;

	public static final String NAME = "be.fedict.eid.pkira.blm.bulkApprovalDetails";
	
	private String reason;
	private List<BulkApprovalItem> items;
	private int scrollerPage;
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}

	
	public List<BulkApprovalItem> getItems() {
		return items;
	}

	
	public void setItems(List<BulkApprovalItem> items) {
		this.items = items;
	}

	
	public int getScrollerPage() {
		return scrollerPage;
	}

	
	public void setScrollerPage(int scrollerPage) {
		this.scrollerPage = scrollerPage;
	}
	
	public boolean getHasData() {
		return items.size()==0;
	}
}
